package final_project;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

/**
 * A pirate. Targets the player but tries to avoid direct collisions.
 */
public class PirateShip extends MovingSprite {
    // How close to the player is too close?
    private int turnDistance;

    /**
     * Create the pirate
     * 
     * @param gameJFrame   the window to add the pirate to
     * @param image        the image of the pirate
     * @param x            the starting X position of the pirate
     * @param y            the starting Y position of the pirate
     * @param speed        how fast it should move
     * @param turningSpeed how fast it should turn
     * @param turnDistance how close it should get to the player before turning
     *                     around to avoid a collision
     */
    public PirateShip(JFrame gameJFrame, ImageIcon image, int x, int y, double speed, double turningSpeed,
            int turnDistance) {
        // Create the super class
        super(gameJFrame, image, x, y, speed, turningSpeed);

        // Set the turn distance
        this.turnDistance = turnDistance;
    }

    @Override
    public void setTarget(int destinationX, int destinationY) {
        // Calculate difference in X and Y
        double xDiff = this.getX() - destinationX;
        double yDiff = this.getY() - destinationY;

        // If too close, aim away
        // If too far, aim towards
        int attackOrAvoid = tooClose(xDiff, yDiff);
        double desiredAngle = Math.toDegrees(Math.atan2(attackOrAvoid * yDiff, attackOrAvoid * xDiff));

        // Restrict angle to 0 < angle < 360
        desiredAngle = (desiredAngle + 360) % 360;

        // Find the difference between current and target angle
        double angleDiff = desiredAngle - this.getRotation();

        // Force it to be positive
        if (angleDiff < 0)
            angleDiff += 360;

        // Find shorter rotation direction
        if (angleDiff > 180) {
            this.rotationDirection = MovingSprite.Direction.COUNTER_CLOCKWISE;
        } else {
            this.rotationDirection = MovingSprite.Direction.CLOCKWISE;
        }
    }

    /**
     * Calcuate whether or not the pirate is too close to the player.
     * 
     * @param xDiff the X distance from the player
     * @param yDiff the Y distance from the player
     * @return 1 if too close, -1 otherwise
     */
    private int tooClose(double xDiff, double yDiff) {
        return (Math.hypot(xDiff, yDiff) > turnDistance) ? 1 : -1;
    }
}