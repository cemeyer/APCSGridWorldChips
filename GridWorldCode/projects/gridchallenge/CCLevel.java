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
  public int getObjectAt(int x, int y, int layer)
  {
    int res;
    try
    {
      res = map[x][y][layer];
    }
    catch (java.lang.ArrayIndexOutOfBoundsException ex)
    {
      res = 1; // wall
    }
    return res;
  }
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
  public void setMonsterLocations(LinkedList<Point> m) { movingMonsters = m; }

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

  public void moveChip(CCPlayer old, Point newp)
  {
    int[] oldchiploc = map[old.chip.x][old.chip.y];
    int[] newploc = map[newp.x][newp.y];
    int dx = newp.x - old.chip.x;
    int dy = newp.y - old.chip.y;

    if (newploc[0] == 0x0a)
    {
      // dirt
      int[] futuredirt = map[newp.x + dx][newp.y + dy];
      if (!(futuredirt[0] == 0x00 || futuredirt[0] == 0x03 ||
            futuredirt[0] == 0x0b))
      {
        System.out.println("can't push dirt into a wall");
        return;
      }

      // dirt can be moved
      int[] newloc = futuredirt;
      if (newloc[0] == 0x00)  // if we're pushing onto blank tile
      {
        newloc[0] = 0x0a;
      }
      else if (newloc[0] == 0x0b)   // onto dirt
      {
        // turns out movabledirt + dirt = dirt
        // so do nothing.
        //newloc[0] = 0x0a;
        //newloc[1] = 0x0b;
      }
      else if (newloc[0] == 0x03)   // onto water
      {
        newloc[0] = 0x0b;
      }
      newploc[0] = newploc[1];
      newploc[1] = 0;
    }

    if (newploc[0] == 0)
    {
      // remove ourself from old square
      oldchiploc[0] = 0;

      // if the old square had something, restore it up
      if (oldchiploc[1] != 0)
      {
        oldchiploc[0] = oldchiploc[1];
        oldchiploc[1] = 0;
      }

      int value = 0x6c | direction(newp.x - old.chip.x,
          newp.y - old.chip.y);

      // put ourself into the new square
      newploc[0] = value;
      old.chip = newp;
    }
    else if (newploc[1] == 0)
    {
      int oldtile = newploc[0];

      if (oldtile == 0x01 || oldtile == 0x05 ||
          (oldtile == 0x22 &&
           ((old.numChips < getChipCount()) && !old.world.godMode))
          || oldtile == 0x25)
      {
        // wall / invis wall / socket / switchwall closed
        return;
      }
      if (oldtile > 0x15 && oldtile < 0x20)
      {
        // color doors
        switch (oldtile - 0x16)
        {
          case 0:
            // blue
            if (old.numBlue > 0)
            {
              newploc[0] = 0;
              old.numBlue -= 1;
            }
            else return;
            break;
          case 1:
            // red
            if (old.numRed > 0)
            {
              newploc[0] = 0;
              old.numRed -= 1;
            }
            else return;
            break;
          case 2:
            // green
            if (old.numGreen > 0)
            {
              newploc[0] = 0;
              // don't decrement this; turns out green keys last forever
//            old.numGreen -= 1;
            }
            else return;
            break;
          case 3:
            // yellow
            if (old.numYellow > 0)
            {
              newploc[0] = 0;
              old.numYellow -= 1;
            }
            else return;
            break;
        }
      }

      // remove ourself from old square
      oldchiploc[0] = 0;

      // if the old square had something, restore it up
      if (oldchiploc[1] != 0)
      {
        oldchiploc[0] = oldchiploc[1];
        oldchiploc[1] = 0;
      }

      int value = 0x6c | direction(dx, dy);
      
      // what have we stepped on?
      switch (oldtile)
      {
        case 0x64:
          // blue key
          old.numBlue += 1;
          break;
        case 0x65:
          // red key
          old.numRed += 1;
          break;
        case 0x66:
          // green key
          old.numGreen += 1;
          break;
        case 0x67:
          // yellow key
          old.numYellow += 1;
          break;
        case 0x02:
          // chip
          old.numChips += 1;
          break;
        case 0x2f:
          // hint
          newploc[1] = newploc[0];
          break;
        case 0x04:
          // fire
          // until we figure out how to kill chip
          newploc[1] = newploc[0];
          break;
        case 0x15:
          // exit
          GridChallengeRunner.startLevel(levelNumber + 1);
          old.world.frame.dispose();
          break;
        case 0x22:
          // socket
        case 0x0b:
          // dirt
          break;
        default:
          newploc[1] = newploc[0];
          break;
      }

      // put ourself into the new square
      newploc[0] = value;
      old.chip = newp;
    }
  }

  public int direction(int dx, int dy)
  {
    int value = 0;

    if (dx > 0)
      value = 3;
    else if (dx < 0)
      value = 1;
    else if (dy > 0)
      value = 2;
    else if (dy < 0)
      value = 0;

    return value;
  }

  public Point moveMonster(Point oldpos, int id)
  {
    Point newpos = oldpos;

    switch (id & 0xfc)
    {
      case 0x40:
        // bug
        return moveBug(oldpos, id & 0x03);
      case 0x6c:
        // chip -- this shouldn't happen but let's prevent it anyways
        return oldpos;
    }

    switch (id & 0x03)
    {
      case 0: // N
        newpos = new Point(oldpos.x, oldpos.y - 1);
        break;
      case 1: // W
        newpos = new Point(oldpos.x - 1, oldpos.y);
        break;
      case 2: // S
        newpos = new Point(oldpos.x, oldpos.y + 1);
        break;
      case 3: // E
        newpos = new Point(oldpos.x + 1, oldpos.y);
        break;
    }

    int[] newploc = map[newpos.x][newpos.y];
    if (newploc[0] == 0)
    {
      // (don't) bubble up the thing the monster pushed down
//    map[oldpos.x][oldpos.y][0] = map[oldpos.x][oldpos.y][1];
//    map[oldpos.x][oldpos.y][1] = 0;

      // move ourself to that square
      map[oldpos.x][oldpos.y][0] = 0;
      newploc[0] = id;
      return newpos;
    }
//  else if (newploc[1] == 0)
//  {
//    // bubble up the thing the monster pushed down
//    map[oldpos.x][oldpos.y][0] = map[oldpos.x][oldpos.y][1];
//    map[oldpos.x][oldpos.y][1] = 0;

//    newploc[1] = newploc[0];
//    newploc[0] = id;
//    return newpos;
//  }
    else
    {
      map[oldpos.x][oldpos.y][0] = id ^ 0x02; // 180 degree turn
      return oldpos;
    }
  }

  public Point moveBug(Point oldpos, int direction)
  {
    Point forward;
    int count = 0;

    do
    {
      forward = oldpos.add(direction);
      direction = CCLevel.turnRight(direction);
      if (++count > 5) return oldpos;
    }
    while (whatsAt(forward, 0) != 0);

    // correct for over-spinning earlier.
    direction = CCLevel.turnLeft(direction);

    // turn left if left of us will be open.
    if (whatsAt(forward.add(CCLevel.turnLeft(direction)), 0) == 0 ||
        (whatsAt(forward.add(CCLevel.turnLeft(direction)), 0) & 0xfc)
        == 0x40)
      direction = CCLevel.turnLeft(direction);

    map[oldpos.x][oldpos.y][0] = 0;
    map[forward.x][forward.y][0] = 0x40 | direction;

    return forward;
  }

  public int whatsAt(Point where, int level)
  {
    return map[where.x][where.y][level];
  }

  public static int turnLeft(int dir)
  {
    return (dir + 1) % 4;
  }

  public static int turnRight(int dir)
  {
    return (dir + 3) % 4;
  }

  public static int dx(int dir)
  {
    if (dir == 1) return -1;
    if (dir == 3) return 1;
    return 0;
  }

  public static int dy(int dir)
  {
    if (dir == 0) return -1;
    if (dir == 2) return 1;
    return 0;
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
