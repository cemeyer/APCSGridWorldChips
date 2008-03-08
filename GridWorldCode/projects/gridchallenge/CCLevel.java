package gridchallenge;

import java.util.LinkedList;

public class CCLevel
{
  class Point
  {
    public int x, y;
    public Point(int x, int y)
    {
      this.x = x;
      this.y = y;
    }
  }

  private CCLevel next = null;
  // timeLimit == 0 indicates no limit
  private int levelNumber, timeLimit, numChips;

  // x, y, layer
  private byte[][][] map;

  private byte[] leveldata;
  private int idx = 0;

  private String title, hint, password;

  private LinkedList<Point> movingMonsters = new LinkedList<Point>();

  public CCLevel(byte[] leveldata)
  {
    // setup map data
    map = new byte[32][][];
    for (int i = 0; i < 32; i++)
    {
      map[i] = new byte[32][];
      map[i][0] = new byte[2];
      map[i][1] = new byte[2];
    }

    this.leveldata = leveldata;

    parseLevelData();
  }

  private void parseLevelData()
  {
    levelNumber = readInt16();
    timeLimit   = readInt16();
    numChips    = readInt16();

    boolean breakFree = false;
    for (;;)
    {
      int field = readInt8();
      switch (field)
      {
        case 1:
          readInt8(); // get rid of second byte in fieldtype word
          int sizeFirstLayer = readInt16();
          int[] firstlayer = readInts8(sizeFirstLayer);

          int sizeSecondLayer = readInt16();
          int[] secondlayer = readInts8(sizeSecondLayer);

          copyLayerIntoMap(0, firstlayer);
          copyLayerIntoMap(1, secondlayer);

          int bytesRemaining = readInt16();
          break;
        case 3:
          int titleLen = readInt8();
          title = readString(titleLen);
          System.out.println(title);
          break;
        case 6:
          int passlen = readInt8(); // always 5...
          password = convertPassword(readString(passlen));
          System.out.println(password);
          break;
        case 7:
          int hintlen = readInt8();
          hint = readString(hintlen);
          System.out.println(hint);
          break;
        case 10:
          // monster motion
          for (;;)
          {
            if (idx >= leveldata.length)
            {
              breakFree = true;
              break;
            }
            int x = readInt8();
            int y = readInt8();
            movingMonsters.add(new Point(x, y));
          }
          break;
        default:
          throw new RuntimeException("We ought to handle field type " + field);
      }
      if (breakFree) break;
      if (idx >= leveldata.length) break;
    }
  }

  private void copyLayerIntoMap(int elevation, int[] rleData)
  {
  }

  private String readString(int len)
  {
    int[] nums = readInts8(len);
    char[] res = new char[len - 1];

    for (int i = 0; i < res.length; i++)
      res[i] = (char)nums[i];

    return new String(res);
  }

  private int[] readInts8(int count)
  {
    int[] res = new int[count];
    for (int i = 0; i < count; i++) res[i] = readInt8();
    return res;
  }

  private int readInt16()
  {
    int lsb = readInt8();
    int msb = readInt8();
    return msb * 256 + lsb;
  }

  private int readInt8()
  {
    int r = leveldata[idx] & 0xFF;
    idx++;
    return r;
  }

  private String convertPassword(String pass)
  {
    byte[] bs = pass.getBytes();
    for (int i = 0; i < bs.length; i++) bs[i] ^= 0xC3;
    return new String(bs);
  }

  public CCLevel getNext()
  {
    return next;
  }

  public void setNext(CCLevel next)
  {
    this.next = next;
  }
}
