package final_project;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A "thing" in the game. Should handle some basic stuff. Show/hide, movement,
 * collisions, etc.
 */
abstract public class Sprite {
    private final JLabel spriteJLabel = new JLabel();
    private ImageIcon image;
    protected int x;
    protected int y;

    public Sprite(ImageIcon image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    protected void draw() {
        spriteJLabel.setIcon(image);
        spriteJLabel.setBounds(x, y, image.getIconWidth(), image.getIconHeight());
        spriteJLabel.setVisible(true);
    }
}