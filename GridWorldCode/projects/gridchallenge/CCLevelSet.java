package gridchallenge;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class CCLevelSet
{
  private FileInputStream file;
  private int levels;

  private CCLevel levelOne;

  public CCLevelSet(String filename) throws IOException
  {
    try { file = new FileInputStream(filename); }
    catch (FileNotFoundException fnfx)
    {
      throw new RuntimeException("Not a file you tard");
    }

    readHeader();

    CCLevel prev = null;
    for (int i = 0; i < levels; i++)
    {
      int offset = readInt16();
      // read offset bytes and pass to the CCLevel ctor.
  
      byte[] leveldata = readBytes(offset);
  
      CCLevel cur = new CCLevel(leveldata);
      if (prev == null) levelOne = cur;
      else prev.setNext(cur);
  
      prev = cur;
    }
  }

  private byte[] readBytes(int count) throws IOException
  {
    byte[] res = new byte[count];

    int sum = 0;
    int tmp;
    
    while ((tmp = file.read(res, sum, count - sum)) >= 0)
    {
      sum += tmp;
      if (sum == count) break;
    }
    // we get here from breaking or not having read enough data
    if (sum < count) throw new RuntimeException(
        "Ran out of file while reading level.");

    return res;
  }

  private String convertPassword(String pass)
  {
    byte[] bs = pass.getBytes();
    for (int i = 0; i < bs.length; i++) bs[i] ^= 0xC3;
    return new String(bs);
  }

  private int readInt8() throws IOException
  {
    return file.read();
  }

  private int readInt16() throws IOException
  {
    int lsb = file.read();
    int msb = file.read();

    return msb * 256 + lsb;
  }

  private void readHeader() throws IOException
  {
    // CC level data
    if (0xAAAC != readInt16()) throw new RuntimeException("Invalid file");
    // MS format
    if (0x0002 != readInt16()) throw new RuntimeException("Invalid file");
    // # of levels in this file
    levels = readInt16();
    if (levels == 0) throw new RuntimeException("No levels in file");
  }
}
