package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Loot that spawns on map.
 */

public class Loot extends Sprite {

	int lootX;
	int lootY;

	protected Loot(JFrame gameJFrame, ImageIcon image, int frequency, int x, int y) {
		super(gameJFrame, image, x, y);
		// Subtract 30 so loot doesn't spawn too close to the content pane border.
		getRandomX(30, gameJFrame.getContentPane().getWidth() - 30);
		getRandomY(30, gameJFrame.getContentPane().getHeight() - 30);
		
		this.x = lootX;
		this.y = lootY;
	}

	public int getRandomX(int min, int max) {
		lootX = (int) ((Math.random() * (max - min)) + min);
		return lootX;
	}

	public int getRandomY(int min, int max) {
		lootY = (int) ((Math.random() * (max - min)) + min);
		return lootY;
	}
}