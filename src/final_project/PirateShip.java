package final_project;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

/**
 * A pirate. Targets the player but tries to avoid direct collisions.
 */
public class PirateShip extends Ship {
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
    protected void tick() {
        super.tick();
        Direction directionToRotate = calculateDirectionToDesiredAngle();

        if (shouldAvoidPlayer()) {
            directionToRotate = oppositeDirection(directionToRotate);
        }

        setRotationDirection(directionToRotate);
        rotate();
        moveForward();
    }

    /**
     * Calcuate whether or not the pirate is too close to the player.
     * 
     * @param xDiff the X distance from the player
     * @param yDiff the Y distance from the player
     * @return true if too close
     */
    private boolean shouldAvoidPlayer() {
        return Math.hypot(getX() - targetX, getY() - targetY) < turnDistance;
    }
}