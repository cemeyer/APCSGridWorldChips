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

    int tiletop = world.getLevel().getObjectAt(chip.x + loc.getCol() - 4, chip.y + loc.getRow() - 4, 0);
    int tilebot = world.getLevel().getObjectAt(chip.x + loc.getCol() - 4, chip.y + loc.getRow() - 4, 1);
    int tilespc = 0;
    
    BufferedImage bit = Tile.getImageForTile(tiletop);
    BufferedImage bib = Tile.getImageForTile(tilebot);
    BufferedImage spc = Tile.getImageForTile(tilespc);
    
    // draw from bottom to top; use transparency!
    g2.drawImage(spc, rect.x, rect.y, rect.width, rect.height, null);
    g2.drawImage(bib, rect.x, rect.y, rect.width, rect.height, null);
    g2.drawImage(bit, rect.x, rect.y, rect.width, rect.height, null);
  }
}
