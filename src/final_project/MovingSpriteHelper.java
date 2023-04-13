package final_project;

import final_project.MovingSprite.Direction;

/**
 * Helper functions for MovingSprite. Logic should be stateless and testable.
 * Logic moved from MovingSprite with ChatGPT doing a some of the refactoring.
 */
public class MovingSpriteHelper {
    // This class should never be instantiated
    private MovingSpriteHelper() {
    }

    /**
     * Calculate the angle of the target relative to current position.
     * 
     * @return Angle of the target in degrees relative to the MovingSprite bounded
     *         from 0-360.
     *         Such that right = 0, up = 90,
     *         left = 180,down = 270
     */
    public static double calculateAngle(int currentX, int currentY, int targetX, int targetY) {
        // Calculate difference in X and Y
        double xDiff = currentX - targetX;
        double yDiff = currentY - targetY;

        // Calculate the angle between the sprite and the cursor
        double desiredAngle = Math.toDegrees(Math.atan2(yDiff, xDiff));
        // Restrict angle to 0 < angle < 360
        return (desiredAngle + 360) % 360;
    }

    public static Direction calculateDirectionToDesiredAngle(double desiredAngle, double currentAngle) {
        if (currentAngle == desiredAngle)
            return Direction.NOT_ROTATING;

        // Find the difference between current and target angle
        double angleDiff = desiredAngle - currentAngle;

        // Force it to be positive
        if (angleDiff < 0)
            angleDiff += 360;

        // Find shorter rotation direction
        if (angleDiff > 180) {
            return Direction.COUNTER_CLOCKWISE;
        } else {
            return Direction.CLOCKWISE;
        }
    }

    /**
     * Returns the opposite of the given direction
     * 
     * @param given the direction to invert
     * @return the opposite direction
     */
    public static Direction oppositeDirection(Direction given) {
        if (given == Direction.COUNTER_CLOCKWISE)
            return Direction.CLOCKWISE;
        else if (given == Direction.CLOCKWISE)
            return Direction.COUNTER_CLOCKWISE;
        return Direction.NOT_ROTATING;
    }

    /**
     * Returns the opposite angle of the given angle
     * 
     * @param angle the angle to invert
     * @return the opposite angle
     */
    public static double oppositeAngle(double angle) {
        return (angle + 180) % 360;
    }

}
