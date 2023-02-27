package final_project;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JFrame;

/**
 * A "thing" in the game. Should handle some basic stuff. Show/hide, movement,
 * collisions, etc.
 */
abstract public class Sprite {
    private final JLabel spriteJLabel = new JLabel();
    private ImageIcon unrotatedIcon;
    private double rotationDegrees = 0;
    protected int x;
    protected int y;

    protected Sprite(JFrame gameJFrame, ImageIcon image, int x, int y) {
        // Add sprite to play area
        gameJFrame.getContentPane().add(spriteJLabel);

        // Assign instance variables
        this.unrotatedIcon = image;
        this.x = x;
        this.y = y;
    }

    protected void draw() {
        // Create a new rotated icon based on the current rotation
        RotatedIcon rotatedIcon = new RotatedIcon(unrotatedIcon, rotationDegrees);

        // Set the sprite to use the rotated icon and it's dimensions
        // Java uses the top left corner for coordinates, but we want to rotate about
        // the center, so we have to offset it by half it's width & height
        spriteJLabel.setBounds(x - rotatedIcon.getIconWidth() / 2,
                y - rotatedIcon.getIconHeight() / 2, rotatedIcon.getIconWidth(), rotatedIcon.getIconHeight());
        spriteJLabel.setIcon(rotatedIcon);

        // Show the sprite
        spriteJLabel.setVisible(true);
    }

    public void setRotation(double rotationDegrees) {

        // Restrict angle to 0 < angle < 360
        this.rotationDegrees = (rotationDegrees + 360) % 360;
        draw();
    }

    public double getRotation() {
        return rotationDegrees;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}