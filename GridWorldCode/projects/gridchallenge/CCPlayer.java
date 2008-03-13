package gridchallenge;

public class CCPlayer
{
  public Point chip;
  public int numChips, numRed, numGreen, numYellow, numBlue;

  public CCPlayer(Point chip)
  {
    this.chip = chip;
    numChips = numRed = numGreen = numYellow = numBlue = 0;
  }
}
