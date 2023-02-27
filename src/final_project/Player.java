package final_project;

import java.util.function.DoubleConsumer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class will probably do some heavy lifting. Controls, player inventory,
 * ship speed, health and so on will all go here
 */
public class Player extends Sprite {
	public enum Direction {
		CLOCKWISE,
		COUNTER_CLOCKWISE,
		NOT_ROTATING
	}

	private int cost;
	private double speed;
	private double turningSpeed;
	private int pickupRadius;
	private Direction rotationDirection = Direction.NOT_ROTATING;

	public Player(JFrame gameJFrame, ImageIcon image, int x, int y, int cost, double speed,
			double turningSpeed,
			int pickupRadius) {
		super(gameJFrame, image, x, y);
		this.cost = cost;
		this.speed = speed;
		this.turningSpeed = turningSpeed;
		this.pickupRadius = pickupRadius;
	}

	public void tick() {
		rotate();
		moveForward();
		draw();
	}

	public void rotate() {
		if (rotationDirection == Direction.CLOCKWISE) {

			setRotation(getRotation() + turningSpeed);
		} else if (rotationDirection == Direction.COUNTER_CLOCKWISE) {
			setRotation(getRotation() - turningSpeed);
		}
	}

	public void setRotationDirection(Direction direction) {
		this.rotationDirection = direction;
	}

	public void moveForward() {
		setX(getX() - (int) (Math.cos(Math.toRadians(getRotation())) * speed));
		setY(getY() - (int) (Math.sin(Math.toRadians(getRotation())) * speed));
	}
}
