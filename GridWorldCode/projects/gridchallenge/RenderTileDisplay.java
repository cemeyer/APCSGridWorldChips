package gridchallenge;

import info.gridworld.gui.AbstractDisplay;
import info.gridworld.grid.Location;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class RenderTileDisplay extends AbstractDisplay
{
  public void draw(Object obj, Component c, Graphics2D g2)
  {
  }

  public void draw(Object obj, Component c, Graphics2D g2, Rectangle rect)
  {
    RenderTile r = (RenderTile)obj;
    CCWorld world = r.getWorld();
    Location loc = r.getLocation();
    Point chip = world.getChip();
    CCLevel level = world.getLevel();

    int tile = world.getLevel().getObjectAt(chip.x + loc.getCol() - 4, chip.y + loc.getRow() - 4, 0);
    BufferedImage bi = Tile.getImageForTile(tile);
    g2.drawImage(bi, rect.x, rect.y, rect.width, rect.height, null);
  }
}
