package gridchallenge;

import info.gridworld.actor.ActorWorld;

import java.io.IOException;
import javax.swing.JOptionPane;

public class GridChallengeRunner
{
  public static CCLevelSet c;

  public static void main(String[] args){
    try
    {
      String choice = "/CCRL/ccrev.dat";
      Object[] options = {"CCLP2", "CC Revised"};
      if (JOptionPane.showOptionDialog(null,
            "What levelset do you want to play?",
            "Levelset chooser",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]) == 0)
      {
        choice = "/CCLP2/CCLP2.dat";
      }

      try
      {
        c = new CCLevelSet(System.getProperty("user.dir") +
            "/gridchallenge" + choice);
      }
      catch (RuntimeException rx)
      {
        c = new CCLevelSet("Z:\\GridWorldCode\\projects\\gridchallenge" +
            choice.replace('/', '\\'));
      }

      CCWorld world = new CCWorld(c.getLevel(1));

      world.show();
    }
    catch (IOException iox) {}
  }
}
