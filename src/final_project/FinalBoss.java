package final_project;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class FinalBoss extends PirateShip {
    public FinalBoss(JFrame gameJFrame, List<Loot> lootList, double speed,
            double turningSpeed, int health,
            int turnDistance) {
        super(gameJFrame, new ImageIcon("assets/images/boss.png"), lootList, speed, turningSpeed, health, turnDistance);
    }
}