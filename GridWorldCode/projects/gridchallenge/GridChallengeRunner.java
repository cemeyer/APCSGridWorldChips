package gridchallenge;

import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;

public class GridChallengeRunner
{
  public static void main(String[] args){
    ActorWorld world = new ActorWorld();
    for (int i = world.getGrid().getNumRows() - 1; i >= 0; i--)
      for (int j = world.getGrid().getNumCols() - 1; j >= 0; j--)
        world.add(new Location(i, j), new RenderTile());
    world.show();
  }
}
