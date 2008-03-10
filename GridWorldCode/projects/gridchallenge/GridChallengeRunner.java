package gridchallenge;

import info.gridworld.actor.ActorWorld;

import java.io.IOException;

public class GridChallengeRunner
{
  private static CCLevelSet c;

  public static void main(String[] args){
    try
    {
      c = new CCLevelSet(System.getProperty("user.dir") +
          "/gridchallenge/CCLP2/CCLP2.dat");

      CCWorld world = new CCWorld(c.getLevel(1));

      world.show();
    }
    catch (IOException iox) {}
  }

  public static void startLevel(int level)
  {
    CCWorld world = new CCWorld(c.getLevel(level));
    world.show();
  }
}
