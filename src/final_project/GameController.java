package final_project;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.event.MouseInputAdapter;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * The main file of the project. Run this one to start the project
 */
public class GameController {
	private static final int FRAME_RATE = 120;

	private Random rand = new Random();

	private JFrame gameJFrame = new JFrame("Virtual Voyagers");

	private PlayerShip[] availableShips = {
			new PlayerShip(gameJFrame, new ImageIcon("assets/water_bug.png"), 0, 100, 90, 30, 800),
			new PlayerShip(gameJFrame, new ImageIcon("assets/floating_point.png"), 20, 125, 120, 50, 500),
			new PlayerShip(gameJFrame, new ImageIcon("assets/byte_me.png"), 50, 150, 140, 100, 300),
			new PlayerShip(gameJFrame, new ImageIcon("assets/sea++.png"), 100, 125, 70, 250, 100),
			new PlayerShip(gameJFrame, new ImageIcon("assets/world_wide_wet.png"), 150, 50, 40, 400, 50)
	};

	private JTextArea textAreaLoot = new JTextArea();
	private JTextArea textAreaPlayerHealth = new JTextArea();
	private JTextArea textAreaEnemyHealth = new JTextArea();
	private JButton upgradeButton = new JButton("Upgrade");
	// Last known coordinates of the player
	private int cursorX;
	private int cursorY;

	// How much money the player has
	private int money = 0;

	// Where the loot is stored
	private List<Loot> lootList = new ArrayList<>();

	private int currentPlayerShipIndex = 0;
	private PlayerShip currentPlayerShip = availableShips[currentPlayerShipIndex];
	private List<PirateShip> enemies = new ArrayList<>();

	/**
	 * The main method. Literally just creates a new game object
	 * 
	 * @param args boilerplate
	 */
	public static void main(String[] args) {
		new GameController();
	}

	/**
	 * Create a new game, entrypoint for entire program
	 */
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

