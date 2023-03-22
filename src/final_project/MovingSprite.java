package final_project;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

/**
 * A sprite that moves towards a specific set of coordinates at a given speed
 * and rotation rate.
 */
public abstract class MovingSprite extends Sprite {
    public enum Direction {
        CLOCKWISE,
        COUNTER_CLOCKWISE,
        NOT_ROTATING
    }

    protected Direction rotationDirection = Direction.NOT_ROTATING;

    // Position of the thing to sail towards
    protected int targetX;
    protected int targetY;

    // How many pixels the sprite should move per frame
    private double speed;
    // How many degrees the sprite should rotate per frame
    private double turningSpeed;

    // If the sprite wants to move <1 pixel, save the
    // remainder for the next frame
    private double previousFrameXChangeRemainder = 0;
    private double previousFrameYChangeRemainder = 0;

    private long previousTime;
    protected float changeTime;

    /**
     * Class constructor. Sets speed and turning speed, and creates the sprite.
     * 
     * @param gameJFrame   the game window, passed to sprite
     * @param image        the image for the sprite, passed to sprite
     * @param x            starting x position
     * @param y            starting y position
     * @param speed        how many pixels the player should move per second
     * @param turningSpeed how many degrees the player should rotate per second
     */
    protected MovingSprite(JFrame gameJFrame, ImageIcon image, int x, int y, double speed,
            double turningSpeed) {
        super(gameJFrame, image, x, y);
        previousTime = System.currentTimeMillis();
        this.speed = speed;
        this.turningSpeed = turningSpeed;
    }

    protected void updateTimeChange() {
        long currentTime = System.currentTimeMillis();
        changeTime = (currentTime - previousTime) / 1000f;
        previousTime = currentTime;
    }

    /**
     * A single "moment" in game. Should rotate and move slightly, and update the UI
     */
    protected void tick() {
        updateTimeChange();
        // setRotationDirection(calculateDirectionToDesiredAngle());
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
            setRotation(getRotation() + turningSpeed * changeTime);
        } else if (rotationDirection == Direction.COUNTER_CLOCKWISE) {
            setRotation(getRotation() - turningSpeed * changeTime);
        }
    }

    /**
     * Calculate which direction to rotate in order to get closer to the destination
     * coordinates
     * 
     * @param destinationX the X position to aim for
     * @param destinationY the Y position to aim for
     */
    public void setTarget(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /**
     * Calculate the angle of the target relative to this MovingSprite.
     * 
     * @return Angle of the target relative to the MovingSprite bounded from 0-360.
     *         Such that right = 0, up = 90,
     *         left = 180,down = 270
     */
    protected double calculatedAngleToCoordinates(int xCoord, int yCoord) {
        // Calculate difference in X and Y
        double xDiff = this.getX() - xCoord;
        double yDiff = this.getY() - yCoord;

        // Calculate the angle between the sprite and the cursor
        double desiredAngle = Math.toDegrees(Math.atan2(yDiff, xDiff));
        // Restrict angle to 0 < angle < 360
        return (desiredAngle + 360) % 360;
    }

    /**
     * Return the direction to turn in order to get closer to the target angle. If
     * the target is to the moving sprite's right, will return CLOCKWISE, if to the
     * left, COUNTER_CLOCKWISE
     * 
     * @return CLOCKWISE or COUNTERCLOCKWISE
     */
    protected Direction calculateDirectionToDesiredAngle() {
        double desiredAngle = calculatedAngleToCoordinates(targetX, targetY);
        // Find the difference between current and target angle
        double angleDiff = desiredAngle - this.getRotation();

        // Force it to be positive
        if (angleDiff < 0)
            angleDiff += 360;

        // Find shorter rotation direction
        if (angleDiff > 180) {
            return MovingSprite.Direction.COUNTER_CLOCKWISE;
        } else {
            return MovingSprite.Direction.CLOCKWISE;
        }
    }

    /**
     * Returns the opposite of the given direction
     * 
     * @param given the direction to invert
     * @return the opposite direction
     */
    protected Direction oppositeDirection(Direction given) {
        if (given == Direction.COUNTER_CLOCKWISE)
            return Direction.CLOCKWISE;
        else if (given == Direction.CLOCKWISE)
            return Direction.COUNTER_CLOCKWISE;
        return Direction.NOT_ROTATING;
    }

    /**
     * Move at "speed" in angle direction
     * 
     * @param angle the angle to move in
     */
    private void move(double angle) {
        // The exact distance we want to move in each direction
        double xChangeExact = Math.cos(Math.toRadians(angle)) * speed * changeTime + previousFrameXChangeRemainder;
        double yChangeExact = Math.sin(Math.toRadians(angle)) * speed * changeTime + previousFrameYChangeRemainder;
        if (xChangeExact > 0 && !canMoveLeft())
            xChangeExact = 0;
        if (xChangeExact < 0 && !canMoveRight())
            xChangeExact = 0;
        if (yChangeExact > 0 && !canMoveUp())
            yChangeExact = 0;
        if (yChangeExact < 0 && !canMoveDown())
            yChangeExact = 0;

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

    /**
     * Move "forward" at the set speed, based on the current direction
     */
    protected void moveForward() {
        move(getRotation());
    }

    /**
     * Move away from "other"
     */
    protected void moveAway(Sprite other) {
        // Get the angle towards "other"
        double towardsAngle = calculatedAngleToCoordinates(other.getX(), other.getY());
        // Get the opposite angle
        double awayAngle = towardsAngle + 180;
        // Force it to be 0-360
        awayAngle = awayAngle % 360;

        move(awayAngle);
    }

    /**
     * Whether or not the sprite can move left without moving offscreen
     * 
     * @return boolean, whether or not the sprite can move left
     */
    private boolean canMoveLeft() {
        return (getX() - (getWidth() / 2)) > 0;
    }

    /**
     * Whether or not the sprite can move up without moving offscreen
     * 
     * @return boolean, whether or not the sprite can move up
     */

    private boolean canMoveUp() {
        return getY() - (getHeight() / 2) > 0;
    }

    /**
     * Whether or not the sprite can move right without moving offscreen
     * 
     * @return boolean, whether or not the sprite can move right
     */
    private boolean canMoveRight() {
        return getX() + (getWidth() / 2) < gameJFrame.getContentPane().getWidth();
    }

    /**
     * Whether or not the sprite can move down without moving offscreen
     * 
     * @return boolean, whether or not the sprite can move down
     */
    private boolean canMoveDown() {
        return getY() + (getHeight() / 2) < gameJFrame.getContentPane().getHeight();
    }

    public Direction getRotationDirection() {
        return rotationDirection;
    }

    protected void setRotationDirection(Direction rotationDirection) {
        this.rotationDirection = rotationDirection;
    }
}
