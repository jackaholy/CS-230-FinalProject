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
		// Add player tick stuff here
		setRotationDirection(calculateDirectionToDesiredAngle());

		// Add just a bit of passive regen
		if (getHealth() < getStartingHealth())
			takeDamagePerSecond(-0.1);

		if (Math.hypot(getX() - targetX, getY() - targetY) < SLOW_DOWN_DISTANCE) {
			setSpeedMultiplier(Math.hypot(getX() - targetX, getY() - targetY) / SLOW_DOWN_DISTANCE);
		}
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

	public int getCost() {
		return cost;
	}
}