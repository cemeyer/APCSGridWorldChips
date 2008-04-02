package gridchallenge;

import info.gridworld.world.World;
import info.gridworld.grid.Location;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Grid;
import info.gridworld.actor.Actor;
import info.gridworld.gui.WorldFrame;

import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class CCWorld extends World<Actor>
{
  private CCPlayer player;

  private Dir action = Dir.NONE;
  private Dir lastAction = Dir.NONE;
  private CCLevel level;
  private int tickCounter = 0;

  private boolean gotoNextLevel = false;
  public boolean godMode = false;
  private int levelTarget = 0;

  public CCLevel getLevel()
  {
    return level;
  }

  public enum Dir
  {
    UP, LEFT, DOWN, RIGHT, NONE;

    public int toDir()
    {
      switch (this)
      {
        case UP: return 0;
        case DOWN: return 2;
        case LEFT: return 1;
        case RIGHT: return 3;
      }
      return 0;
    }
  }

  public Point getChip()
  {
    if (player == null) return null;
    return player.chip;
  }

  public void loadLevel(CCLevel level)
  {
    this.level = level;

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

  public CCWorld(CCLevel level)
  {
    super(new BoundedGrid<Actor>(9, 9));

    Grid<Actor> grid = getGrid();

    for (int i = 0; i < 9; i++)
      for (int j = 0; j < 9; j++)
        (new RenderTile(this)).putSelfInGrid(grid, new Location(i, j));

    this.frame = new WorldFrame<Actor>(this);
    this.frame.setVisible(true);
//  ((WorldFrame)this.frame).display.cellSize =
//    (((WorldFrame)this.frame).display.cellSize * 100) / 91;
    ((WorldFrame)this.frame).control.run();
    ((WorldFrame)this.frame).control.timer.setDelay(20);

    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    loadLevel(level);
  }

  public Dir opposite(Dir in)
  {
    if (in == Dir.LEFT) return Dir.RIGHT;
    if (in == Dir.RIGHT) return Dir.LEFT;
    if (in == Dir.UP) return Dir.DOWN;
    return Dir.UP;
  }

  public void step()
  {
    final int cmf = 5;
    Grid<Actor> grid = getGrid();
    for (Location loc : grid.getOccupiedLocations())
    {
      Actor a = grid.get(loc);
      a.act();  // paint yourselves.
    }
    switch (level.getObjectAt(player.chip.x, player.chip.y, 1))
    {
      case 0x0c:
        // ice
        if (!player.iceSkates)
        {
          if (tickCounter % cmf == 0)
          {
            action = lastAction;
            if (level.whatsAt(player.chip.add(action), 0) == 0x01)
              action = opposite(action);
          }
          else action = Dir.NONE;
        }
        break;
      case 0x1a:
        // south east ice
        if (!player.iceSkates)
        {
          if (tickCounter % cmf == 0)
          {
            if (lastAction == Dir.UP) action = Dir.RIGHT;
            else action = Dir.DOWN;
          }
          else action = Dir.NONE;
        }
        break;
      case 0x1b:
        // south west ice
        if (!player.iceSkates)
        {
          if (tickCounter % cmf == 0)
          {
            if (lastAction == Dir.UP) action = Dir.LEFT;
            else action = Dir.DOWN;
          }
          else action = Dir.NONE;
        }
        break;
      case 0x1c:
        // north west ice
        if (!player.iceSkates)
        {
          if (tickCounter % cmf == 0)
          {
            if (lastAction == Dir.DOWN) action = Dir.LEFT;
            else action = Dir.UP;
          }
          else action = Dir.NONE;
        }
        break;
      case 0x1d:
        // north east ice
        if (!player.iceSkates)
        {
          if (tickCounter % cmf == 0)
          {
            if (lastAction == Dir.DOWN) action = Dir.RIGHT;
            else action = Dir.UP;
          }
          else action = Dir.NONE;
        }
        break;
      case 0x0d:
        // force floor south
        if (!player.suctionBoots)
        {
          if (tickCounter % cmf == 0)
            action = Dir.DOWN;
          else if (action == Dir.UP)
            action = Dir.NONE;
        }
        break;
      case 0x12:
        // force floor north
        if (!player.suctionBoots)
        {
          if (tickCounter % cmf == 0)
            action = Dir.UP;
          else if (action == Dir.DOWN)
            action = Dir.NONE;
        }
        break;
      case 0x13:
        // force floor east
        if (!player.suctionBoots)
        {
          if (tickCounter % cmf == 0)
            action = Dir.RIGHT;
          else if (action == Dir.LEFT)
            action = Dir.NONE;
        }
        break;
      case 0x14:
        // force floor west
        if (!player.suctionBoots)
        {
          if (tickCounter % cmf == 0)
            action = Dir.LEFT;
          else if (action == Dir.RIGHT)
            action = Dir.NONE;
        }
        break;
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
    if (action != Dir.NONE)
    {
      lastAction = action;
      action = Dir.NONE;
    }

    tickCounter++;
    if (tickCounter % 15 == 0)
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
    else if (desc.equals("ctrl R"))
    {
      level.killChip("Player Requested", player);
    }
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
        loadLevel(GridChallengeRunner.c.getLevel(levelTarget));
      }
      levelTarget = 0;
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
