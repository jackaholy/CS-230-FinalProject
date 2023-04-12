package final_project;

import java.awt.Color;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class Healthbar {
    private JProgressBar progressBar = new JProgressBar();

    private final int maxHealth;

    private final int yShift;

    /**
     * Create a healthbar
     * @param gameJFrame the JFrame to add the healthbar to
     * @param maxHealth the maximum amount of health
     * @param yShift how far above the parent element the sprite should be
     */
    public Healthbar(JFrame gameJFrame, int maxHealth, int yShift) {
        this.yShift = yShift;
        progressBar.setMinimum(0);
        gameJFrame.getContentPane().add(progressBar);
        this.maxHealth = maxHealth;
        progressBar.setMaximum(maxHealth);
        progressBar.setValue(maxHealth);
        progressBar.setForeground(Color.green);
        progressBar.setVisible(false);
    }

    /**
     * Set the health to be displayed by the healthbar
     * 
     * @param health an arbitrary integer value. Less than maxHealth, greater than 0
     */
    public void setHealth(int health) {
        progressBar.setValue(health);
    }

    /**
     * The (center) position of the thing we're displaying the health of
     * The healthbar will be centered on parentX and displayed above/below parentY based on yShift
     * 
     * @param parentX Center X position
     * @param parentY Center Y position
     */
    public void updatePosition(int parentX, int parentY) {
        progressBar.setBounds(parentX - progressBar.getWidth() / 2, parentY - yShift, maxHealth, 10);
    }

    /**
     * Make the healthbar visible/invisible
     * @param visibility true - visible, false - invisible
     */
    public void setVisible(boolean visibility) {
        progressBar.setVisible(visibility);
    }
}