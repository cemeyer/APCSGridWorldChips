package gridchallenge;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import info.gridworld.actor.Actor;
import info.gridworld.gui.Display;

public class RenderTile extends Actor implements Display
{
  // this is the same for all rendertiles...
  static public int offsetX, offsetY;
  
  private Image getImage()
  {
    return null;
  }
  
  public void draw(Object obj, Component c, Graphics2D g2, Rectangle rect)
  {
    g2.drawImage(getImage(), rect.x, rect.y, rect.width, rect.height, null);
  }
}
