package final_project;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * The main file of the project. Run this one to start the project
 */
@SuppressWarnings("serial")
public class TitleScreen extends JFrame {
    
	/**
	 * The main method. Literally just creates a new game object
	 *
	 * @param args boilerplate
	 */
	public static void main(String[] args) {
	        new TitleScreen();
	}
	
	/*
	 * Create a title screen with a "Play" button and a "Quit" button. This is the
	 * first window to appear to the user when running the program. In order to
	 * continue to the main game the user must press the "Play" button. If they want
	 * to quit the game they must press the "Quit" button, or close the window.
	 */
	protected TitleScreen() {
	    	setResizable(false);
		// The label we're adding our background image to
		JLabel backgroundJLabel = new JLabel();
		// The image that we're setting to the background
		ImageIcon backgroundPicture = new ImageIcon("assets/images/tropical_titlescreen.jpeg");
		backgroundJLabel.setIcon(backgroundPicture);
		// The background image will fit the screen
		backgroundJLabel.setBounds(0, 0, backgroundPicture.getIconWidth(), backgroundPicture.getIconHeight());
		getContentPane().add(backgroundJLabel);

		JLabel gameSummaryLabel = new JLabel(
				"Sail the high seas, amass loot, upgrade your ship, and rid the sea of pirates");
		gameSummaryLabel.setFont(new Font("Apple Chancery", Font.PLAIN, 15));
		gameSummaryLabel.setBounds(165, 150, 600, 50);
		backgroundJLabel.add(gameSummaryLabel);

		// Create our play button
		JButton playBtn = new JButton("Play");
		playBtn.setFont(new Font("Apple Chancery", Font.BOLD, 20));
		playBtn.setBounds(310, 300, 130, 50);
		backgroundJLabel.add(playBtn);

		/*
		 * Check to see if the player clicks on the play button. If so, dispose of this
		 * window and continue to the main program.
		 */
		playBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == playBtn) {
					dispose();
					new GameController();
				}
			}
		});
		// Don't have a default button selected.
		playBtn.setFocusable(false);
		
		// Create our instructions button
		JButton instructionsBtn = new JButton("Instructions");
		instructionsBtn.setFont(new Font("Apple Chancery", Font.BOLD, 15));
		instructionsBtn.setBounds(310, 370, 130, 50);
		backgroundJLabel.add(instructionsBtn);

		/*
		 * Check to see if the player clicks on the instructions button. If so,
		 * move to the instructions JFrame.
		 */
		instructionsBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new Instructions();
			}
		});
		// Don't have a default button selected.
		instructionsBtn.setFocusable(false);

		// Create our quit button
		JButton quitBtn = new JButton("Quit");
		quitBtn.setFont(new Font("Apple Chancery", Font.BOLD, 15));
		quitBtn.setBounds(325, 440, 100, 40);
		backgroundJLabel.add(quitBtn);

		/*
		 * Check to see if the player clicks on the quit button. If so, close all
		 * windows and abort the program.
		 */
		quitBtn.addMouseListener(new MouseAdapter() {
			// Close the application if clicked
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(ABORT);
			}
		});
		// Don't have a default button selected.
		quitBtn.setFocusable(false);

		// Get the size of the screen
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		// Set the size of the JFrame title screen so that it's an appropriate size
		setSize((width / 2) + 50, (height / 2) + 150);
		// Set the window to the middle of the screen
		setLocationRelativeTo(null);
		// Make it visible
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
