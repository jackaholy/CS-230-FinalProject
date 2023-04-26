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
 * Creates a JFrame that has instructions on how to play the game.
 */

@SuppressWarnings("serial")
public class Instructions extends JFrame {

	protected Instructions() {
	    	setResizable(false);
		// The label we're adding our background image to
		JLabel backgroundJLabel = new JLabel();
		// The image that we're setting to the background
		ImageIcon backgroundPicture = new ImageIcon("assets/images/tropical_instructions.jpeg");
		backgroundJLabel.setIcon(backgroundPicture);
		// The background image will fit the screen
		backgroundJLabel.setBounds(0, 0, backgroundPicture.getIconWidth(), backgroundPicture.getIconHeight());
		getContentPane().add(backgroundJLabel);
		// Create our play button
		JButton playBtn = new JButton("Play");
		playBtn.setFont(new Font("Apple Chancery", Font.BOLD, 16));
		playBtn.setBounds(525, 515, 110, 45);
		backgroundJLabel.add(playBtn);

		/*
		 * Check to see if the player clicks on the play button. If so, dispose of this
		 * window and continue to the main program.
		 */
		playBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == playBtn) {
					dispose();
					new GameController();
				}
			}
		});
		// Don't have a default button selected.
		playBtn.setFocusable(false);

		// Create our quit button
		JButton quitBtn = new JButton("Quit");
		quitBtn.setFont(new Font("Apple Chancery", Font.BOLD, 16));
		quitBtn.setBounds(650, 515, 110, 45);
		backgroundJLabel.add(quitBtn);

		/*
		 * Check to see if the player clicks on the quit button. If so, close all
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
