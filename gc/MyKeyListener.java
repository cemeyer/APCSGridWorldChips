package gc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener
{

    public void keyPressed(KeyEvent e)
    {
        System.out.println("pressed #" + e.getKeyCode());
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
        System.out.println("typed '" + e.getKeyChar() + '\'');
    }
    
}
