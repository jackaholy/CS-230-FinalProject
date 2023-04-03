package final_project;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * A MovingSprite capable of firing cannons and taking damage.
 */
public abstract class Ship extends MovingSprite {
    private double health;

    // Cannonballs we've fired
    private List<Cannonball> cannonballs = new ArrayList<>();

    // Variables to store information for cannonball creation, see comment in
    // createCannonball
    private boolean shouldCreateCannonballNextFrame = false;
    private Cannonball queuedCannonball;

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
    protected Ship(JFrame gameJFrame, ImageIcon image, int x, int y, double speed, double turningSpeed, int health) {
        super(gameJFrame, image, x, y, speed, turningSpeed);
        this.health = health;
    }

    @Override
    public void tick() {
        // Don't do anything if we're dead
        if (!exists)
            return;

        super.tick();

        // If a cannonball has been requested for this game
        if (shouldCreateCannonballNextFrame) {
            // Create one
            if (queuedCannonball != null) {
                cannonballs.add(queuedCannonball);
                queuedCannonball = null;
            }
            // Mark it as finished
            shouldCreateCannonballNextFrame = false;
        }

        // Tell all of our cannonballs to update
        for (Cannonball cannonBall : cannonballs) {
            cannonBall.tick();
        }

        // Die
        if (health <= 0)
            erase();

    }

    public int getHealth() {
        return (int) health;
    }

    public void takeDamageAbsolute(int damage) {
        health -= damage;
    }

    public void takeDamagePerSecond(int damage) {
        System.out.println(damage * changeTime);
        health -= damage * changeTime;
    }

    /**
     * Requests the creation of a cannonball next tick. Creating a cannonball right
     * now causes concurrent modification issues, so we store the required
     * information and create it later.
     * 
     * @param targetX X position to fire the cannonball towards
     * @param targetY Y position to fire the cannonball towards
     * @param targets Ships to run collision checks on and deal damage to
     */
    public Cannonball createCannonball(int targetX, int targetY, Ship[] targets) {
        queuedCannonball = new Cannonball(gameJFrame, x, y, targetX, targetY, targets);
        return queuedCannonball;
    }
}
