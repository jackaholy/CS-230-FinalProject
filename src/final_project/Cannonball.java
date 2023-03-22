package final_project;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Cannonball extends MovingSprite {
    public static final double SCALE = 1.2;
    public static final double SPEED = 1.5;
    public static final ImageIcon ICON = new ImageIcon("assets/cannonball.png");

    private final long timeOfImpact;
    private boolean isFlying = true;

    public Cannonball(JFrame gameJFrame, int x, int y, double angle) {
        super(gameJFrame, ICON, x, y, speed, 0);
        setRotation(angle);
        timeOfImpact = System.currentTimeMillis() + FLIGHT_TIME_MS;
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() > timeOfImpact) {
            isFlying = false;
            return;
        }
        Image image = ICON.getImage(); // transform it
        Image newimg = image.getScaledInstance(getWidth() + 1, getHeight() + 1, java.awt.Image.SCALE_SMOOTH); // scale
                                                                                                              // it the
                                                                                                              // smooth
                                                                                                              // way
        setIcon(new ImageIcon(newimg));
        super.tick();
    }

    public boolean isFlying() {
        return isFlying;
    }

}
