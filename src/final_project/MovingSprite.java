package final_project;

import javax.swing.JFrame;
import javax.swing.Icon;

/**
 * A sprite that moves in a particular direction and can change direction
 * towards a target.
 */
public abstract class MovingSprite extends Sprite {
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

    // The last time tick was called currentTimeMillis()
    private long previousTime;

    // How many milliseconds since tick() was called
    protected float changeTime;

    // How fast to move relative to base speed.
    // 0.5 = half speed
    private double speedMultiplier = 1;

    /**
     * Class constructor. Sets speed and turning speed, and creates the sprite.
     * 
     * @param gameJFrame   the game window, passed to
     *                     {@link Sprite#Sprite(JFrame, javax.swing.Icon, int, int)}
     * @param image        the image for the sprite, passed to
     *                     {@link Sprite#Sprite(JFrame, javax.swing.Icon, int, int)}
     * @param x            starting x position
     * @param y            starting y position
     * @param speed        how many pixels the player should move per second
     * @param turningSpeed how many degrees the player should rotate per second
     */
    public MovingSprite(JFrame gameJFrame, Icon image, int x, int y, double speed,
            double turningSpeed) {
        super(gameJFrame, image, x, y);
        previousTime = System.currentTimeMillis();
        this.speed = speed;
        this.turningSpeed = turningSpeed;
        this.targetX = x;
        this.targetY = y;
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
     * Calculate how much time has passed since the previous tick
     */
    protected void updateTimeChange() {
        long currentTime = System.currentTimeMillis();
        changeTime = (currentTime - previousTime) / 1000f;
        if (changeTime >= 1)
            // It's been more than 1 second since the previous frame. Bad things are
            // happening!
            changeTime = 0;
        previousTime = currentTime;
    }

    /**
     * A single "moment" in game. The instance should rotate and move based on time
     * elapsed since the previous tick and the speed, and
     * redraw
     */
    protected void tick() {
        if (!exists)
            return;
        updateTimeChange();
        rotate();
        moveForward();
        draw();
    }

    /**
     * Rotate turningSpeed degrees per second in the desired direction.
     * If NOT_ROTATING, do nothing
     * 
     * Should be called after {@link MovingSprite#updateTimeChange()}
     */
    private void rotate() {
        if (rotationDirection == Direction.CLOCKWISE) {
            setRotation(getRotation() + turningSpeed * changeTime);
        } else if (rotationDirection == Direction.COUNTER_CLOCKWISE) {
            setRotation(getRotation() - turningSpeed * changeTime);
        }
    }

    /**
     * Move at "speed" in angle direction
     * 
     * @param angle the angle to move in
     */
    private void move(double angle) {
        // The exact distance we want to move in each direction
        double xChangeExact = Math.cos(Math.toRadians(angle)) * speed * speedMultiplier * changeTime
                + previousFrameXChangeRemainder;
        double yChangeExact = Math.sin(Math.toRadians(angle)) * speed * speedMultiplier * changeTime
                + previousFrameYChangeRemainder;

        if (xChangeExact > 0 && !canMoveLeft())
            xChangeExact = 0;
        else if (xChangeExact < 0 && !canMoveRight())
            xChangeExact = 0;

        if (yChangeExact > 0 && !canMoveUp())
            yChangeExact = 0;
        else if (yChangeExact < 0 && !canMoveDown())
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
     * 
     * @param speedMultiplier
     */
    protected void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    /**
     * Move "forward" at the set speed, based on the current direction
     */
    protected void moveForward() {
        move(getRotation());
    }

    /**
     * Move away from "other" at "speed", such as in the case of a collision
     * 
     * @param other The sprite to move away from
     */
    protected void moveAway(Sprite other) {
        double awayAngle = MovingSpriteHelper.calculateAwayAngle(x, y, other.getX(), other.getY());
        move(awayAngle);
    }

    /**
     * Whether or not the sprite can move left without moving offscreen
     * 
     * @return boolean, whether or not the sprite can move left
     */
    protected boolean canMoveLeft() {
        return (getX() - (getWidth() / 2)) > 0;
    }

    /**
     * Whether or not the sprite can move up without moving offscreen
     * 
     * @return boolean, whether or not the sprite can move up
     */

    protected boolean canMoveUp() {
        return getY() - (getHeight() / 2) > 0;
    }

    /**
     * Whether or not the sprite can move right without moving offscreen
     * 
     * @return boolean, whether or not the sprite can move right
     */
    protected boolean canMoveRight() {
        return getX() + (getWidth() / 2) < gameJFrame.getContentPane().getWidth();
    }

    /**
     * Whether or not the sprite can move down without moving offscreen
     * 
     * @return boolean, whether or not the sprite can move down
     */
    protected boolean canMoveDown() {
        return getY() + (getHeight() / 2) < gameJFrame.getContentPane().getHeight();
    }

    /**
     * Gets the direction this sprite is currently rotating
     * 
     * @return
     */
    public Direction getRotationDirection() {
        return rotationDirection;
    }

    /**
     * Specifies the direction this sprite should rotate every time rotate() is
     * called.
     * 
     * @param rotationDirection
     */
    protected void setRotationDirection(Direction rotationDirection) {
        this.rotationDirection = rotationDirection;
    }
}
