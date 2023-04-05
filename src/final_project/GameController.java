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
			new PlayerShip(gameJFrame, new ImageIcon("assets/sea++.png"), 100, 125, 100, 250, 100),
			new PlayerShip(gameJFrame, new ImageIcon("assets/world_wide_wet.png"), 150, 100, 90, 400, 50)
	};

	private JTextArea textAreaLoot = new JTextArea();
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
	private List<PirateShip> enemiesToRemoveNextTick = new ArrayList<>();

	/**
	 * The main method. Literally just creates a new game object
	 * 
	 * @param args boilerplate
	 */
	public static void main(String[] args) {
		new GameController();
	}

	/**
	 * Create a new game, entry point for entire program
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

		// Every time the player clicks the cannon ball, fire the cannon
		gameJFrame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				List<Ship> targets = new ArrayList<>();
				// targets.add(currentPlayerShip);
				targets.addAll(enemies);
				currentPlayerShip.createCannonball(cursorX, cursorY, targets.toArray(new Ship[0]));
			}
		});
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (PirateShip enemy : enemiesToRemoveNextTick) {
					enemies.remove(enemy);
				}
				enemiesToRemoveNextTick.clear();

				for (Loot loot : lootList) {
					loot.draw();
				}
				// Aim for the cursor
				currentPlayerShip.setTarget(cursorX, cursorY);
				// Move towards the cursor
				currentPlayerShip.tick();
				if (rand.nextInt((2000 * enemies.size()) + 1000) == 1)
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

				// Randomly decide to fire the cannons at the player
				for (PirateShip enemy : enemies) {
					if (!enemy.getExistance()) {
						enemiesToRemoveNextTick.add(enemy);
						continue;
					}
					int fireRate = 250;
					if (enemy instanceof FinalBoss)
						fireRate = 75;
					if (rand.nextInt(fireRate) == 1) {
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

		// Displays the amount of loot the user has collected

		textAreaLoot.setBackground(new Color(30, 144, 255));
		textAreaLoot.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		textAreaLoot.setEditable(false);
		textAreaLoot.setText("0");
		textAreaLoot.setBounds(77, 11, 30, 33);
		gameJFrame.getContentPane().add(textAreaLoot);
		// Show the window and player
		gameJFrame.setVisible(true);

		upgradeButton.setBounds(gameJFrame.getContentPane().getWidth() - 110, 10, 100, 50);
		upgradeButton.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		upgradeButton.setVisible(false);
		gameJFrame.getContentPane().add(upgradeButton);
		upgradeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentPlayerShipIndex > availableShips.length - 1)
					return;
				if (currentPlayerShipIndex == availableShips.length - 2)
					enemies.add(new FinalBoss(gameJFrame, lootList, 100, 40, 1000, 300));
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
				if (currentPlayerShipIndex >= availableShips.length - 1)
					return;
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