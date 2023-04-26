package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * A projectile that travels to a particular target and deals damage to any Ship
 * it collides with on impact
 */
public class Cannonball extends MovingSprite {
    public static final int TRAVEL_DISTANCE = 300;
    private static final ImageIcon ICON = new ImageIcon("assets/images/cannonball.png");
    private static final int SPEED = 120;
    private static final int DAMAGE = 20;
    private Ship[] targets;
    private int startX;
    private int startY;
    private boolean firstTick = true;

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

        this.startX = startX;
        this.startY = startY;

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
        if (firstTick) {
            SoundHelper.getInstance().playSound("fire.wav");
            firstTick = false;
        }
        for (Ship ship : targets) {
            if (isColliding(ship)) {
                // We did, hit it
                ship.takeDamageAbsolute(DAMAGE);
                erase();
                return;
            }
        }
        boolean canMove = canMoveDown() && canMoveLeft() && canMoveRight() && canMoveUp();
        // See how far the cannonball has traveled
        if (Math.hypot(startX - x, startY - y) > TRAVEL_DISTANCE || !canMove) {
            // We've reached the target, see if we hit anything
            // Splash! Cannonball's gone
            erase();
            return;
        }
        // Move and stuff
        super.tick();
    }
}
