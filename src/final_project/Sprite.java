package final_project;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.Point;

/**
 * A "thing" in the game. Should handle some basic stuff. Show/hide, movement,
 * collisions, etc.
 */
public abstract class Sprite {
    // The JFrame this Sprite is part of
    protected JFrame gameJFrame;
    // The J "thing" that's draw onto the screen
    protected final JLabel spriteJLabel = new JLabel();

    // Current position
    protected int x;
    protected int y;

    protected boolean exists = true;

    // Use this constant to scale the hitboxes up or down
    // If too sensitive, decrease. If not sensitive enough, increase.
    // Should be about 1.
    private static final double COLLISION_SCALE_FACTOR = 0.8;

    // The original icon
    private Icon unrotatedIcon;
    private double rotationDegrees = 0;
    private RotatedIcon rotatedIcon;

    /**
     * Constructor for the sprite
     * 
     * @param gameJFrame the frame to add the sprite to
     * @param image      an Icon for the sprite
     * @param x          the starting Y
     * @param y          the starting X
     */
    protected Sprite(JFrame gameJFrame, Icon image, int x, int y) {
        this.gameJFrame = gameJFrame;
        // Add sprite to play area
        gameJFrame.getContentPane().add(spriteJLabel);

        // Assign instance variables
        this.unrotatedIcon = image;
        this.rotatedIcon = new RotatedIcon(unrotatedIcon);
        this.x = x;
        this.y = y;
    }

    /**
     * Display current position and rotation to the screen
     */
    protected void draw() {
        if (!exists)
            return;
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
     * Remove the sprite from the game
     */
    protected void erase() {
        spriteJLabel.setVisible(false);
        exists = false;
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
     * Set the icon to a new one
     * 
     * @param icon
     */
    protected void setIcon(Icon icon) {
        unrotatedIcon = icon;
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
     * Check whether the sprite exists
     * 
     * @return
     */
    public boolean getExistance() {
        return exists;
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

    /**
     * Get the cordinates of this Sprites' corners (if it's represented as a rotated
     * rectangle)
     * 
     * ChatGPT was used for this function. Comments are my own.
     * 
     * @return
     */
    public Point[] getCorners() {
        int width = (int) (unrotatedIcon.getIconWidth() * COLLISION_SCALE_FACTOR);
        int height = (int) (unrotatedIcon.getIconHeight() * COLLISION_SCALE_FACTOR);
        return SpriteHelper.getRotatedRectangleCorners(x, y, width, height, rotationDegrees);
    }

    /**
     * Check whether any corners of one sprite are within the bounds of another.
     * 
     * @param other the sprite to check ours against
     * @return
     */
    protected boolean isColliding(Sprite other) {
        // If one of the sprites doesn't exist,
        // there's no collision
        if (!exists || !other.getExistance()) {
            return false;
        }
        // Get the corners of both sprites
        Point[] ourCorners = getCorners();
        Point[] theirCorners = other.getCorners();
        // Construct Polygons from the points
        return SpriteHelper.isColliding(ourCorners, theirCorners);
    }
}
