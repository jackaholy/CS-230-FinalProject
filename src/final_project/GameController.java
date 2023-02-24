package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Container;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;

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
                // Get the position of the cursor
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                // Calcuate the width of the cursor
                double xDiff = player.getX() - mouseLocation.getX();
                // Calculate the height of the triangle
                double yDiff = player.getY() - mouseLocation.getY();

                // Calculate the angle between the player and the cursor
                double desiredAngle = Math.toDegrees(Math.atan2(yDiff, xDiff)) + 90;
                player.setRotation(desiredAngle);
            }
            // About 30 fps
        }, 0, 33);

    }

}