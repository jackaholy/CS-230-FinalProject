package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Container;
import java.awt.Color;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

/**
 * The main file of the project. Run this one to start the project
 */
public class GameController {
	private static final int FRAMES_PER_SECOND = 120;

	private JFrame gameJFrame;
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
	PlayerShip player;
	PirateShip enemy;

	public static void main(String[] args) {
		new GameController();
	}

	public GameController() {
		createWindow();
		createSprites();

		tickTimer.schedule(new TimerTask() {
			// A single tick of the game
			@Override
			public void run() {
				// Aim for the cursor
				player.setTarget(cursorX, cursorY);
				// Move towards the cursor
				player.tick();

				// Aim for the player
				enemy.setTarget(player.getX(), player.getY());
				// Move towards the player
				enemy.tick();

				for (int i = 0; i < lootArray.length; i++) {
					Loot loot = lootArray[i];
					// when the player comes in contact with the loot make it disappear
					if (loot != null && loot.isCollected(player, 10)) {
						loot.collect(gameJFrame);
						// increment totalLoot only once
						totalLoot++;
						System.out.println("Total Loot: " + totalLoot);

						// Remove the collected loot from the array
						lootArray[i] = new Loot(gameJFrame, new ImageIcon("assets/loot.png"));
						lootArray[i].draw();
						break;
					}
				}
			}
		}, 0, 1000 / FRAMES_PER_SECOND);

		gameJFrame.getContentPane().addMouseMotionListener(new MouseInputAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// Every time the cursor moves, save the new coordinates
				cursorX = e.getX();
				cursorY = e.getY();
			}
		});
	}

	private void createWindow() {
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

		// Show the window and player
		gameJFrame.setVisible(true);
	}

	private void createSprites() {
		// Create a player
		player = new PlayerShip(
				gameJFrame,
				new ImageIcon("assets/water_bug.png"),
				300, 300, 0, 0.9, 0.9, 0);

		// Create an enemy
		enemy = new PirateShip(
				gameJFrame,
				new ImageIcon("assets/floating_point.png"),
				500, 500, 1, 0.4,
				125);

		// Create some loot
		for (int i = 0; i < lootArray.length; i++) {
			lootArray[i] = new Loot(gameJFrame, new ImageIcon("assets/loot.png"));
			// Draw loot on map
			lootArray[i].draw();
		}
	}
}