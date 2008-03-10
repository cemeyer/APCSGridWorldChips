package gridchallenge;

import info.gridworld.actor.ActorWorld;

import java.io.IOException;

public class GridChallengeRunner
{
  public static void main(String[] args){
    try
    {
      CCLevelSet c = new CCLevelSet(System.getProperty("user.dir") +
          "/gridchallenge/CCLP2/CCLP2.dat");

      CCWorld world = new CCWorld(c.getLevel(1));

      world.show();
    }
    catch (IOException iox) {}
  }
}
