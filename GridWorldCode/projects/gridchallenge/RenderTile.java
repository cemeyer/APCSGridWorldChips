package gridchallenge;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import info.gridworld.actor.Actor;
import info.gridworld.gui.Display;
import info.gridworld.grid.Location;

public class RenderTile extends Actor implements Display
{
  private CCWorld world;

  public RenderTile(CCWorld world)
  {
    super();
    this.world = world;
  }

  public void act()
  {
  }

  public void draw(Object obj, Component c, Graphics2D g2, Rectangle rect)
  {
//  Actor a = (Actor)obj;
    Actor a = this;
    Location loc = a.getLocation();
    Point chip = world.getChip();
    CCLevel level = world.getLevel();

    int tile = world.getLevel().getObjectAt(chip.x + loc.getCol() - 5, chip.y + loc.getRow() - 5, 0);
    tile = 0;
    BufferedImage bi = Tile.getImageForTile(tile);
    if (bi == null) System.out.println("Image is null :(");
    g2.drawImage(bi, rect.x, rect.y, rect.width, rect.height, null);
  }
}
