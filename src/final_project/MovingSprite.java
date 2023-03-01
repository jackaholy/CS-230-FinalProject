package final_project;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

abstract public class MovingSprite extends Sprite {
    public enum Direction {
        CLOCKWISE,
        COUNTER_CLOCKWISE,
        NOT_ROTATING
    }

    private double speed;
    private double turningSpeed;
    private Direction rotationDirection = Direction.NOT_ROTATING;

    private double previousFrameXChangeRemainder = 0;
    private double previousFrameYChangeRemainder = 0;

    protected MovingSprite(JFrame gameJFrame, ImageIcon image, int x, int y, double speed,
            double turningSpeed) {
        super(gameJFrame, image, x, y);
        this.speed = speed;
        this.turningSpeed = turningSpeed;
    }

    protected void tick() {
        rotate();
        moveForward();
        draw();
    }

    protected void rotate() {
        if (rotationDirection == Direction.CLOCKWISE) {

            setRotation(getRotation() + turningSpeed);
        } else if (rotationDirection == Direction.COUNTER_CLOCKWISE) {
            setRotation(getRotation() - turningSpeed);
        }
    }

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

    protected void moveForward() {
        double xChangeExact = Math.cos(Math.toRadians(getRotation())) * speed + previousFrameXChangeRemainder;
        double yChangeExact = Math.sin(Math.toRadians(getRotation())) * speed + previousFrameYChangeRemainder;
        int xChangeInt = (int) xChangeExact;
        int yChangeInt = (int) yChangeExact;
        previousFrameXChangeRemainder = xChangeExact - xChangeInt;
        previousFrameYChangeRemainder = yChangeExact - yChangeInt;
        setX(getX() - xChangeInt);
        setY(getY() - yChangeInt);
    }
}
