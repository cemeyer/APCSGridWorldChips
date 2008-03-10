package gridchallenge;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;


public class Tile
{
  private static String[] tileObjects = new String[]
  {
    "Space",
    "Wall",
    "Chip",
    "Water",
    "Fire",
    "InvisWallInv",
    "BlockedNorth",
    "BlockedWest",
    "BlockedSouth",
    "BlockedEast",
    "MovableDirt",
    "Dirt",
    "Ice",
    "ForceSouth",
    "CloningNorth",
    "CloningWest",
    "CloningSouth",
    "CloningEast",
    "ForceNorth",
    "ForceEast",
    "ForceWest",
    "Exit",
    "BlueDoor",
    "RedDoor",
    "GreenDoor",
    "YellowDoor",
    "SouthEastIce",
    "SouthWestIce",
    "NorthWestIce",
    "NorthEastIce",
    "BlueBlockTile",
    "BlueBlockWall",
    null,
    "Thief",
    "Socket",
    "GreenButton",
    "RedButton",
    "SwitchBlockCl",
    "SwitchBlockOp",
    "BrownButton",
    "BlueButton",
    "Teleport",
    "Bomb",
    "Trap",
    "InvisWallVis",
    "Gravel",
    "PassOnce",
    "Hint",
    "BlockedSE",
    "CloneMachine",
    "ForceAllDir",
    "DrowningChip",
    "BurnedChip",
    "BurnedChip2",
    null,
    null,
    null,
    "ChipExit",
    "ExitEndGame",
    "ExitEndGame2",
    "ChipSwimmingN",
    "ChipSwimmingW",
    "ChipSwimmingS",
    "ChipSwimmingE",
    "BugN",
    "BugW",
    "BugS",
    "BugE",
    "FireBugN",
    "FireBugW",
    "FireBugS",
    "FireBugE",
    "PinkBallN",
    "PinkBallW",
    "PinkBallS",
    "PinkBallE",
    "TankN",
    "TankW",
    "TankS",
    "TankE",
    "GhostN",
    "GhostW",
    "GhostS",
    "GhostE",
    "FrogN",
    "FrogW",
    "FrogS",
    "FrogE",
    "DumbbellN",
    "DumbbellW",
    "DumbbellS",
    "DumbbellE",
    "BlobN",
    "BlobW",
    "BlobS",
    "BlobE",
    "CentipedeN",
    "CentipedeW",
    "CentipedeS",
    "CentipedeE",
    "BlueKey",
    "RedKey",
    "GreenKey",
    "YellowKey",
    "Flippers",
    "FireBoots",
    "IceSkates",
    "SuctionBoots",
    "ChipN",
    "ChipW",
    "ChipS",
    "ChipE",
  };

  public static String getNameForTile(int tile)
  {
    if (tile > 0x6f || tile < 0) return null;
    return tileObjects[tile];
  }

  public static BufferedImage getImageForTile(int tile)
  {
    BufferedImage res = null;
    String path = "/home/konrad/src/java/gridch/GridWorldCode/projects/gridchallenge/images/" +
      getNameForTile(tile) + ".png";

    try
    {
      res = ImageIO.read(new File(path));
    }
    catch (Exception x)
    {
      //System.out.println(x.getClass().getName() + ": " + x.getMessage());
    }
    return res;
  }
}
