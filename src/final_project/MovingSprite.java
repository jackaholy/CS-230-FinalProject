package final_project;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

/**
 * A sprite that moves towards a specific set of coordinates at a given speed
 * and rotation rate.
 */
abstract public class MovingSprite extends Sprite {
    public enum Direction {
        CLOCKWISE,
        COUNTER_CLOCKWISE,
        NOT_ROTATING
    }

    protected Direction rotationDirection = Direction.NOT_ROTATING;

    // How many pixels the sprite should move per frame
    private double speed;
    // How many degrees the sprite should rotate per frame
    private double turningSpeed;

    // If the sprite wants to move <1 pixel, save the
    // remainder for the next frame
    private double previousFrameXChangeRemainder = 0;
    private double previousFrameYChangeRemainder = 0;

    /**
     * Class constructor. Sets speed and turning speed, and creates the sprite.
     * 
     * @param gameJFrame   the game window, passed to sprite
     * @param image        the image for the sprite, passed to sprite
     * @param x            starting x position
     * @param y            starting y position
     * @param speed        how many pixels the player should move per frame
     * @param turningSpeed how many degrees the player should rotate per frame
     */
    protected MovingSprite(JFrame gameJFrame, ImageIcon image, int x, int y, double speed,
            double turningSpeed) {
        super(gameJFrame, image, x, y);
        this.speed = speed;
        this.turningSpeed = turningSpeed;
    }

    /**
     * A single "moment" in game. Should rotate and move slightly, and update the UI
     */
    protected void tick() {
        rotate();
        moveForward();
        draw();
    }

    /**
     * Rotate slightly in the desired direction.
     * If NOT_ROTATING, do nothing
     */
    protected void rotate() {
        if (rotationDirection == Direction.CLOCKWISE) {
            setRotation(getRotation() + turningSpeed);
        } else if (rotationDirection == Direction.COUNTER_CLOCKWISE) {
            setRotation(getRotation() - turningSpeed);
        }
    }

    /**
     * Calculate which direction to rotate in order to get closer to the destination
     * coordinates
     * 
     * @param destinationX the X position to aim for
     * @param destinationY the Y position to aim for
     */
    public void setTarget(int destinationX, int destinationY) {
        // Calculate difference in X and Y
        double xDiff = this.getX() - destinationX;
        double yDiff = this.getY() - destinationY;

        // Calculate the angle between the sprite and the cursor
        double desiredAngle = Math.toDegrees(Math.atan2(yDiff, xDiff));

        // Restrict angle to 0 < angle < 360
        desiredAngle = (desiredAngle + 360) % 360;

        // Find the difference between current and target angle
        double angleDiff = desiredAngle - this.getRotation();

        // Force it to be positive
        if (angleDiff < 0)
            angleDiff += 360;

        // Find shorter rotation direction
        if (angleDiff > 180) {
            this.rotationDirection = MovingSprite.Direction.COUNTER_CLOCKWISE;
        } else {
            this.rotationDirection = MovingSprite.Direction.CLOCKWISE;
        }
    }

    /**
     * Move "forward" at the set speed, based on the current direction
     */
    protected void moveForward() {
        // The exact distance we want to move in each direction
        double xChangeExact = Math.cos(Math.toRadians(getRotation())) * speed + previousFrameXChangeRemainder;
        double yChangeExact = Math.sin(Math.toRadians(getRotation())) * speed + previousFrameYChangeRemainder;

        // Since you can't move part of a pixel, convert to ints
        int xChangeInt = (int) xChangeExact;
        int yChangeInt = (int) yChangeExact;

        // Move the desired amount
        setX(getX() - xChangeInt);
        setY(getY() - yChangeInt);

        // Save the remaining distance for the next frame
        previousFrameXChangeRemainder = xChangeExact - xChangeInt;
        previousFrameYChangeRemainder = yChangeExact - yChangeInt;
    }
}
