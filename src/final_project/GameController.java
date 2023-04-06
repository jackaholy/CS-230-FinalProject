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
	// 1 / X chance of a pirate spawning every tick
	private static final int BASE_PIRATE_SPAWN_ODDS = 200;
	// Reduce the chances by X for every pirate that already exists
	private static final int PER_PIRATE_SPAWN_ODDS_INCREASE = 2000;
	// How much damage to deal to colliding ships every second
	private static final int COLLISION_DAMAGE_PER_SECOND = 15;

	private boolean freeze = false;

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
	private JLabel lblUpgrade = new JLabel();
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
		registerEventListeners();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (freeze)
					return;
				lblUpgrade.setBounds(gameJFrame.getContentPane().getWidth() / 2 - lblUpgrade.getWidth() / 2,
						gameJFrame.getContentPane().getHeight() - lblUpgrade.getHeight(), 200, 20);
				lblUpgrade.setVisible(true);

				removeDeadEnemies();

				updatePlayer();
				checkLootCollection();

				updateEnemies();

				redrawLoot();

				attemptLootSpawn();
				attemptEnemySpawn();
			}

		}, 0, 1000 / FRAME_RATE);

	}

	/**
	 * Create the window and content pane for the game itself.
	 */
	private void createWindow() {
		lblUpgrade
				.setText("Next ship costs: " + availableShips[currentPlayerShipIndex + 1].getCost());
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
				new ImageIcon("assets/cyber_scourge.png"), lootList,
				120, 75,
				100, 125));
	}

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
		PlayerShip upgradedShip = availableShips[currentPlayerShipIndex + 1];
		if (money < upgradedShip.getCost())
			return;

		if (currentPlayerShipIndex > availableShips.length - 1)
			return;
		if (currentPlayerShipIndex == availableShips.length - 2)
			enemies.add(new FinalBoss(gameJFrame, lootList, 100, 40, 1000, 300));
		money -= upgradedShip.getCost();
		currentPlayerShipIndex++;
		upgradedShip.tick();
		upgradedShip.setX(currentPlayerShip.getX());
		upgradedShip.setY(currentPlayerShip.getY());
		upgradedShip.setRotation(currentPlayerShip.getRotation());
		currentPlayerShip.erase();
		currentPlayerShip = upgradedShip;
		lblUpgrade
				.setText("Next ship costs: " + availableShips[currentPlayerShipIndex + 1].getCost());
		String displayMoney = "" + money;
		textAreaLoot.setText(displayMoney);
		freeze = false;
	}

	private void fireCannon() {
		List<Ship> targets = new ArrayList<>();
		// targets.add(currentPlayerShip);
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
		if (rand.nextInt((PER_PIRATE_SPAWN_ODDS_INCREASE * enemies.size()) + BASE_PIRATE_SPAWN_ODDS) == 1)
			enemies.add(new PirateShip(
					gameJFrame,
					new ImageIcon("assets/cyber_scourge.png"), lootList,
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
	}

	private void updateEnemy(PirateShip enemy) {
		// Aim for the player
		enemy.setTarget(currentPlayerShip.getX(), currentPlayerShip.getY());
		// Move towards the player
		enemy.tick();
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
			if (!enemy.getExistance())
				continue;
			// Can't collide with self
			if (otherEnemy == enemy)
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