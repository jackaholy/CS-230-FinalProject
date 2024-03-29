package final_project;

import javax.swing.ImageIcon;
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
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.event.MouseInputAdapter;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.JTextArea;

/**
 * This file handles the actual gameplay of the project including the various
 * levels of boats, updating the player and enemy, and handling collisions for 
 * boats and loot.
 */
public class GameController {
	// Number of ticks per second
	private static final int FRAME_RATE = 120;
	// 1 / X chance of each pirate firing their cannon every tick
	private static final int PIRATE_CANNON_FIRE_ODDS = 400;
	// 1 / X chance of the boss firing its cannon every tick
	private static final int BOSS_PIRATE_CANNON_FIRE_ODDS = 75;
	// 1 / X chance of loot spawning every tick
	private static final int LOOT_SPAWN_ODDS = 150;
	// Maximum amount of loot to naturally spawn at a time
	// More may appear if pirates drop it
	private static final int TOTAL_LOOT = 10;
	// How much damage to deal to colliding ships every second
	private static final int COLLISION_DAMAGE_PER_SECOND = 15;
	// How many pixels ahead of the player the enemy should aim.
	private static final double CANNONBALL_LEAD = 0.75;

	// Flag to disable ticks while another operation completes.
	// Prevents concurrency issues when event listeners fire
	private boolean freeze = false;

	private Random rand = new Random();

	private JFrame gameJFrame = new JFrame("Virtual Voyagers");
	private PlayerShip[] availableShips = {
			new PlayerShip(gameJFrame, new ImageIcon("assets/images/water_bug.png"), 0, 100, 90, 30, 800),
			new PlayerShip(gameJFrame, new ImageIcon("assets/images/floating_point.png"), 20, 125, 120, 50, 500),
			new PlayerShip(gameJFrame, new ImageIcon("assets/images/byte_me.png"), 50, 150, 140, 100, 300),
			new PlayerShip(gameJFrame, new ImageIcon("assets/images/sea++.png"), 75, 125, 100, 250, 150),
			new PlayerShip(gameJFrame, new ImageIcon("assets/images/world_wide_wet.png"), 100, 100, 90, 500, 50)
	};

	// The last time we tried spawning a pirate
	private long previousEnemySpawnTime = 0;

	// Labels on the screen
	private JTextArea textAreaLoot = new JTextArea();
	private JLabel lblUpgrade = new JLabel();
	private JLabel lblLoot = new JLabel("Loot: ");

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
	private List<PirateShip> deadEnemies = new ArrayList<>();

	// This flag can be used to stop gameMusic
	private AtomicBoolean gameMusicPlaying = new AtomicBoolean(true);
	private AtomicBoolean bossMusicPlaying = new AtomicBoolean(false);

	private Timer gameTimer = new Timer(1000 / FRAME_RATE, null);
	private boolean gameOver = false;

