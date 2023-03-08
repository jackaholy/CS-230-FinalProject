package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class will probably do some heavy lifting. Controls, player inventory,
 * ship speed, health and so on will all go here
 */
public class PlayerShip extends MovingSprite {
	private int cost;
	private int pickupRadius;

	public PlayerShip(JFrame gameJFrame, ImageIcon image, int x, int y, int cost, double speed,
			double turningSpeed,
			int pickupRadius) {
		super(gameJFrame, image, x, y, speed, turningSpeed);
		this.cost = cost;
		this.pickupRadius = pickupRadius;
	}

	public void tick() {
		// Add player tick stuff here
		super.tick();
	}
}