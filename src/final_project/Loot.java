package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Loot that spawns on map. Each loot has a randomly assigned x and y
 * coordinate.
 */

public class Loot extends Sprite {
    // boolean flag to determine if loot has already been collected
    private boolean collected = false;

    /**
     * Create loot with random coordinates
     * 
     * @param gameJFrame the window to add the loot to
     * @param image      the image of the loot
     */
    protected Loot(JFrame gameJFrame, ImageIcon image) {
        super(gameJFrame, image, 0, 0);

        // Subtract 20 so loot doesn't spawn too close to the content pane border.
        this.x = getRandomX(20, gameJFrame.getContentPane().getWidth() - 20);
        this.y = getRandomY(20, gameJFrame.getContentPane().getHeight() - 20);
    }

    /**
     * Create loot with specific coordinates
     * 
     * @param gameJFrame the window to add the loot to
     * @param image      the image of the loot
     * @param x          the x coordinate the loot will spawn at
     * @param y          the y coordinate the loot will spawn at
     */
    protected Loot(JFrame gameJFrame, ImageIcon image, int x, int y) {
        super(gameJFrame, image, x, y);
    }

    /**
     * Set the x coordinate of the loot to a random x value in the window.
     * 
     * @param min - the minimum x coordinate value that loot could spawn
     * @param max - the maximum x coordinate value that loot could spawn
     */
    private int getRandomX(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Set the y coordinate of the loot to a random y value in the window.
     * 
     * @param min - the minimum y coordinate value that loot could spawn
     * @param max - the maximum y coordinate value that loot could spawn
     */
    private int getRandomY(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Is called when the player ship overlaps with the loot objects.
     * 
     * @param player - the player's boat.
     * @return true if loot is collected by player ship.
     */
    protected boolean isCollected(PlayerShip player, int padding) {
        if (!collected && x - padding <= player.getX()
                && player.getX() <= (x + this.getWidth() + padding)
                && (y - padding <= player.getY()
                        && player.getY() <= (y + this.getHeight() + padding))) {
            collected = true;
            return true;
        }
        return false;
    }

    /**
     * 
     * @param gameJFrame
     * @return true if the player collects loot.
     */
    protected boolean collect(JFrame gameJFrame) {
        gameJFrame.getContentPane().remove(spriteJLabel);
        return true;
    }
}