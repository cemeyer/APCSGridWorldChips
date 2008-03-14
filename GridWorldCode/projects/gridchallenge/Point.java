package gridchallenge;

public class Point
{
  public int x, y;
  public Point(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public Point add(int dir)
  {
    return new Point(x + CCLevel.dx(dir), y + CCLevel.dy(dir));
  }
}
