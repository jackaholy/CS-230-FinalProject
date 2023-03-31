package final_project;

import javax.swing.JFrame;

import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * A pirate. Targets the player but tries to avoid direct collisions.
 */
public class PirateShip extends Ship {
    private static Random rand = new Random();

    // How close to the player is too close?
    private int turnDistance;

    private List<Loot> lootList;

    /**
     * Create the pirate
     * 
     * @param gameJFrame   the window to add the pirate to
     * @param image        the image of the pirate
     * @param lootList     the list to add loot to after the pirate dies
     * @param x            the starting X position of the pirate
     * @param y            the starting Y position of the pirate
     * @param speed        how fast it should move
     * @param turningSpeed how fast it should turn
     * @param turnDistance how close it should get to the player before turning
     *                     around to avoid a collision
     */
    public PirateShip(JFrame gameJFrame, ImageIcon image, List<Loot> lootList, double speed,
            double turningSpeed, int health,
            int turnDistance) {
        // Create the super class
        super(gameJFrame, image, rand.nextInt(20, gameJFrame.getContentPane().getWidth() - 20),
                rand.nextInt(20, gameJFrame.getContentPane().getHeight() - 20), speed, turningSpeed,
                health);

        // Set the lootList
        this.lootList = lootList;
        // Set the turn distance
        this.turnDistance = turnDistance;
    }

    @Override
    protected void tick() {
        Direction directionToRotate = calculateDirectionToDesiredAngle();

        if (shouldAvoidPlayer()) {
            directionToRotate = oppositeDirection(directionToRotate);
        }

        setRotationDirection(directionToRotate);
        super.tick();
    }

    @Override
    protected void erase() {
        for (int i = 0; i < 15; i++) {
            Loot newLoot = new Loot(gameJFrame);
            newLoot.setX(x + rand.nextInt(-15, 15));
            newLoot.setY(y + rand.nextInt(-15, 15));
            lootList.add(newLoot);
        }
        super.erase();
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