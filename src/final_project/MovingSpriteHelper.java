package final_project;

public class MovingSpriteHelper {
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

}
