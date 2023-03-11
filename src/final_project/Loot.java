package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Loot that spawns on map. Each loot has a randomly assigned x and y
 * coordinate.
 */

public class Loot extends Sprite {
    // the coordinates that the loot will spawn at
    int lootX;
    int lootY;
    // arbitrary loot positions
    int xPosition;
    int yPosition;
    // the total amount of loot collected
    int totalLoot;

    /**
     * Create loot
     * 
     * @param gameJFrame the window to add the loot to
     * @param image      the image of the loot
     * @param x          the x coordinate the loot will spawn at
     * @param y          the y coordinate the loot will spawn at
     */
    protected Loot(JFrame gameJFrame, ImageIcon image, int x, int y) {
        super(gameJFrame, image, x, y);
        // Subtract 20 so loot doesn't spawn too close to the content pane border.
        xPosition = getRandomX(30, gameJFrame.getContentPane().getWidth() - 20);
        yPosition = getRandomY(30, gameJFrame.getContentPane().getHeight() - 20);

        this.setX(lootX);
        this.setY(lootY);
    }

    /**
     * Set the x coordinate of the loot to a random x value in the window.
     * 
     * @param min - the minimum x coordinate value that loot could spawn
     * @param max - the maximum x coordinate value that loot could spawn
     */
    private int getRandomX(int min, int max) {
        lootX = (int) ((Math.random() * (max - min)) + min);
        return lootX;
    }

    /**
     * Set the y coordinate of the loot to a random y value in the window.
     * 
     * @param min - the minimum y coordinate value that loot could spawn
     * @param max - the maximum y coordinate value that loot could spawn
     */
    private int getRandomY(int min, int max) {
        lootY = (int) ((Math.random() * (max - min)) + min);
        return lootY;
    }

    /**
     * Is called when the player ship overlaps with the loot objects.
     * 
     * @param player - the player's boat.
     * @return true if loot is collected by player ship.
     */
    private boolean isCollected(PlayerShip player, Loot loot) {

        if (xPosition <= player.getX() && player.getX() <= (xPosition + loot.getWidth())
                && (yPosition <= player.getY() && player.getY() <= (yPosition + loot.getHeight()))) {
            // ERASE IMAGE OF LOOT.

            totalLoot++;
            System.out.println(totalLoot);
            return true;
        }
        return false;
    }

    /**
     * 
     * @return lootCollected
     */
    private int getLootCollected() {
        return totalLoot;
    }

    /**
     * 
     * @return lootX
     */
    private int getLootX() {
        return lootX;
    }

    /**
     * 
     * @return lootY
     */
    private int getLootY() {
        return lootY;
    }
}