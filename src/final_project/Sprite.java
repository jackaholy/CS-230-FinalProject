package final_project;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JFrame;

/**
 * A "thing" in the game. Should handle some basic stuff. Show/hide, movement,
 * collisions, etc.
 */
abstract public class Sprite {
    // The J "thing" that's draw onto the screen
    private final JLabel spriteJLabel = new JLabel();
    // The original icon
    private ImageIcon unrotatedIcon;
    private RotatedIcon rotatedIcon;

    // Current position
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

    /**
     * Display current position and rotation to the screen
     */
    protected void draw() {
        // Create a new rotated icon based on the current rotation
        rotatedIcon = new RotatedIcon(unrotatedIcon, rotationDegrees);

        // Set the sprite to use the rotated icon and it's dimensions
        // Java uses the top left corner for coordinates, but we want to rotate about
        // the center, so we have to offset it by half it's width & height
        spriteJLabel.setBounds(x - rotatedIcon.getIconWidth() / 2,
                y - rotatedIcon.getIconHeight() / 2, rotatedIcon.getIconWidth(), rotatedIcon.getIconHeight());
        spriteJLabel.setIcon(rotatedIcon);

        // Show the sprite
        spriteJLabel.setVisible(true);
    }

    /**
     * Set rotation
     * 
     * @param rotationDegrees degrees from 0 (right)
     */
    public void setRotation(double rotationDegrees) {

        // Restrict angle to 0 < angle < 360
        this.rotationDegrees = (rotationDegrees + 360) % 360;
        draw();
    }

    /**
     * Get current rotation direction
     * 
     * @return Degrees from 0 (right)
     */
    public double getRotation() {
        return rotationDegrees;
    }

    /**
     * Get the currently set X position
     * 
     * @return current X position
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the X position of the player. Will be shown next time draw() is
     * called.
     * 
     * @param y desired x position.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the player's currently set Y position
     * 
     * @return current Y position
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the Y position of the player. Will be shown next time draw() is
     * called.
     * 
     * @param y desired y position.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the width of the sprite
     * 
     * @return the width of the sprite
     */
    public int getWidth() {
        return rotatedIcon.getIconWidth();
    }

    /**
     * Get the height of the sprite
     * 
     * @return the height of the sprite
     */
    public int getHeight() {
        return rotatedIcon.getIconHeight();
    }

}