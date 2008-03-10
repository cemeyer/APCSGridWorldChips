package gridchallenge;

import info.gridworld.world.World;
import info.gridworld.grid.Location;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Grid;
import info.gridworld.actor.Actor;

public class CCWorld extends World<Actor>
{
  private Point chip = new Point(15, 15);
  private Dir action = Dir.NONE;
  private CCLevel level;

  public CCLevel getLevel()
  {
    return level;
  }

  private enum Dir
  {
    UP, LEFT, DOWN, RIGHT, NONE;
  }

  public Point getChip()
  {
    return chip;
  }

  public CCWorld(CCLevel level)
  {
    super(new BoundedGrid<Actor>(9, 9));

    Grid<Actor> grid = getGrid();

    for (int i = 0; i < 9; i++)
      for (int j = 0; j < 9; j++)
        (new RenderTile(this)).putSelfInGrid(grid, new Location(i, j));

    this.level = level;
  }

  public void step()
  {
    Grid<Actor> grid = getGrid();
    for (Location loc : grid.getOccupiedLocations())
    {
      Actor a = grid.get(loc);
      a.act();
    }
    switch(action)
    {
      case UP:
        if (chip.y > 0)
          chip = new Point(chip.x, chip.y - 1);
        break;
      case RIGHT:
        if (chip.x < 31)
          chip = new Point(chip.x + 1, chip.y);
        break;
      case LEFT:
        if (chip.x > 0)
          chip = new Point(chip.x - 1, chip.y);
        break;
      case DOWN:
        if (chip.y < 31)
          chip = new Point(chip.x, chip.y + 1);
        break;
      case NONE:
        // do nothing
        break;
    }
    if (action != Dir.NONE)
      System.out.println("Chip is at (" + chip.x + ", " + chip.y + ")");
    action = Dir.NONE;
    // TODO: move all monsters that need to move...
    super.step();
  }

  public boolean keyPressed(String desc, Location loc)
  {
    // Return true if we consume the keypress, false if GUI should
    if (desc.equals("LEFT"))
      action = Dir.LEFT;
    else if (desc.equals("RIGHT"))
      action = Dir.RIGHT;
    else if (desc.equals("UP"))
      action = Dir.UP;
    else if (desc.equals("DOWN"))
      action = Dir.DOWN;
    else
    {
      System.out.println(desc);
      return false;
    }
    return true;
  }
}
