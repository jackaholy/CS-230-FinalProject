package testing;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import final_project.Cannonball;
import final_project.PlayerShip;
import final_project.Ship;

public class TestPlayerShip {
    public static void main(String[] args) {
        testCreateCannonball();
    }

    public static boolean testCreateCannonball() {
        JFrame jFrame = new JFrame();
        PlayerShip p = new PlayerShip(jFrame, new ImageIcon("../assets/water_bug.png"), 0, 0, 0, 0.0, 0.0, 0, 0);
        p.createCannonball(100, 200, new Ship[] { p });
        p.tick();
        p.tick();
        List<Cannonball> cannonballs = Publicizer.getField(p, "cannonballs");

        if (cannonballs.size() != 1) {
            System.out.println("FAILED: Expected creation of one cannonball");
            return false;
        }
        if ((int) Publicizer.getField(cannonballs.get(0), "targetX") != 100
                || (int) Publicizer.getField(cannonballs.get(0), "targetY") != 200) {
            System.out.println("FAILED: Cannonball target set incorrectly");
            return false;
        }

        System.out.println("PASSED: Cannonballs created successfully");
        return true;
    }
}
