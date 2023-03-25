package final_project;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;


/**
 * Create a title screen
 */
public class TitleScreen extends JFrame {

    protected TitleScreen() {
	setTitle("Virtual Voyagers");

	JLabel backgroundJLabel = new JLabel();

	ImageIcon backgroundPicture = new ImageIcon("assets/tropical_titlescreen.jpeg");
	backgroundJLabel.setIcon(backgroundPicture);
	backgroundJLabel.setBounds(0, 0, backgroundPicture.getIconWidth(), backgroundPicture.getIconHeight());
	getContentPane().add(backgroundJLabel);

	JButton playBtn = new JButton("Play");
	playBtn.addMouseListener(new MouseAdapter() {
	    
	    public void mouseClicked(MouseEvent e) {
		// If pressed, continue to the game
	    }
	});
	playBtn.setBackground(Color.BLACK);
	
	playBtn.setBounds(325, 350, 100, 30);
	backgroundJLabel.add(playBtn);
	
	JButton quitBtn = new JButton("Quit");
	quitBtn.addMouseListener(new MouseAdapter() {
	    
	    public void mouseClicked(MouseEvent e) {
		dispose();
	    }
	});
	quitBtn.setBackground(Color.BLACK);
	
	quitBtn.setBounds(325, 400, 100, 30);
	backgroundJLabel.add(quitBtn);
	

	// Get the size of the screen
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	int width = gd.getDisplayMode().getWidth();
	int height = gd.getDisplayMode().getHeight();
	// Set the size of the title screen
	setSize((width / 2) + 50, (height / 2) + 150);
	
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	// Put the window in the middle of the screen.
	setLocationRelativeTo(null);
	// Make it visible
	setVisible(true);
    }

    public static void main(String[] args) {
	new TitleScreen();
    }
}
