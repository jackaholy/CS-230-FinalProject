package final_project;

public class MovingSpriteHelper {
    // This class should never be instantiated
    private MovingSpriteHelper() {
    }

    public static double calculateAwayAngle(int currentX, int currentY, int avoidX, int avoidY) {
        // Get the angle towards "other"
        double towardsAngle = calculateAngleToCoordinates(currentX, currentY, avoidX, avoidY);
        // Get the opposite angle
        double awayAngle = towardsAngle + 180;
        // Force it to be 0-360
        return awayAngle % 360;
    }

    /**
     * Calculate the angle of the target relative to this MovingSprite.
     * 
     * @return Angle of the target in degrees relative to the MovingSprite bounded
     *         from 0-360.
     *         Such that right = 0, up = 90,
     *         left = 180,down = 270
     */
    public static double calculateAngleToCoordinates(int currentX, int currentY, int compareX, int compareY) {
        // Calculate difference in X and Y
        double xDiff = currentX - compareX;
        double yDiff = currentY - compareY;

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
    public static Direction calculateDirectionToDesiredAngle(double desiredAngle, double currentAngle) {
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
}