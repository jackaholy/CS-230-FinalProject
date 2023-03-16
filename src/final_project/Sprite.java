package final_project;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.Point;
import java.awt.Polygon;

/**
 * A "thing" in the game. Should handle some basic stuff. Show/hide, movement,
 * collisions, etc.
 */
public abstract class Sprite {
    // Use this constant to scale the hitboxes up or down
    // If too sensitive, decrease. If not sensitive enough, increase.
    // Should be about 1.
    static final double COLLISION_SCALE_FACTOR = 0.8;

    // The J "thing" that's draw onto the screen
    protected final JLabel spriteJLabel = new JLabel();
    // The original icon
    private ImageIcon unrotatedIcon;
    private RotatedIcon rotatedIcon;

    // The JFrame this Sprite is part of
    protected JFrame gameJFrame;

    // Current position
    private double rotationDegrees = 0;
    protected int x;
    protected int y;

    protected Sprite(JFrame gameJFrame, ImageIcon image, int x, int y) {
        this.gameJFrame = gameJFrame;
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

    /**
     * Get the cordinates of this Sprites' corners (if it's represented as a rotated
     * rectangle)\
     * 
     * ChatGPT was used for this function. Comments are my own.
     * 
     * @return
     */
    public Point[] getCorners() {
        // The distance from the center to the edge of the hitbox
        double halfWidth = ((double) unrotatedIcon.getIconWidth() / 2) * COLLISION_SCALE_FACTOR;
        double halfHeight = ((double) unrotatedIcon.getIconHeight() / 2) * COLLISION_SCALE_FACTOR;
        // 0 if horizontal, 1 if vertical
        double sin = Math.sin(Math.toRadians(rotationDegrees));
        // 1 if horizontal, 0 if vertical
        double cos = Math.cos(Math.toRadians(rotationDegrees));

        // Construct each of the points
        Point[] corners = new Point[4];
        corners[0] = new Point(
                (int) (this.x + halfWidth * cos + halfHeight * sin),
                (int) (this.y + halfWidth * sin - halfHeight * cos));
        corners[1] = new Point(
                (int) (this.x - halfWidth * cos + halfHeight * sin),
                (int) (this.y - halfWidth * sin - halfHeight * cos));
        corners[2] = new Point(
                (int) (this.x - halfWidth * cos - halfHeight * sin),
                (int) (this.y - halfWidth * sin + halfHeight * cos));
        corners[3] = new Point(
                (int) (this.x + halfWidth * cos - halfHeight * sin),
                (int) (this.y + halfWidth * sin + halfHeight * cos));
        return corners;
    }

    /**
     * Check whether any corners of one sprite are within the bounds of another.
     * 
     * @param other the sprite to check ours against
     * @return
     */
    boolean isColliding(Sprite other) {
        // Get the corners of both sprites
        Point[] ourCorners = getCorners();
        Point[] theirCorners = other.getCorners();
        // Construct Polygons from the points
        Polygon ourPoly = new Polygon();
        for (Point corner : ourCorners) {
            ourPoly.addPoint(corner.x, corner.y);
        }
        Polygon theirPoly = new Polygon();
        for (Point corner : theirCorners) {
            theirPoly.addPoint(corner.x, corner.y);
        }
        // Check if our polygon contains any of their corners
        for (Point corner : theirCorners) {
            if (ourPoly.contains(corner))
                return true;
        }
        // Check if their polygon contains any of our corners
        for (Point corner : ourCorners) {
            if (theirPoly.contains(corner))
                return true;
        }
        // If none of the corners are within the bounds, we're not colliding
        return false;
    }
}
