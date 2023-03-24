package final_project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Label;

/**
 * Create a title screen
 */
public class TitleScreen extends JFrame {

    // The J "thing" that's draw onto the screen
    protected final JLabel backgroundJLabel = new JLabel();
    
    private ImageIcon backgroundPicture;
    
 // The JFrame this Sprite is part of
    protected static JFrame gameJFrame;
    
    protected TitleScreen(JFrame gameJFrame) {
	
	backgroundPicture = new ImageIcon("assets/tropical_background.jpeg");
	backgroundJLabel.setIcon(backgroundPicture);
	gameJFrame.getContentPane().add(backgroundJLabel);
	
	
	JFrame titleScreen = new JFrame("Virutal Voyagers");
    	titleScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	int width = gd.getDisplayMode().getWidth();
	int height = gd.getDisplayMode().getHeight();
    	// Set the size of the title screen
    	titleScreen.setSize((width / 2) + 50, (height / 2) + 150);
    	
    	
    	
	// Put the window in the middle of the screen.
	titleScreen.setLocationRelativeTo(null);
	
	titleScreen.setVisible(true);
    }
    
    public static void main(String[] args) {
	new TitleScreen(gameJFrame);
    }
}
