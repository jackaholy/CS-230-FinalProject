package final_project;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * A MovingSprite capable of firing cannons and taking damage.
 */
public class Ship extends MovingSprite {
    private double health;
    private final double startingHealth;

    // Cannonballs we've fired
    private List<Cannonball> cannonballs = new ArrayList<>();

    // Variables to store information for cannonball creation, see comment in
    // createCannonball
    private boolean shouldCreateCannonballNextFrame = false;
    private int cannonTargetX;
    private int cannonTargetY;
    private Ship[] cannonTargets;

    /**
     * Create a Ship
     * 
     * @param gameJFrame   jframe to add Ship to
     * @param image        image of the Ship
     * @param x            starting X position
     * @param y            starting Y position
     * @param speed        how many pixels to move per second
     * @param turningSpeed how many degrees to rotate per second
     * @param health       how much health the ship has, arbitarary units
     */
    public Ship(JFrame gameJFrame, ImageIcon image, int x, int y, double speed, double turningSpeed, int health) {
        super(gameJFrame, image, x, y, speed, turningSpeed);
        this.health = health;
        this.startingHealth = health;
    }

    @Override
    protected void tick() {
        // Tell all of our cannonballs to update
        for (Cannonball cannonBall : cannonballs) {
            cannonBall.tick();
        }

        // Don't do anything else if we're dead
        if (!exists)
            return;

        super.tick();

        // If a cannonball has been requested for this game
        if (shouldCreateCannonballNextFrame) {
            // Create one
            cannonballs.add(new Cannonball(gameJFrame, x, y, cannonTargetX, cannonTargetY, cannonTargets));
            // Mark it as finished
            shouldCreateCannonballNextFrame = false;
        }

        // Die
        if (health <= 0)
            erase();

    }

    public int getStartingHealth() {
        return (int) health;
    }

    public int getHealth() {
        return (int) health;
    }

    public void takeDamageAbsolute(int damage) {
        health -= damage;
    }

    public void takeDamagePerSecond(double damage) {
        health -= damage * changeTime;
    }

    public void createCannonball(int targetX, int targetY, Ship[] targets) {
        // If we actually create a cannonball here, we can get a
        // ConcurrentModificationException.
        // Instead, we just tell the Ship to create it on the next frame
        shouldCreateCannonballNextFrame = true;
        cannonTargetX = targetX;
        cannonTargetY = targetY;
        cannonTargets = targets;
    }
}
