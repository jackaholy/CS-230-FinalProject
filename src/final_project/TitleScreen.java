package final_project;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * Create a title screen
 */
public class TitleScreen extends JFrame {

    protected TitleScreen(JFrame gameJFrame) {

	JLabel backgroundJLabel = new JLabel();

	ImageIcon backgroundPicture = new ImageIcon("assets/tropical_titlescreen.jpeg");
	backgroundJLabel.setIcon(backgroundPicture);
	backgroundJLabel.setBounds(0, 0, backgroundPicture.getIconWidth(), backgroundPicture.getIconHeight());
	gameJFrame.getContentPane().add(backgroundJLabel);

	backgroundJLabel.setVisible(true);

	gameJFrame = new JFrame("Virtual Voyagers");

	gameJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	// Get the size of the screen
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	int width = gd.getDisplayMode().getWidth();
	int height = gd.getDisplayMode().getHeight();
	// Set the size of the title screen
	gameJFrame.setSize((width / 2) + 50, (height / 2) + 150);

	// Put the window in the middle of the screen.
	gameJFrame.setLocationRelativeTo(null);
	// Make it visible
	gameJFrame.setVisible(true);
    }

    public static void main(String[] args) {
	JFrame frame = new JFrame();
	new TitleScreen(frame);
    }
}
