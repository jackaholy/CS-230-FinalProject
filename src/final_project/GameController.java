package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.Container;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * The main file of the project. Run this one to start the project
 */
public class GameController {
    private final Timer tickTimer = new Timer();
    private int cursorX;
    private int cursorY;

    public static void main(String[] args) {
        new GameController();
    }

    public GameController() {
        // The window itself
        JFrame gameJFrame = new JFrame();
        // With arbitrary default dimensions
        gameJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                300, 300, 0, 3, 5, 0);

        // Show the window and player
        gameJFrame.setVisible(true);
        player.draw();

        tickTimer.schedule(new TimerTask() {

            // A single tick of the game
            @Override
            public void run() {
                // Calcuate the width of the cursor
                double xDiff = player.getX() - cursorX;
                // Calculate the height of the triangle
                double yDiff = player.getY() - cursorY;

                // Calculate the angle between the player and the cursor
                double desiredAngle = Math.toDegrees(Math.atan2(yDiff, xDiff));
                if (desiredAngle > player.getRotation()) {
                    player.setRotationDirection(Player.Direction.CLOCKWISE);
                } else if (desiredAngle < player.getRotation()) {
                    player.setRotationDirection(Player.Direction.COUNTER_CLOCKWISE);
                }

                player.tick();
            }
            // About 30 fps
        }, 0, 30);
        gameContentPane.addMouseMotionListener(new MouseInputListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cursorX = e.getX();
                cursorY = e.getY();

            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

}