package final_project;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * This class will probably do some heavy lifting. Controls, player inventory,
 * ship speed, health and so on will all go here
 */
public class Player extends Sprite {
  // directions our boat should be allowed to move
	enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}

	protected final JFrame jFrame = new JFrame();
	protected final JLabel playerJLabel;
  
  // these are each of our boat levels with an image. jpg's still need to added
	private static final ImageIcon waterBug = new ImageIcon("waterBug.jpg");
	private static final ImageIcon floatingPoint = new ImageIcon("floatingPoint.jpg");
	private static final ImageIcon byteMe = new ImageIcon("byteMe.jpg");
	private static final ImageIcon seaPlusPlus = new ImageIcon("sea++.jpg");
	private static final ImageIcon worldWideWet = new ImageIcon("worldWideWet.jpg");

	protected Map<Direction, String> boatImageNames = new HashMap<>(Direction.values().length);
	protected Map<Direction, ImageIcon> boatImages = new HashMap<>(Direction.values().length);

	protected Direction boatDirection;

	protected int xPosition = 0;
	protected int yPosition = 0;

	public Player(JFrame playerJFrame) {
		playerJLabel = new JLabel();

		Random randomPosition = new Random();

		// arbitrary starting point
		xPosition = randomPosition.nextInt(playerJFrame.getWidth());
		yPosition = randomPosition.nextInt(playerJFrame.getHeight());
	}
  // draw the player with their boat.
	protected void drawPlayer() {
		final ImageIcon icon = boatImages.get(boatDirection);
		playerJLabel.setIcon(icon);
		playerJLabel.setBounds(xPosition, yPosition, icon.getIconWidth(), icon.getIconHeight());
		playerJLabel.setVisible(true);
	}

}
