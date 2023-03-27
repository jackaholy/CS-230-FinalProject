package final_project;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * An island. Should probably deal with collisions, trading?
 */
public class Island extends Sprite {
    
//    {"../assets/island_down.png", "../assets/island_up.png"};
    ImageIcon[] img = new ImageIcon[3];
    

    protected Island(JFrame gameJFrame, ImageIcon image) {
	super(gameJFrame, image, 0, 0);
	
	 // Subtract 40 so islands don't spawn too close to the content pane border.
	this.x = getRandomX(20, gameJFrame.getContentPane().getWidth() - 20);
        this.y = getRandomY(20, gameJFrame.getContentPane().getHeight() - 20);
    }

    /**
     * Set the x coordinate of the loot to a random x value in the window.
     * 
     * @param min - the minimum x coordinate value that loot could spawn
     * @param max - the maximum x coordinate value that loot could spawn
     */
    private ImageIcon[] getRandomIslandImage(ImageIcon image) {
       return img;
    }
    
    /**
     * Set the x coordinate of the loot to a random x value in the window.
     * 
     * @param min - the minimum x coordinate value that loot could spawn
     * @param max - the maximum x coordinate value that loot could spawn
     */
    private int getRandomX(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Set the y coordinate of the loot to a random y value in the window.
     * 
     * @param min - the minimum y coordinate value that loot could spawn
     * @param max - the maximum y coordinate value that loot could spawn
     */
    private int getRandomY(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    
}