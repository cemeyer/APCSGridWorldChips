package gridchallenge;

public class CCLevel
{
  private CCLevel next = null;
  // timeLimit == 0 indicates no limit
  private int levelNumber, timeLimit, numChips;

  // x, y, layer
  private byte[][][] map;

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

    for (int i = 0; i < leveldata.length; i++)
    {
      // parse that shit
    }
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
