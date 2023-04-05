package final_project;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class Healthbar {
    private JProgressBar progressBar = new JProgressBar();

    private final int startingHealth;

    private final int yShift;

    public Healthbar(JFrame gameJFrame, int startingHealth, int yShift) {
        this.yShift = yShift;
        progressBar.setMinimum(0);
        gameJFrame.getContentPane().add(progressBar);
        this.startingHealth = startingHealth;
        progressBar.setMaximum(startingHealth);
        progressBar.setValue(startingHealth);
        progressBar.setVisible(false);
    }

    public void setHealth(int health) {
        progressBar.setValue(health);
    }

    public void updatePosition(int parentX, int parentY) {
        progressBar.setBounds(parentX - progressBar.getWidth() / 2, parentY - yShift, startingHealth, 10);
    }

    public void setVisible(boolean visibility) {
        progressBar.setVisible(visibility);
    }
}