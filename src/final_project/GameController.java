package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Container;
import java.awt.Color;

/**
 * The main file of the project. Run this one to start the project
 */
public class GameController {
    private final Timer tickTimer = new Timer();

    public static void main(String[] args) {
        new GameController();
    }

    public GameController() {
        // The window itself
        JFrame gameJFrame = new JFrame();
        // With arbitrary default dimensions
        gameJFrame.setSize(800, 600);

        // The play area
        Container gameContentPane = gameJFrame.getContentPane();
        gameContentPane.setBackground(Color.blue);
        // Use absolute positioning
        gameContentPane.setLayout(null);

        // Create a player
        Player player = new Player(
                gameJFrame,
                new ImageIcon("assets/water_bug.png"),
                100, 100, 0, 0, 0, 0);

        // Show the window and player
        gameJFrame.setVisible(true);
        player.draw();

        tickTimer.schedule(new TimerTask() {

            // A single tick of the game
            @Override
            public void run() {
                // Rotate one degree
                player.setRotation(player.getRotation() + 1);
            }
            // About 30 fps
        }, 0, 33);

    }

}