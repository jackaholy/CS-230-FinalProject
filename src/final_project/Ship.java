package final_project;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Ship extends MovingSprite {
    private List<Cannonball> cannonballs = new ArrayList<>();

    public Ship(JFrame gameJFrame, ImageIcon image, int x, int y, double speed, double turningSpeed) {
        super(gameJFrame, image, x, y, speed, turningSpeed);
    }

    @Override
    protected void tick() {
        super.tick();
        for (Cannonball cannonBall : cannonballs) {
            cannonBall.tick();
        }
    }

    public void createCannonball(int targetX, int targetY) {
        double targetAngle = calculatedAngleToCoordinates(targetX, targetY);
        cannonballs.add(new Cannonball(gameJFrame, x, y, targetAngle));
    }
}