		// Every time the player clicks the cannonball, fire the cannon
		gameJFrame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				List<Ship> targets = new ArrayList<>();
				// targets.add(currentPlayerShip);
				targets.addAll(enemies);
				currentPlayerShip.createCannonball(cursorX, cursorY, targets.toArray(new Ship[0]));
			}
		});

		currentPlayerShip.setX(300);
		currentPlayerShip.setY(300);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (Loot loot : lootList) {
					loot.draw();
				}
				// Aim for the cursor
				currentPlayerShip.setTarget(cursorX, cursorY);
				// Move towards the cursor
				currentPlayerShip.tick();
				if (rand.nextInt(5000) == 1)
					enemies.add(new PirateShip(
							gameJFrame,
							new ImageIcon("assets/cyber_scourge.png"), lootList,
							120, 75,
							100, 125));

				if (rand.nextInt(75) == 1) {
					Loot newLoot = new Loot(gameJFrame);
					lootList.add(newLoot);
					if (lootList.size() > 20)
						lootList.remove(0).erase();

					newLoot.draw();
				}

				// Check if loot can be collected and handle it if it can
				checkLootCollection();
				textAreaPlayerHealth.setText(String.valueOf(currentPlayerShip.getHealth()));
				textAreaEnemyHealth.setText(String.valueOf(enemies.get(0).getHealth()));

				// Randomly decide to fire the cannons at the player
				for (PirateShip enemy : enemies) {
					if (!enemy.getExistance()) {
						continue;
					}
					if (rand.nextInt(250) == 1) {
						List<Ship> targets = new ArrayList<>();
						targets.add(currentPlayerShip);

						for (Ship potentialTarget : enemies) {
							// Don't let a pirate shoot itself, but let them shoot each other
							if (potentialTarget != enemy) {
								targets.add(potentialTarget);
							}
						}
						enemy.createCannonball(currentPlayerShip.getX(), currentPlayerShip.getY(),
								targets.toArray(new Ship[0]));
					}

					// Aim for the player
					enemy.setTarget(currentPlayerShip.getX(), currentPlayerShip.getY());
					// Move towards the player
					enemy.tick();

					// Check if the two ships are colliding
					if (currentPlayerShip.isColliding(enemy)) {
						currentPlayerShip.takeDamagePerSecond(15);
						enemy.takeDamagePerSecond(15);
						enemy.moveAway(currentPlayerShip);
						currentPlayerShip.moveAway(enemy);
					}
					for (PirateShip otherEnemy : enemies) {
						if (!enemy.getExistance())
							continue;
						// Can't collide with self
						if (otherEnemy == enemy)
							continue;

						if (enemy.isColliding(otherEnemy)) {
							enemy.takeDamagePerSecond(15);
							otherEnemy.takeDamagePerSecond(15);
							enemy.moveAway(otherEnemy);
							otherEnemy.moveAway(enemy);
						}
					}
					textAreaEnemyHealth.setText(String.valueOf(enemy.getHealth()));
				}

			}
		}, 0, 1000 / FRAME_RATE);

	}

	/**
	 * Create the window and content pane for the game itself.
	 */
	private void createWindow() {
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
		textAreaPlayerHealth.setBackground(new Color(30, 144, 255));
		textAreaPlayerHealth.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		textAreaPlayerHealth.setEditable(false);
		textAreaPlayerHealth.setText("100");
		textAreaPlayerHealth.setBounds(87, 40, 43, 33);
		gameJFrame.getContentPane().add(textAreaPlayerHealth);

		// Show the window and player
		gameJFrame.setVisible(true);

		// Displays the amount of health the enemy has left
		textAreaEnemyHealth.setBackground(new Color(30, 144, 255));
		textAreaEnemyHealth.setEditable(false);
		textAreaEnemyHealth.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		textAreaEnemyHealth.setText("100");
		textAreaEnemyHealth.setBounds(164, 69, 30, 31);
		gameJFrame.getContentPane().add(textAreaEnemyHealth);

		upgradeButton.setBounds(gameJFrame.getContentPane().getWidth() - 110, 10, 100, 50);
		upgradeButton.setVisible(false);
		gameJFrame.getContentPane().add(upgradeButton);
		upgradeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlayerShip upgradedShip = availableShips[currentPlayerShipIndex + 1];
				if (money >= upgradedShip.getCost()) {
					money -= upgradedShip.getCost();
					currentPlayerShipIndex++;
					upgradedShip.tick();
					upgradedShip.setX(currentPlayerShip.getX());
					upgradedShip.setY(currentPlayerShip.getY());
					upgradedShip.setRotation(currentPlayerShip.getRotation());
					currentPlayerShip.erase();
					currentPlayerShip = upgradedShip;
					upgradeButton.setVisible(false);
					String displayMoney = "" + money;
					textAreaLoot.setText(displayMoney);
				}
			}
		});
	}

	/**
	 * Initialize any in-game objects that should exist right as the game starts up.
	 */
	private void createSprites() {
		// Create a player
		currentPlayerShip = availableShips[currentPlayerShipIndex];

		// Create an enemy
		enemies.add(new PirateShip(
				gameJFrame,
				new ImageIcon("assets/cyber_scourge.png"), lootList,
				120, 75,
				100, 125));
	}

	/**
	 * Check if any pieces of loot have been picked up. If so, increase player money
	 * and create new loot.
	 */
	private void checkLootCollection() {
		for (Loot loot : lootList) {
			// When the player comes in contact with the loot make it disappear
			if (loot != null && loot.isCollected(currentPlayerShip, 10)) {
				loot.collect(gameJFrame);
				// Increment totalLoot only once
				money++;

				PlayerShip upgradedShip = availableShips[currentPlayerShipIndex + 1];
				if (money >= upgradedShip.getCost()) {
					upgradeButton.setVisible(true);
				}

				String displayMoney = "" + money;
				textAreaLoot.setText(displayMoney);

				lootList.remove(loot);
				break;
			}
		}
	}
}