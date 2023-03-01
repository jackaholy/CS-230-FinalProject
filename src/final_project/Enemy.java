package final_project;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

/**
 * A pirate or something. I dunno. Might try to shoot the player or ram them or
 * something?
 */
public class Enemy extends MovingSprite {
    public Enemy(JFrame gameJFrame, ImageIcon image, int x, int y, double speed, double turningSpeed) {
        super(gameJFrame, image, x, y, speed, turningSpeed);
    }
}