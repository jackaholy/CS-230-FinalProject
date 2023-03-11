package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Container;
import java.awt.Color;
import java.awt.Component;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

import final_project.MovingSprite.Direction;

/**
 * The main file of the project. Run this one to start the project
 */
public class GameController {
	private static final int FRAMES_PER_SECOND = 120;

	// Timer goes off once per frame
	private final Timer tickTimer = new Timer();

	// Last known coordinates of the player
	private int cursorX;
	private int cursorY;

	// Handles how much loot spawns
	private int lootFrequency = 20;
	// How much loot has been collected
	public int totalLoot = 0;
	// Where the loot is stored
	Loot lootArray[] = new Loot[lootFrequency];

	public static void main(String[] args) {
		new GameController();
	}

	public GameController() {
		// The window itself
		JFrame gameJFrame = new JFrame("Virtual Voyagers");
		// With arbitrary default dimensions
		gameJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gameJFrame.setSize(800, 600);

		// The play area
		Container gameContentPane = gameJFrame.getContentPane();
		gameContentPane.setBackground(Color.blue);
		// Use absolute positioning
		gameContentPane.setLayout(null);

		// Create a player
		PlayerShip player = new PlayerShip(
				gameJFrame,
				new ImageIcon("assets/water_bug.png"),
				300, 300, 0, 0.5, 0.5, 0);

		// Create an enemy
		PirateShip enemy = new PirateShip(
				gameJFrame,
				new ImageIcon("assets/floating_point.png"),
				500, 500, 1, 0.4,
				125);

		// Show the window and player
		gameJFrame.setVisible(true);
		player.draw();

		// Create some loot
		for (int i = 0; i < lootArray.length; i++) {
			lootArray[i] = new Loot(gameJFrame, new ImageIcon("assets/loot.png"), 100, 100);
			// Draw loot on map
			lootArray[i].draw();
		}

		tickTimer.schedule(new TimerTask() {
			// A single tick of the game
			@Override
			public void run() {
				// Aim for the cursor
				player.setTarget(cursorX, cursorY);
				// Move towards the cursor
				// player.tick();
				player.setRotationDirection(Direction.CLOCKWISE);
				player.rotate();
				player.draw();

				// Aim for the player
				// enemy.setTarget(player.getX(), player.getY());
				// Move towards the player
				// enemy.tick();
				enemy.setX(player.getCorners()[0].x);
				enemy.setY(player.getCorners()[0].y);
				enemy.draw();
			}
		}, 0, 1000 / FRAMES_PER_SECOND);

		gameContentPane.addMouseMotionListener(new MouseInputAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// Every time the cursor moves, save the new coordinates
				cursorX = e.getX();
				cursorY = e.getY();
			}
		});
	}
}