package gridchallenge;

import java.util.LinkedList;
import java.util.HashMap;

public class CCLevel
{
  // pointer to next level
  private CCLevel next = null;
  // x, y, layer
  private int[][][] map;
  // used for parsing during ctor; unused afterwards.
  private byte[] leveldata;
  private int idx = 0;
  // information about the level
  private int levelNumber, timeLimit, numChips;   // timeLimit of 0 is no limit
  private String title, hint, password;
  private LinkedList<Point> movingMonsters = new LinkedList<Point>();
  private HashMap<Point, Point> trapControls = new HashMap<Point, Point>();
  private HashMap<Point, Point> cloneControls = new HashMap<Point, Point>();

  // getters
  public int getObjectAt(int x, int y, int layer) { return map[x][y][layer]; }
  public LinkedList<Point> getMonsterLocations() { return movingMonsters; }
  public int getLevelNumber() { return levelNumber; }
  public int getTimeLimit() { return timeLimit; }
  public int getChipCount() { return numChips; }
  public CCLevel getNextLevel() { return next; }
  public String getTitle() { return title; }
  public String getHint() { return hint; }
  public String getPassword() { return password; }
  public Point getTrapForButton(Point button) { return trapControls.get(button); }
  public Point getCloneForButton(Point button) { return cloneControls.get(button); }
  
  // setters
  public void setNextLevel(CCLevel next) { this.next = next; }

  // ctor
  public CCLevel(byte[] leveldata)
  {
    // setup map data
    map = new int[32][][];
    for (int i = 0; i < 32; i++)
    {
      map[i] = new int[32][];
      for (int j = 0; j < 32; j++)
        map[i][j] = new int[2];
    }

    this.leveldata = leveldata;

    try { parseLevelData(); } catch(ArrayIndexOutOfBoundsException ex) {}

    // free after we're done with it.
    this.leveldata = null;
  }

  // private methods used during parsing
  private void parseLevelData() throws ArrayIndexOutOfBoundsException
  {
    final boolean debug = false;
    levelNumber = readInt16();
    timeLimit   = readInt16();
    numChips    = readInt16();
    if (debug) System.out.println("Read: 6 bytes");

    boolean breakFree = false;
    for (;;)
    {
      int field = readInt8();
      int len = readInt8();
      switch (field)
      {
        case 1:
          if (debug) System.out.println("Field 1:");
          int sizeFirstLayer = readInt16();
          int[] firstlayer = readInts8(sizeFirstLayer);

          int sizeSecondLayer = readInt16();
          int[] secondlayer = readInts8(sizeSecondLayer);

          copyLayerIntoMap(0, firstlayer);
          copyLayerIntoMap(1, secondlayer);

          int bytesRemaining = readInt16();
          if (debug) System.out.println("  Read: " + (8 + sizeFirstLayer + sizeSecondLayer) + " bytes");
          break;
        case 3:
          if (debug) System.out.println("Field 3:");
          title = readString(len);
          if (debug) System.out.println("  Title: " + title);
          if (debug) System.out.println("  Read: " + (2 + len) + " bytes");
          break;
        case 4:
          if (debug) System.out.println("Field 4:");
          int[] trapcontrols = readInts8(len);
          handleTrapControls(trapcontrols);
          break;
        case 5:
          if (debug) System.out.println("Field 5:");
          int[] cloningcontrols = readInts8(len);
          handleCloningControls(cloningcontrols);
          break;
        case 6:
          if (debug) System.out.println("Field 6:");
          password = convertPassword(readString(len));
          if (debug) System.out.println("  Password: " + password);
          if (debug) System.out.println("  Read: " + (2 + len) + " bytes");
          break;
        case 7:
          if (debug) System.out.println("Field 7:");
          hint = readString(len);
          if (debug) System.out.println("  Hint: " + hint);
          if (debug) System.out.println("  Read: " + (2 + len) + " bytes");
          break;
        case 10:
          if (debug) System.out.println("Field 10:");
          // monster motion
          for (int m = 0; m < len; m += 2)
          {
            int x = readInt8();
            int y = readInt8();
            movingMonsters.add(new Point(x, y));
            if (debug) System.out.println("  Monster: " + x + ", " + y);
          }
          break;
        default:
          throw new RuntimeException("We ought to handle field type " + field);
      }
    }
  }

  private void handleCloningControls(int[] ctrls)
  {
    for (int i = 0; i < ctrls.length; i += 8)
    {
      cloneControls.put(
          new Point(ctrls[i + 0] + 256 * ctrls[i + 1],
            ctrls[i + 2] + 256 * ctrls[i + 3]),
          new Point(ctrls[i + 4] + 256 * ctrls[i + 5],
            ctrls[i + 6] + 256 * ctrls[i + 7])
          );
    }
  }

  private void handleTrapControls(int[] ctrls)
  {
    for (int i = 0; i < ctrls.length; i += 10)
    {
      trapControls.put(
          new Point(ctrls[i + 0] + 256 * ctrls[i + 1],
            ctrls[i + 2] + 256 * ctrls[i + 3]),
          new Point(ctrls[i + 4] + 256 * ctrls[i + 5],
            ctrls[i + 6] + 256 * ctrls[i + 7])
          );
      // last two bytes ([i + 8] [i + 9]) are terminating nulls.
    }
  }

  private void copyLayerIntoMap(int elevation, int[] rleData)
  {
    int outidx = 0;
    for (int i = 0;;)
    {
      switch(rleData[i])
      {
        case 0xFF:
          int copies = rleData[i + 1] + outidx;
          int ofwhat = rleData[i + 2];
          for (; outidx < copies; outidx++)
            map[outidx & 0x1F][outidx >> 5][elevation] = ofwhat;
          i += 3;
          break;
        default:
          map[outidx & 0x1f][outidx >> 5][elevation] = rleData[i];
          i++;
          outidx++;
          break;
      }
      if (i >= rleData.length) break;
    }
  }

  private String readString(int len) throws ArrayIndexOutOfBoundsException
  {
    int[] nums = readInts8(len);
    char[] res = new char[len - 1];

    for (int i = 0; i < res.length; i++)
      res[i] = (char)nums[i];

    return new String(res);
  }

  private int[] readInts8(int count) throws ArrayIndexOutOfBoundsException
  {
    int[] res = new int[count];
    for (int i = 0; i < count; i++) res[i] = readInt8();
    return res;
  }

  private int readInt16() throws ArrayIndexOutOfBoundsException
  {
    int lsb = readInt8();
    int msb = readInt8();
    return msb * 256 + lsb;
  }

  private int readInt8() throws ArrayIndexOutOfBoundsException
  {
    int r = leveldata[idx] & 0xFF;
    idx++;
    return r;
  }

  private String convertPassword(String pass)
  {
    char[] cs = pass.toCharArray();
    for (int i = 0; i < cs.length; i++)
    {
      cs[i] ^= 0x99;
    }
    return new String(cs);
  }
}
