package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class will probably do some heavy lifting. Controls, player inventory,
 * ship speed, health and so on will all go here
 */
public class PlayerShip extends Ship {
	private int cost;

	public PlayerShip(JFrame gameJFrame, ImageIcon image, int x, int y, int cost, double speed,
			double turningSpeed, int health) {
		super(gameJFrame, image, x, y, speed, turningSpeed, health);
		this.cost = cost;
	}

	@Override
	public void tick() {
		// Add player tick stuff here
		setRotationDirection(calculateDirectionToDesiredAngle());
		super.tick();
	}
}