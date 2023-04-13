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

    // Cannon balls we've fired
    private List<Cannonball> cannonballs = new ArrayList<>();
    Healthbar healthbar;

    // Variables to store information for cannon ball creation, see comment in
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
     * @param health       how much health the ship has, arbitrary units
     */
    public Ship(JFrame gameJFrame, ImageIcon image, int x, int y, double speed, double turningSpeed, int health) {
        super(gameJFrame, image, x, y, speed, turningSpeed);
        this.health = health;
        this.startingHealth = health;
        healthbar = new Healthbar(gameJFrame, (int) startingHealth, getWidth());
        healthbar.updatePosition(x, y);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        healthbar.updatePosition(x, y);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        healthbar.updatePosition(x, y);
    }

    @Override
    protected void erase() {
        healthbar.setVisible(false);
        super.erase();
    }

    @Override
    protected void draw() {
        healthbar.setVisible(true);
        super.draw();
    }

    @Override
    protected void tick() {
        // TODO: Test
        // Tell all of our cannon balls to update
        for (Cannonball cannonBall : cannonballs) {
            cannonBall.tick();
        }

        // Don't do anything else if we're dead
        if (!exists)
            return;

        super.tick();

        // If a cannon ball has been requested for this game
        if (shouldCreateCannonballNextFrame) {
            // Create one
            cannonballs.add(new Cannonball(gameJFrame, x, y, cannonTargetX, cannonTargetY, cannonTargets));
            // Mark it as finished
            shouldCreateCannonballNextFrame = false;
        }

        // Die
        if (health <= 0) {
            erase();
            // Solve ghost cannons
            for (Cannonball cannonball : cannonballs) {
                cannonball.erase();
            }
        }

    }

    /**
     * Get the ships initial health
     * 
     * @return int representing the health the ship had at first
     */
    public int getStartingHealth() {
        return (int) startingHealth;
    }

    /**
     * Get the ship's current health
     * 
     * @return int representing the (truncated) value of the ship's health
     */
    public int getHealth() {
        return (int) health;
    }

    /**
     * A one-time hit, like a cannon ball
     * 
     * @param damage
     */
    public void takeDamageAbsolute(int damage) {
        health -= damage;
        healthbar.setHealth((int) health);
    }

    /**
     * Continuous damage, like a collision. Deals a slight amount of damage every
     * time its called, based on the amount of time since the previous tick
     * 
     * @param damage
     */
    public void takeDamagePerSecond(double damage) {
        health -= damage * changeTime;
        healthbar.setHealth((int) health);
    }

    /**
     * Requests the creation of a cannon ball next tick. Creating a cannon ball
     * right
     * now causes concurrent modification issues, so we store the required
     * information and create it later.
     * 
     * @param targetX X position to fire the cannon ball towards
     * @param targetY Y position to fire the cannon ball towards
     * @param targets Ships to run collision checks on and deal damage to
     */
    public void createCannonball(int targetX, int targetY, Ship[] targets) {
        // TODO: Test
        shouldCreateCannonballNextFrame = true;
        cannonTargetX = targetX;
        cannonTargetY = targetY;
        cannonTargets = targets;
    }
}
