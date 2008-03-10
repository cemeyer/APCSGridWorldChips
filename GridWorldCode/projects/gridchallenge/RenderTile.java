package gridchallenge;

import info.gridworld.actor.Actor;

public class RenderTile extends Actor
{
  private CCWorld world;

  public CCWorld getWorld()
  {
    return world;
  }

  public RenderTile(CCWorld world)
  {
    super();
    this.world = world;
  }

  public void act()
  {
  }
}
