package final_project;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.Font;

/**
 * Create a victory screen with a "Play Again?" button and a "Quit?" button. This is the
 * window that will appear shortly after the user defeats the final boss. In order to
 * play the game again the user must press the "Play Again?" button. If they want
 * to quit the game they must press the "Quit?" button, or close the window.
 */

@SuppressWarnings("serial")
public class VictoryScreen extends JFrame {

	protected VictoryScreen() {
		// The label we're adding our background image to
		JLabel backgroundJLabel = new JLabel();
		// The image that we're setting to the background
		ImageIcon backgroundPicture = new ImageIcon("assets/images/tropical_victory.jpeg");
		backgroundJLabel.setIcon(backgroundPicture);
		// The background image will fit the screen
		backgroundJLabel.setBounds(0, 0, backgroundPicture.getIconWidth(), backgroundPicture.getIconHeight());
		getContentPane().add(backgroundJLabel);
		// Create our playAgain button
		JButton playAgainBtn = new JButton("Play Again?");
		playAgainBtn.setFont(new Font("Apple Chancery", Font.BOLD, 18));
		playAgainBtn.setBounds(325, 320, 130, 50);
		backgroundJLabel.add(playAgainBtn);

	/*
	 * Check to see if the player clicks on the playAgainBtn. If so, dispose of this
	 * window and continue to the main program.
	 */
	playAgainBtn.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		if (e.getSource() == playAgainBtn) {
		    dispose();
		    new GameController();
		}
	    }
	});
	// Don't have a default button selected.
	playAgainBtn.setFocusable(false);

	// Create our quit button
	JButton quitBtn = new JButton("Quit?");
	quitBtn.setFont(new Font("Apple Chancery", Font.BOLD, 18));
	quitBtn.setBounds(325, 400, 130, 50);
	backgroundJLabel.add(quitBtn);

	/*
	 * Check to see if the player clicks on the quitBtn. If so, close all
	 * windows and abort the program.
	 */
	quitBtn.addMouseListener(new MouseAdapter() {
	    // Close the application if clicked
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
