package gridchallenge;

import info.gridworld.world.World;
import info.gridworld.grid.Location;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Grid;
import info.gridworld.actor.Actor;
import info.gridworld.gui.WorldFrame;

import java.util.LinkedList;
import javax.swing.JOptionPane;

public class CCWorld extends World<Actor>
{
  private CCPlayer player;

  private Dir action = Dir.NONE;
  private CCLevel level;
  private int tickCounter = 0;

  private boolean gotoNextLevel = false;
  public boolean godMode = false;
  private int levelTarget = 0;

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
    if (player == null) return null;
    return player.chip;
  }

  public CCWorld(CCLevel level)
  {
    super(new BoundedGrid<Actor>(9, 9));

    Grid<Actor> grid = getGrid();

    for (int i = 0; i < 9; i++)
      for (int j = 0; j < 9; j++)
        (new RenderTile(this)).putSelfInGrid(grid, new Location(i, j));

    this.level = level;
    this.frame = new WorldFrame<Actor>(this);
    this.frame.setVisible(true);
    ((WorldFrame)this.frame).display.cellSize =
      (((WorldFrame)this.frame).display.cellSize * 10) / 9;
    ((WorldFrame)this.frame).control.run();
    ((WorldFrame)this.frame).control.timer.setDelay(20);

    //this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    boolean breakFree = false;
    for (int i = 0; i < 32; i++)
    {
      for (int j = 0; j < 32; j++)
      {
        String tile = Tile.getNameForTile(level.getObjectAt(i, j, 0));
        if (tile.length() == 5 && tile.startsWith("Chip"))
        {
          player = new CCPlayer(new Point(i, j), this);
          breakFree = true;
          break;
        }
      }
      if (breakFree) break;
    }
  }

  public void step()
  {
    Grid<Actor> grid = getGrid();
    for (Location loc : grid.getOccupiedLocations())
    {
      Actor a = grid.get(loc);
      a.act();  // paint yourselves.
    }
    switch(action)
    {
      case UP:
        if (player.chip.y > 0)
          level.moveChip(player, new Point(player.chip.x, player.chip.y - 1));
        break;
      case RIGHT:
        if (player.chip.x < 31)
          level.moveChip(player, new Point(player.chip.x + 1, player.chip.y));
        break;
      case LEFT:
        if (player.chip.x > 0)
          level.moveChip(player, new Point(player.chip.x - 1, player.chip.y));
        break;
      case DOWN:
        if (player.chip.y < 31)
          level.moveChip(player, new Point(player.chip.x, player.chip.y + 1));
        break;
      case NONE:
        // do nothing
        break;
    }
    action = Dir.NONE;

    tickCounter++;
    if (tickCounter >= 15)
    {
      LinkedList<Point> monsters = level.getMonsterLocations();
      LinkedList<Point> newmonsters = new LinkedList<Point>();
      for (Point m : monsters)
      {
        int realmonster = level.getObjectAt(m.x, m.y, 0);
        int genericMonster = realmonster & 0xfc;

        Point n = level.moveMonster(m, realmonster);
        if (n != null) newmonsters.add(n);
      }
      level.setMonsterLocations(newmonsters);
      tickCounter = 0;
    }

    super.step();
  }

  public String getMessage()
  {
    try
    {
      String res = "Level " + level.getLevelNumber() + ": "
        + level.getTitle();
      if (level.getHint() != null) res += " -- Hint: " + level.getHint();
      res += "\n" + player.numChips + "/" + level.getChipCount() +
        " chips obtained. You have the following keys: ";
      for (int i = 0; i < player.numGreen; i++) res += "G";
      for (int i = 0; i < player.numBlue; i++) res += "B";
      for (int i = 0; i < player.numRed; i++) res += "R";
      for (int i = 0; i < player.numYellow; i++) res += "Y";
      return res;
    }
    catch (Exception x) {}
    return null;
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
    else if (desc.equals("ctrl alt G"))
    {
      gotoNextLevel = true;
      levelTarget = 0;
    }
    else if (desc.equals("ctrl alt CLOSE_BRACKET"))
    {
      godMode = true;
    }
    else if (desc.matches("^\\d$") && gotoNextLevel)
      levelTarget = (levelTarget * 10) + Integer.valueOf(desc);
    else if (desc.equals("ENTER") && gotoNextLevel)
    {
      if (levelTarget > 0 && levelTarget <= 150)
      {
        GridChallengeRunner.startLevel(levelTarget);
        this.frame.dispose();
      }
      else levelTarget = 0;
    }
    else if (desc.equals("ctrl C"))
    {
      System.exit(0);
    }
    else
    {
      System.out.println(desc);
      return false;
    }
    return true;
  }
}
