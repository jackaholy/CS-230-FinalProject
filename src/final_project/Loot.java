package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Loot that spawns on map. Each loot has a randomly assigned x and y
 * coordinate. When the player moves over loot their money count increases and
 * the loot image disappears.
 */

public class Loot extends Sprite {

    // Boolean flag to determine if loot has already been collected
    private boolean collected = false;

    /**
     * Create loot with random coordinates.
     * 
     * @param gameJFrame the window to add the loot to
     */
    public Loot(JFrame gameJFrame) {
	super(gameJFrame, new ImageIcon("assets/loot.png"), 0, 0);

	/*
	 * Set the x and y coordinate of the loot to be added to the JFrame. Subtract 20
	 * so loot doesn't spawn too close to the content pane border.
	 */
	this.x = getRandomCoord(20, gameJFrame.getContentPane().getWidth() - 20);
	this.y = getRandomCoord(20, gameJFrame.getContentPane().getHeight() - 20);
    }

    /**
     * Create loot with specified coordinates.
     * 
     * @param gameJFrame the window to add the loot to
     * @param x          the x coordinate the loot will spawn at
     * @param y          the y coordinate the loot will spawn at
     */
    public Loot(JFrame gameJFrame, int x, int y) {
	super(gameJFrame, new ImageIcon("assets/loot.png"), x, y);
    }

    /**
     * Get a random coordinate between a specified maximum and minimum value.
     * 
     * @param min the minimum coordinate value that loot could spawn
     * @param max the maximum coordinate value that loot could spawn
     * @return a random number between the min and max value. Number can be the min
     *         or max value.
     */
    private int getRandomCoord(int min, int max) {
	return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Checks to see if the player ship overlaps with the loot objects.
     * 
     * @param player  the player's boat
     * @param padding how much space between the player and loot is available
     * @return boolean true if loot is collected by player ship. Otherwise return
     *         false
     */
    protected boolean isCollected(PlayerShip player, int padding) {
	if (!collected && x - padding <= player.getX() && player.getX() <= (x + this.getWidth() + padding)
		&& (y - padding <= player.getY() && player.getY() <= (y + this.getHeight() + padding))) {
	    collected = true;
	    return true;
	}
	return false;
    }

    /**
     * Erase the loot from the game.
     * 
     * @param gameJFrame the window to remove the loot from
     */
    protected void collect(JFrame gameJFrame) {
	gameJFrame.getContentPane().remove(spriteJLabel);
    }
}