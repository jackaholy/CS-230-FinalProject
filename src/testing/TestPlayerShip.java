import java.awt.Component;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

        // Assert that there are no cannonballs on screen.
        Component[] components = jFrame.getContentPane().getComponents();
        for (Component component : components) {
            System.out.println(component);
            if (component instanceof JLabel) {
                ((JLabel) component).getIcon().;
            }
        }
        
        p.tick();
        p.tick();
        return true;

    }
}
