package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class will probably do some heavy lifting. Controls, player inventory,
 * ship speed, health and so on will all go here
 */
public class PlayerShip extends Ship {
	private int cost;
	private final int cannonCooldownMS;
	private boolean cannonCoolingDown = false;

	public PlayerShip(JFrame gameJFrame, ImageIcon image, int x, int y, int cost, double speed,
			double turningSpeed, int health, int cannonCooldownMS) {
		super(gameJFrame, image, x, y, speed, turningSpeed, health);
		this.cost = cost;
		this.cannonCooldownMS = cannonCooldownMS;
	}

	@Override
	public void tick() {
		// Add player tick stuff here
		setRotationDirection(calculateDirectionToDesiredAngle());
		super.tick();
	}

	@Override
	public Cannonball createCannonball(int targetX, int targetY, Ship[] targets) {
		if (cannonCoolingDown) {
			return null;
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				cannonCoolingDown = false;
			}
		}, cannonCooldownMS);
		cannonCoolingDown = true;
		return super.createCannonball(targetX, targetY, targets);
	}
}