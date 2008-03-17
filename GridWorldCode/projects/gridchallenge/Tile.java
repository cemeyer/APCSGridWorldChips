package gridchallenge;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.HashMap;


public class Tile
{
  private static String[] tileObjects = new String[]
  {
    "Space",        // 0x00
    "Wall",
    "Chip",
    "Water",
    "Fire",
    "InvisWallInv",
    "BlockedNorth",
    "BlockedWest",
    "BlockedSouth", // 0x08
    "BlockedEast",
    "MovableDirt",
    "Dirt",
    "Ice",
    "ForceSouth",
    "CloningNorth",
    "CloningWest",
    "CloningSouth", // 0x10
    "CloningEast",
    "ForceNorth",
    "ForceEast",
    "ForceWest",
    "Exit",
    "BlueDoor",
    "RedDoor",
    "GreenDoor",    // 0x18
    "YellowDoor",
    "SouthEastIce",
    "SouthWestIce",
    "NorthWestIce",
    "NorthEastIce",
    "BlueBlockTile",
    "BlueBlockWall",
    null,           // 0x20
    "Thief",
    "Socket",
    "GreenButton",
    "RedButton",
    "SwitchBlockCl",
    "SwitchBlockOp",
    "BrownButton",
    "BlueButton",   // 0x28
    "Teleport",
    "Bomb",
    "Trap",
    "InvisWallVis",
    "Gravel",
    "PassOnce",
    "Hint",
    "BlockedSE",    // 0x30
    "CloneMachine",
    "ForceAllDir",
    "DrowningChip",
    "BurnedChip",
    "BurnedChip2",
    null,
    null,
    null,           // 0x38
    "ChipExit",
    "ExitEndGame",
    "ExitEndGame2",
    "ChipSwimmingN",
    "ChipSwimmingW",
    "ChipSwimmingS",
    "ChipSwimmingE",
    "BugN",         // 0x40
    "BugW",
    "BugS",
    "BugE",
    "FireBugN",
    "FireBugW",
    "FireBugS",
    "FireBugE",
    "PinkBallN",    // 0x48
    "PinkBallW",
    "PinkBallS",
    "PinkBallE",
    "TankN",
    "TankW",
    "TankS",
    "TankE",
    "GhostN",       // 0x50
    "GhostW",
    "GhostS",
    "GhostE",
    "FrogN",
    "FrogW",
    "FrogS",
    "FrogE",
    "DumbbellN",    // 0x58
    "DumbbellW",
    "DumbbellS",
    "DumbbellE",
    "BlobN",
    "BlobW",
    "BlobS",
    "BlobE",
    "CentipedeN",   // 0x60
    "CentipedeW",
    "CentipedeS",
    "CentipedeE",
    "BlueKey",
    "RedKey",
    "GreenKey",
    "YellowKey",
    "Flippers",     // 0x68
    "FireBoots",
    "IceSkates",
    "SuctionBoots",
    "ChipN",
    "ChipW",
    "ChipS",
    "ChipE",        // 0x6f
  };
  private static HashMap<String, BufferedImage> images =
    new HashMap<String, BufferedImage>();

  public static String getNameForTile(int tile)
  {
    if (tile > 0x6f || tile < 0) return null;
    return tileObjects[tile];
  }

  public static BufferedImage getImageForTile(int tile)
  {
    BufferedImage res = null;
    String nameOfTile = getNameForTile(tile);

    try
    {
      if (images.containsKey(nameOfTile))
        return images.get(nameOfTile);
      if (File.separatorChar == '/')
      {
        String path = "/home/konrad/src/java/gridch/GridWorldCode/projects/gridchallenge/images/" +
          nameOfTile + ".png";
        res = ImageIO.read(new File(path));
      }
      else if (File.separatorChar == '\\')
      {
        String path = "Z:/GridWorldCode/projects/gridchallenge/images/" +
          nameOfTile + ".png";
        res = ImageIO.read(new File(path));
      }
      images.put(nameOfTile, res);
    }
    catch (Exception x)
    {
      //System.out.println(x.getClass().getName() + ": " + x.getMessage());
      System.out.println("Need: " + getNameForTile(tile));
      images.put(nameOfTile, null);
    }
    return res;
  }
}
