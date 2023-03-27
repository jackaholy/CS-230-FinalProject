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
 * Create a title screen
 */
@SuppressWarnings("serial")
public class TitleScreen extends JFrame {
    // Keeps track of whether the play button is pressed
    private boolean playPressed = false;

    protected TitleScreen() {
        	// The label we're adding the picture to.
        	JLabel backgroundJLabel = new JLabel();
        	// Our image
        	ImageIcon backgroundPicture = new ImageIcon("assets/tropical_titlescreen.jpeg");
        	backgroundJLabel.setIcon(backgroundPicture);
        	backgroundJLabel.setBounds(0, 0, backgroundPicture.getIconWidth(), backgroundPicture.getIconHeight());
        	getContentPane().add(backgroundJLabel);
        
        	JButton playBtn = new JButton("Play");
        	playBtn.setFont(new Font("Apple Chancery", Font.BOLD, 20));

        	playBtn.addMouseListener(new MouseAdapter() {
        
        	    public void mouseClicked(MouseEvent e) {
        		if (e.getSource() == playBtn) {
        		    playPressed = true;
        		    dispose();
        		}
        	    }
        	});
        
        	playBtn.setBounds(310, 320, 130, 50);
        	backgroundJLabel.add(playBtn);
        
        	JButton quitBtn = new JButton("Quit");
        	quitBtn.setFont(new Font("Apple Chancery", Font.BOLD, 15));
        	quitBtn.addMouseListener(new MouseAdapter() {
        	    // Close the application if clicked
        	    public void mouseClicked(MouseEvent e) {
        		System.exit(ABORT);
        	    }
        	});
        
        	quitBtn.setBounds(325, 400, 100, 40);
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
        
        	while (!this.isPlayPressed()) {
        	    try {
        		Thread.sleep(10);
        	    } catch (InterruptedException e) {
        		e.printStackTrace();
        	    }
        	}
        
            }
        
            private boolean isPlayPressed() {
        	return playPressed;
            }
}
