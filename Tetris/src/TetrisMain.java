// Necessary imports
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class TetrisMain extends Applet implements KeyListener {

    // Instance variables
    private TetrisGame tG;                  // running tetris game
    private final static int DELAY = 300;   // timer's delay
    private Timer t;                        // timer that runs the game

    public void init(){
        //load the game
        tG = new TetrisGame();
        //paint(this.getGraphics());
        //other stuff
        setBackground(Color.BLACK);
        addKeyListener(this);
        go();
    }

    // Method that starts the timer and runs the game
    public void go(){
        t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                if(tG.isGameOver()){
                    this.cancel();
                    t.cancel();
                }
                tG.next();
                repaint();
            }
        }, 0, DELAY);
    }

    // Method that paints the playing Tetris board
    public void paint (Graphics g) {
        Color[][] temp = tG.getBoard();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,400,500);
        for(int r = 0; r < temp.length; r++)
            for(int c = 0; c < temp[0].length; c++)
                if(!temp[r][c].equals(Color.BLACK)) {
                    g.setColor(temp[r][c]);
                    g.fillRect(c * 25, r * 25, 25, 25);
                }
        if(tG.isGameOver()){
            g.setColor(Color.CYAN);
            g.fillRect(100,200,200,150);
            g.setColor(Color.WHITE);
            g.drawRect(100,200,200,150);
            Font f = new Font("Courier New",Font.BOLD,35);
            g.setFont(f);
            g.drawString("Game Over", 106, 250);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Courier New",Font.BOLD,13));
            g.drawString("Press ENTER to try again", 104,300);
        }
    }

    // Method that interprets the player's key movements
    public void keyPressed(KeyEvent e){
        if(tG.isGameOver()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                tG = null;
                tG = new TetrisGame();
                go();
            }
        } else {
            tG.tryMove(e);
            repaint();
        }

    }

    // Necessary methods for the KeyListener interface
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}

}