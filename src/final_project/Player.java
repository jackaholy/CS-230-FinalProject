package final_project;

import java.util.function.DoubleConsumer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * This class will probably do some heavy lifting. Controls, player inventory,
 * ship speed, health and so on will all go here
 */
public class Player extends Sprite {
	private int cost;
	private double speed;
	private double turningSpeed;
	private int pickupRadius;

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
		// setRotation(30);
		setRotation(getRotation() + turningSpeed);
	}

	public void moveForward() {
		// System.out.println(getX());
		System.out.println((int) (Math.cos(getRotation()) * speed));
		System.out.println((int) (Math.sin(getRotation()) * speed));
		setX(getX() + (int) (Math.cos(Math.toRadians(getRotation())) * speed));
		setY(getY() + (int) (Math.sin(Math.toRadians(getRotation())) * speed));
	}
}
