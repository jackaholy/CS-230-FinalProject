package final_project;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * A projectile that travels to a particular target and deals damage to any Ship
 * it collides with on impact
 */
public class Cannonball extends MovingSprite {
    public static final ImageIcon ICON = new ImageIcon("assets/cannonball.png");
    private static final int SPEED = 120;
    private Ship[] targets;

    /**
     * Create a cannonball
     * 
     * @param gameJFrame  the jframe to add the sprite to
     * @param startX      the X position to begin at
     * @param startY      the Y position to begin at
     * @param targetX     the X position to fly to
     * @param targetY     the Y position to fly to
     * @param targetShips Things to collide with
     */
    public Cannonball(JFrame gameJFrame, int startX, int startY, int targetX, int targetY, Ship[] targetShips) {
        // Create a moving sprite
        super(gameJFrame, ICON, startX, startY, SPEED, 0);

        // Save Ships to collide with
        this.targets = targetShips;

        // Set the target and rotate towards it
        setTarget(targetX, targetY);
        setRotation(calculateAngleToCoordinates(targetX, targetY));

        // Don't make heatseeking cannonballs
        setRotationDirection(Direction.NOT_ROTATING);
    }

    @Override
    public void tick() {
        // The angle to the target now
        int expectedAngle = (int) calculateAngleToCoordinates(targetX, targetY);

        // The angle to the target on creation
        int actualAngle = (int) getRotation();

        // The difference between the angles
        // Once we pass the target, the difference should be ~180
        int difference = Math.abs(expectedAngle - actualAngle);

        // Arbitrary difference tolerance. Too low, cannonball disappears early
        // Too high, cannonball never disappears
        if (difference > 90) {
            // We've reached the target, see if we hit anything
            for (Ship ship : targets) {
                if (isColliding(ship)) {
                    // We did, hit it
                    ship.takeDamageAbsolute(20);
                }
            }
            // Splash! Cannonball's gone
            erase();
            return;
        }
        // Move and stuff
        super.tick();
    }
}
