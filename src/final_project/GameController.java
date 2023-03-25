package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.event.MouseInputAdapter;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.BorderLayout;

/**
 * The main file of the project. Run this one to start the project
 */
public class GameController {
    	
    	// How much money the player has
 	public int money = 0;
    	
    	private JFrame gameJFrame;
	private JTextArea textAreaLoot = new JTextArea();

	// Last known coordinates of the player
	private int cursorX;
	private int cursorY;

	// Handles how much loot should be onscreen at any given time
	private int lootFrequency = 20;

	// Where the loot is stored
	private Loot lootArray[] = new Loot[lootFrequency];
	private PlayerShip player;
	private PirateShip enemy;

	public static void main(String[] args) {
		new GameController();
	}

	public GameController() {
	    	new TitleScreen();
		createWindow();
		createSprites();
		gameJFrame.getContentPane().addMouseMotionListener(new MouseInputAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// Every time the cursor moves, save the new coordinates
				cursorX = e.getX();
				cursorY = e.getY();
			}
		});

		while (true) {
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
			// Aim for the cursor
			player.setTarget(cursorX, cursorY);
			// Move towards the cursor
			player.tick();

			// Aim for the player
			enemy.setTarget(player.getX(), player.getY());
			// Move towards the player
			enemy.tick();

			// Check if the two ships are colliding
			if (player.isColliding(enemy)) {
				// DEAL DAMAGE HERE
				enemy.moveAway(player);
				player.moveAway(enemy);
			}

			// Check if loot can be collected and handle it if it can
			checkLootCollection();
		}
	}
	
	/**
	 * Create the window and content pane for the game itself
	 */
	private void createWindow() {
		// The window itself
		gameJFrame = new JFrame("Virtual Voyagers");
		// With arbitrary default dimensions
		gameJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		gameJFrame.setSize(width, height);

		// The play area
		Container gameContentPane = gameJFrame.getContentPane();
		gameContentPane.setBackground(new Color(30, 144, 255));
		// Use absolute positioning
		gameContentPane.setLayout(null);

		// Label for the amount of loot collected
		JLabel lblLoot = new JLabel("Loot: ");
		lblLoot.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		lblLoot.setBounds(19, 18, 61, 16);
		gameJFrame.getContentPane().add(lblLoot);

		// Label for the amount of health the user has left
		JLabel lblUserHealth = new JLabel("Health: ");
		lblUserHealth.setHorizontalAlignment(SwingConstants.LEFT);
		lblUserHealth.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		lblUserHealth.setBounds(19, 48, 76, 16);
		gameJFrame.getContentPane().add(lblUserHealth);

		// Label for the amount of health the enemy has left
		JLabel lblEnemyHealth = new JLabel("Enemy's Health: ");
		lblEnemyHealth.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		lblEnemyHealth.setBounds(19, 69, 144, 33);
		gameJFrame.getContentPane().add(lblEnemyHealth);

		// Displays the amount of loot the user has collected

		textAreaLoot.setBackground(new Color(30, 144, 255));
		textAreaLoot.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		textAreaLoot.setEditable(false);
		textAreaLoot.setText("0");
		textAreaLoot.setBounds(77, 11, 30, 33);
		gameJFrame.getContentPane().add(textAreaLoot);

		// Displays the amount of health the user has left
		JTextArea textAreaUserHealth = new JTextArea();
		textAreaUserHealth.setBackground(new Color(30, 144, 255));
		textAreaUserHealth.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		textAreaUserHealth.setEditable(false);
		textAreaUserHealth.setText("100");
		textAreaUserHealth.setBounds(87, 40, 43, 33);
		gameJFrame.getContentPane().add(textAreaUserHealth);

		// Displays the amount of health the enemy has left
		JTextArea textAreaEnemyHealth = new JTextArea();
		textAreaEnemyHealth.setBackground(new Color(30, 144, 255));
		textAreaEnemyHealth.setEditable(false);
		textAreaEnemyHealth.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		textAreaEnemyHealth.setText("100");
		textAreaEnemyHealth.setBounds(164, 69, 30, 31);
		gameJFrame.getContentPane().add(textAreaEnemyHealth);

		// Show the window and player
		gameJFrame.setVisible(true);
	}

	/**
	 * Initialize any in-game objects that should exist right as the game starts up
	 */
	private void createSprites() {
		// Create a player
		player = new PlayerShip(
				gameJFrame,
				new ImageIcon("assets/water_bug.png"),

				300, 300, 0, 100, 100, 125);


		// Create an enemy
		enemy = new PirateShip(
				gameJFrame,
				new ImageIcon("assets/cyber_scourge.png"),

				500, 500, 100, 100,

				125);

		// Create some loot
		for (int i = 0; i < lootArray.length; i++) {
			lootArray[i] = new Loot(gameJFrame, new ImageIcon("assets/loot.png"));
			// Draw loot on map
			lootArray[i].draw();
		}
	}

	/**
	 * Check if any pieces of loot have been picked up. If so, increase player money
	 * and create new loot
	 */
	private void checkLootCollection() {
		for (int i = 0; i < lootArray.length; i++) {
			Loot loot = lootArray[i];
			// when the player comes in contact with the loot make it disappear
			if (loot != null && loot.isCollected(player, 10)) {
				loot.collect(gameJFrame);
				// increment totalLoot only once
				money++;
				String displayMoney = "" + money;
				textAreaLoot.setText(displayMoney);

				// Remove the collected loot from the array
				lootArray[i] = new Loot(gameJFrame, new ImageIcon("assets/loot.png"));
				lootArray[i].draw();
				break;
			}
		}
	}
}