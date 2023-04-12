package final_project;

import java.awt.Point;
import java.awt.Polygon;

public class SpriteHelper {
    // This shouldn't be instantiated
    private SpriteHelper() {
    };

    /**
     * Get the coordinates of the corners of a rotated rectangle.
     * 
     * @param x               the x-coordinate of the center of the rectangle
     * @param y               the y-coordinate of the center of the rectangle
     * @param width           the width of the rectangle
     * @param height          the height of the rectangle
     * @param rotationDegrees the rotation angle of the rectangle in degrees
     * @return an array of four Point objects representing the corners of the
     *         rectangle
     */
    public static Point[] getRotatedRectangleCorners(int x, int y, int width, int height, double rotationDegrees) {
        // The distance from the center to the edge of the rectangle
        double halfWidth = ((double) width / 2);
        double halfHeight = ((double) height / 2);
        // 0 if horizontal, 1 if vertical
        double sin = Math.sin(Math.toRadians(rotationDegrees));
        // 1 if horizontal, 0 if vertical
        double cos = Math.cos(Math.toRadians(rotationDegrees));

        // Construct each of the points
        Point[] corners = new Point[4];
        corners[0] = new Point(
                (int) (x + halfWidth * cos + halfHeight * sin),
                (int) (y + halfWidth * sin - halfHeight * cos));
        corners[1] = new Point(
                (int) (x - halfWidth * cos + halfHeight * sin),
                (int) (y - halfWidth * sin - halfHeight * cos));
        corners[2] = new Point(
                (int) (x - halfWidth * cos - halfHeight * sin),
                (int) (y - halfWidth * sin + halfHeight * cos));
        corners[3] = new Point(
                (int) (x + halfWidth * cos - halfHeight * sin),
                (int) (y + halfWidth * sin + halfHeight * cos));
        return corners;
    }

    /**
     * Check whether two rotated rectangles (represented as an array of corner
     * points) are overlapping.
     * Non-rectangles may work, but are untested and not supported.
     * 
     * @param rect1 the corner points of one rectangle
     * @param rect2 the corner points of the other rectangle
     * @return true if they overlap
     */
    public static boolean isColliding(Point[] rect1, Point[] rect2) {
        Polygon rect1Poly = new Polygon();
        for (Point corner : rect1) {
            rect1Poly.addPoint(corner.x, corner.y);
        }
        Polygon rect2Poly = new Polygon();
        for (Point corner : rect2) {
            rect2Poly.addPoint(corner.x, corner.y);
        }
        // Check if our polygon contains any of their corners
        for (Point corner : rect1) {
            if (rect2Poly.contains(corner))
                return true;
        }
        // Check if their polygon contains any of our corners
        for (Point corner : rect2) {
            if (rect1Poly.contains(corner))
                return true;
        }
        // If none of the corners are within the bounds, we're not colliding
        return false;
    }
}
