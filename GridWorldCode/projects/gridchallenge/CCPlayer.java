package gridchallenge;

public class CCPlayer
{
  public Point chip;
  public CCWorld world;
  public int numChips, numRed, numGreen, numYellow, numBlue;

  public CCPlayer(Point chip, CCWorld world)
  {
    this.chip = chip;
    this.world = world;
    numChips = numRed = numGreen = numYellow = numBlue = 0;
  }
}
