package gridchallenge;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class CCLevelSet
{
  BufferedReader br;
  int levels;

  public CCLevelSet(String filename)
  {
    try { br = new BufferedReader(new FileReader(filename)); }
    catch (FileNotFoundException fnfx)
    {
      throw new RuntimeException("Not a file you tard");
    }

    readHeader();
  }

  private int readInt8() throws IOException
  {
    return br.read();
  }

  private int readInt16() throws IOException
  {
    int lsb = br.read();
    int msb = br.read();

    return msb * 256 + lsb;
  }

  private int readHeader()
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
