package final_project;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

/**
 * A sprite that moves in a particular direction and can change direction
 * towards a target.
 */
public abstract class MovingSprite extends Sprite {
    /**
     * A direction to rotate
     */
    public enum Direction {
        /**
         * The sprite will rotate turningSpeed degrees clockwise every second when
         * tick() is called
         */
        CLOCKWISE,
        /**
         * The sprite will rotate turningSpeed degrees counter clockwise every second
         * when tick() is called
         */
        COUNTER_CLOCKWISE,
        /** The sprite will not rotate */
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
     * @param gameJFrame   the game window, passed to sprite
     * @param image        the image for the sprite, passed to sprite
     * @param x            starting x position
     * @param y            starting y position
     * @param speed        how many pixels the player should move per second
     * @param turningSpeed how many degrees the player should rotate per second
     */
    public MovingSprite(JFrame gameJFrame, ImageIcon image, int x, int y, double speed,
            double turningSpeed) {
        super(gameJFrame, image, x, y);
        previousTime = System.currentTimeMillis();
        this.speed = speed;
        this.turningSpeed = turningSpeed;
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
     * A single "moment" in game. The instance should rotate and move slightly, and
     * update the UI
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
     */
    private void rotate() {
        if (rotationDirection == Direction.CLOCKWISE) {
            setRotation(getRotation() + turningSpeed * changeTime);
        } else if (rotationDirection == Direction.COUNTER_CLOCKWISE) {
            setRotation(getRotation() - turningSpeed * changeTime);
        }
    }

    /**
     * Calculate the angle of the target relative to this MovingSprite.
     * 
     * @return Angle of the target in degrees relative to the MovingSprite bounded
     *         from 0-360.
     *         Such that right = 0, up = 90,
     *         left = 180,down = 270
     */
    protected double calculateAngleToCoordinates(int targetX, int targetY) {
        return MovingSpriteHelper.calculateAngle(x, y, targetX, targetY);
    }

    /**
     * Return the direction to turn in order to get closer to the target angle. If
     * the target is to the moving sprite's right, will return CLOCKWISE, if to the
     * left, COUNTER_CLOCKWISE
     * 
     * @return CLOCKWISE or COUNTERCLOCKWISE
     */
    protected Direction calculateDirectionToDesiredAngle() {
        double desiredAngle = calculateAngleToCoordinates(targetX, targetY);
        return MovingSpriteHelper.calculateDirectionToDesiredAngle(desiredAngle, getRotation());
    }

    /**
     * Move at "speed" in angle direction
     * 
     * @param angle the angle to move in
     */
    private void move(double angle) {
        // The exact distance we want to move in each direction
        double xChangeExact = calculateXChangeExact(angle);
        double yChangeExact = calculateYChangeExact(angle);

        xChangeExact = clampXMovement(xChangeExact);
        yChangeExact = clampYMovement(yChangeExact);

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
     * Force the xChange to keep the sprite in bounds
     * 
     * @param xChange the desired change in X
     * @return xChange if movement permitted, else 0
     */
    private double clampXMovement(double xChange) {
        if (xChange > 0 && !canMoveLeft()) {
            return 0;
        } else if (xChange < 0 && !canMoveRight()) {
            return 0;
        }
        return xChange;
    }

    /**
     * Force the yChange to keep the sprite in bounds
     * 
     * @param yChange the desired change in X
     * @return yChange if movement permitted, else 0
     */
    private double clampYMovement(double yChange) {
        if (yChange > 0 && !canMoveUp()) {
            return 0;
        } else if (yChange < 0 && !canMoveDown()) {
            return 0;
        }
        return yChange;
    }

    /**
     * The exact distance to move on the X axis this tick based on angle, speed,
     * time since previous frame, and any sub-pixel movements from the previous
     * frame.
     * 
     * @param angle the angle we want to move
     * @return the number of pixels to move in the x direction this frame
     */
    private double calculateXChangeExact(double angle) {
        return Math.cos(Math.toRadians(angle)) * speed * speedMultiplier * changeTime + previousFrameXChangeRemainder;
    }

    /**
     * The exact distance to move on the Y axis this tick based on angle, speed,
     * time since previous frame, and any sub-pixel movements from the previous
     * frame.
     * 
     * @param angle the angle we want to move
     * @return the number of pixels to move in the y direction this frame
     */
    private double calculateYChangeExact(double angle) {
        return Math.sin(Math.toRadians(angle)) * speed * speedMultiplier * changeTime + previousFrameYChangeRemainder;
    }

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
     * Move away from "other"
     * 
     * @param other The sprite to move away from
     */
    protected void moveAway(Sprite other) {
        // Get the angle towards "other"
        double towardsAngle = calculateAngleToCoordinates(other.getX(), other.getY());
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
