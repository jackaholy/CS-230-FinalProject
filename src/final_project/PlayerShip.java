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
	private int SLOW_DOWN_DISTANCE = 75;
	private int cost;
	private final int cannonCooldownMS;
	private boolean cannonCoolingDown = false;

	public PlayerShip(JFrame gameJFrame, ImageIcon image, int cost, double speed,
			double turningSpeed, int health, int cannonCooldownMS) {
		super(gameJFrame, image, 500, 300, speed, turningSpeed, health);
		this.cost = cost;
		this.cannonCooldownMS = cannonCooldownMS;
	}

	@Override
	public void tick() {
		// TODO: Test
		// Add player tick stuff here
		setRotationDirection(calculateDirectionToDesiredAngle());

		// Add some passive health regeneration
		if (getHealth() < getStartingHealth())
			takeDamagePerSecond(-1);
		if (Math.hypot(getX() - targetX, getY() - targetY) < SLOW_DOWN_DISTANCE) {
			setSpeedMultiplier(Math.hypot(getX() - targetX, getY() - targetY) / SLOW_DOWN_DISTANCE);
		} else {
			setSpeedMultiplier(1);
		}
		super.tick();
	}

	@Override
	public void createCannonball(int targetX, int targetY, Ship[] targets) {
		// TODO: Test
		if (cannonCoolingDown) {
			return;
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				cannonCoolingDown = false;
			};
		}, cannonCooldownMS);
		super.createCannonball(targetX, targetY, targets);
		cannonCoolingDown = true;
	}

	public int getCost() {
		return cost;
	}
}