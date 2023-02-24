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
        spriteJLabel.setBounds(x, y, rotatedIcon.getIconWidth(), rotatedIcon.getIconHeight());
        spriteJLabel.setIcon(rotatedIcon);

        // Show the sprite
        spriteJLabel.setVisible(true);
    }

    public void setRotation(double rotationDegrees) {
        // Set the rotation and update the ui
        this.rotationDegrees = rotationDegrees;
        draw();
    }

    public double getRotation() {
        return rotationDegrees;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}