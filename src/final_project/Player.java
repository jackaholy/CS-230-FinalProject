package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class will probably do some heavy lifting. Controls, player inventory,
 * ship speed, health and so on will all go here
 */
public class Player extends Sprite {
	private int cost;
	private int speed;
	private int turningRadius;
	private int pickupRadius;

	public Player(JFrame gameJFrame, ImageIcon image, int x, int y, int cost, int speed, int turningRadius,
			int pickupRadius) {
		super(gameJFrame, image, x, y);
		this.cost = cost;
		this.speed = speed;
		this.turningRadius = turningRadius;
		this.pickupRadius = pickupRadius;
	}
}