	/**
	 * Create a new game, entry point for entire program
	 */
	public GameController() {

		SoundHelper.getInstance().playSound("gamemusic.wav", gameMusicPlaying, true);
		createWindow();
		createSprites();
		registerEventListeners();

		gameTimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (freeze)
					return;
				lblUpgrade.setBounds(gameJFrame.getContentPane().getWidth() / 2 - lblUpgrade.getWidth() / 2,
						gameJFrame.getContentPane().getHeight() - lblUpgrade.getHeight(), 200, 20);

				removeDeadEnemies();

				updatePlayer();

				checkLootCollection();

				updateEnemies();

				redrawLoot();

				attemptLootSpawn();

				attemptEnemySpawn();

				// If the player dies, erase them and end the game
				if (currentPlayerShip.getHealth() <= 0) {
					gameOver = true;
					currentPlayerShip.erase();
					gameJFrame.dispose();
					gameMusicPlaying.set(false);
					bossMusicPlaying.set(false);
					gameTimer.stop();
					new DeathScreen();
				}
			}
		});
		gameTimer.start();
	}

	/**
	 * Create the window and content pane for the game itself.
	 */
	private void createWindow() {
		lblUpgrade.setText("Next ship costs: " + availableShips[currentPlayerShipIndex + 1].getCost());
		lblUpgrade.setVisible(true);
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

		lblUpgrade.setFont(new Font("Apple Chancery", Font.PLAIN, 20));
		lblUpgrade.setVisible(true);
		gameJFrame.getContentPane().add(lblUpgrade);

		gameJFrame.setVisible(true);
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
				new ImageIcon("assets/images/cyber_scourge.png"), lootList,
				120, 75,
				100, 125));
	}

	/**
	 * Add event listeners for cursor movement, key presses, clicks, etc.
	 */
	private void registerEventListeners() {
		gameJFrame.getContentPane().addMouseMotionListener(new MouseInputAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				freeze = true;
				// Every time the cursor moves, save the new coordinates
				cursorX = e.getX();
				cursorY = e.getY();
				freeze = false;
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				freeze = true;
				// Every time the cursor moves, save the new coordinates
				cursorX = e.getX();
				cursorY = e.getY();
				freeze = false;
			}
		});

		// Every time the player clicks the mouse, fire the cannon
		gameJFrame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				freeze = true;
				fireCannon();
				freeze = false;
			}
		});

		gameJFrame.getContentPane().requestFocus();
		gameJFrame.getContentPane().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				freeze = true;
				if (e.getKeyCode() == 85) {
					// "U"
					upgradeShip();
				} else if (e.getKeyCode() == 32) {
					// Space
					fireCannon();
				}
				if (e.getKeyCode() == 81) {
					// "Q"
					System.exit(0);
				}
				super.keyPressed(e);
				freeze = false;
			}
		});
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
					lblUpgrade.setText("Press \"U\" to upgrade");
				}

				String displayMoney = "" + money;
				textAreaLoot.setText(displayMoney);

				lootList.remove(loot);
				break;
			}
		}
	}

	private void upgradeShip() {
		freeze = true;
		if (currentPlayerShipIndex == availableShips.length - 1) {
			freeze = false;
			return;
		}
		PlayerShip upgradedShip = availableShips[currentPlayerShipIndex + 1];
		if (money < upgradedShip.getCost())
			return;
		if (currentPlayerShipIndex > availableShips.length - 1)
			return;
		SoundHelper.getInstance().playSound("money.wav");
		money -= upgradedShip.getCost();
		currentPlayerShipIndex++;
		upgradedShip.tick();
		upgradedShip.setX(currentPlayerShip.getX());
		upgradedShip.setY(currentPlayerShip.getY());
		upgradedShip.setRotation(currentPlayerShip.getRotation());
		currentPlayerShip.erase();
		currentPlayerShip = upgradedShip;
		// If the user has the World Wide Wet boat, spawn the boss.
		if (currentPlayerShipIndex == availableShips.length - 1) {
			enemies.add(new FinalBoss(gameJFrame, lootList, 100, 40, 1000, 300));
			gameMusicPlaying.set(false);
			bossMusicPlaying.set(true);
			SoundHelper.getInstance().playSound("bossmusic.wav", bossMusicPlaying, true);
			// Remove labels
			textAreaLoot.setVisible(false);
			lblUpgrade.setVisible(false);
			lblLoot.setVisible(false);
		}
		if (currentPlayerShipIndex != availableShips.length - 1) {
			lblUpgrade.setText("Next ship costs: " + availableShips[currentPlayerShipIndex + 1].getCost());
		}
		String displayMoney = "" + money;
		textAreaLoot.setText(displayMoney);
		freeze = false;
	}

	private void fireCannon() {
		List<Ship> targets = new ArrayList<>();
		targets.addAll(enemies);
		currentPlayerShip.createCannonball(cursorX, cursorY, targets.toArray(new Ship[0]));
	}

	private void removeDeadEnemies() {
		for (PirateShip enemy : deadEnemies) {
			enemies.remove(enemy);
		}
		deadEnemies.clear();
	}

	private void redrawLoot() {
		for (Loot loot : lootList) {
			loot.draw();
		}
	}

	private void updatePlayer() {
		// Aim for the cursor
		currentPlayerShip.setTarget(cursorX, cursorY);
		// Move towards the cursor
		currentPlayerShip.tick();
	}

	private void attemptEnemySpawn() {
		if (previousEnemySpawnTime + 1000 > System.currentTimeMillis())
			return;
		previousEnemySpawnTime = System.currentTimeMillis();

		double cap = 1.2 * currentPlayerShipIndex + 1;

		double r = 0.2;

		// Formula my roommate gave me for modeling ecosystems that works well on
		// pirates
		double probability = r * (enemies.size() + 1) * (1 - (enemies.size() / (double) cap)) + 0.001;
		if (rand.nextDouble() < probability)
			enemies.add(new PirateShip(
					gameJFrame,
					new ImageIcon("assets/images/cyber_scourge.png"), lootList,
					120, 75,
					100, 125));
	}

	private void attemptLootSpawn() {
		if (rand.nextInt(LOOT_SPAWN_ODDS) == 1) {
			Loot newLoot = new Loot(gameJFrame);
			lootList.add(newLoot);
			if (lootList.size() > TOTAL_LOOT)
				lootList.remove(0).erase();

			newLoot.draw();
		}
	}

	private void updateEnemies() {
		for (PirateShip enemy : enemies) {
			// If the enemy doesn't exist, skip and mark for removal
			if (!enemy.getExistance()) {
				deadEnemies.add(enemy);
				continue;
			}
			updateEnemy(enemy);
			attemptFireCannon(enemy);
			checkPlayerCollision(enemy);
			checkEnemyCollision(enemy);
		}
	}

	private void attemptFireCannon(PirateShip enemy) {
		int fireRate = PIRATE_CANNON_FIRE_ODDS;
		if (enemy instanceof FinalBoss)
			fireRate = BOSS_PIRATE_CANNON_FIRE_ODDS;
		if (Math.hypot(enemy.getX() - currentPlayerShip.getX(),
				enemy.getY() - currentPlayerShip.getY()) > Cannonball.TRAVEL_DISTANCE)
			return;
		if (rand.nextInt(fireRate) == 1) {
			List<Ship> targets = new ArrayList<>();
			targets.add(currentPlayerShip);

			for (Ship potentialTarget : enemies) {
				// Don't let a pirate shoot itself, but let them shoot each other
				if (potentialTarget != enemy) {
					targets.add(potentialTarget);
				}
			}
			int predictedX = (int) (currentPlayerShip.getX()
					- Math.cos(Math.toRadians(currentPlayerShip.getRotation())) * currentPlayerShip.getSpeed()
							* CANNONBALL_LEAD);
			int predictedY = (int) (currentPlayerShip.getY()
					- Math.sin(Math.toRadians(currentPlayerShip.getRotation())) * currentPlayerShip.getSpeed()
							* CANNONBALL_LEAD);
			enemy.createCannonball(predictedX, predictedY,
					targets.toArray(new Ship[0]));

		}
	}

	private void updateEnemy(PirateShip enemy) {
		// Check to see if the final boss is dead
		if (enemy instanceof FinalBoss && enemy.getHealth() <= 0) {
			victory();
		}

		// Aim for the player
		enemy.setTarget(currentPlayerShip.getX(), currentPlayerShip.getY());
		// Move towards the player
		enemy.tick();
	}

	// Called when the final boss is defeated
	private void victory() {
		// Prevent multiple victories from displaying
		if (gameOver)
			return;
		gameOver = true;

		SoundHelper.getInstance().playSound("victory.wav");
		// Start a 3 second timer after defeating the final boss. This gives time for
		// celebration
		Timer delayTimer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Stop the main game and dispose of the window
				gameTimer.stop();
				gameJFrame.dispose();
				gameMusicPlaying.set(false);
				bossMusicPlaying.set(false);
				// You win!
				new VictoryScreen();
			}
		});
		delayTimer.setRepeats(false);
		delayTimer.start();
	}

	private void checkPlayerCollision(PirateShip enemy) {
		// Check if the two ships are colliding
		if (currentPlayerShip.isColliding(enemy)) {
			currentPlayerShip.takeDamagePerSecond(COLLISION_DAMAGE_PER_SECOND);
			enemy.takeDamagePerSecond(COLLISION_DAMAGE_PER_SECOND);

			enemy.moveAway(currentPlayerShip);
			currentPlayerShip.moveAway(enemy);
		}
	}

	private void checkEnemyCollision(PirateShip enemy) {
		for (PirateShip otherEnemy : enemies) {
			// Can't collide with nonexistant ships or self
			if (!enemy.getExistance() || otherEnemy == enemy)
				continue;

			if (enemy.isColliding(otherEnemy)) {
				enemy.takeDamagePerSecond(COLLISION_DAMAGE_PER_SECOND);
				otherEnemy.takeDamagePerSecond(COLLISION_DAMAGE_PER_SECOND);
				enemy.moveAway(otherEnemy);
				otherEnemy.moveAway(enemy);
			}
		}
	}
}