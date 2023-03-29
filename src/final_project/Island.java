package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import final_project.MovingSprite.Direction;

/**
 * An island. Should probably deal with collisions, trading?
 */
public class Island extends Sprite {
    
    protected Island(JFrame gameJFrame, ImageIcon image, int x, int y) {
	super(gameJFrame, image, x, y);
    }
    
}