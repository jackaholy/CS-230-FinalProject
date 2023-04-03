package final_project;

import java.awt.Point;
import java.awt.Polygon;

public class SpriteHelper {
    // This should never be instantiated
    private SpriteHelper() {
    }

    /**
     * Get the corners of a rotated rectangle given some information about it
     * 
     * @param x               The x position of the center of the rectangle
     * @param y               The y position of the center of the rectangle
     * @param rotationDegrees The rotation in degrees such that right = 0, up = 90,
     *                        left = 180,down = 270
     * @param width           The length of one side
     * @param height          The length of another side
     * @return the four corner points of the shape
     */
    public static Point[] getCorners(int x, int y, int width, int height, double rotationDegrees) {
        // The distance from the center to the edge of the hitbox
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
     * Determines whether two rotated rectangles are colliding
     * 
     * Note: Other polygons may work, but are untested and may result in unexpected
     * behavior.
     * 
     * @param aPoints four points representing the corner of one rectangle
     * @param bPoints four points representing the corner of another rectangle
     * @return boolean, whether or not the shapes are colliding
     */
    public static boolean isColliding(Point[] aPoints, Point[] bPoints) {
        // Construct Polygons from the points
        Polygon aPoly = new Polygon();
        for (Point corner : aPoints) {
            aPoly.addPoint(corner.x, corner.y);
        }
        Polygon bPoly = new Polygon();
        for (Point corner : bPoints) {
            bPoly.addPoint(corner.x, corner.y);
        }
        // Check if our polygon contains any of their corners
        for (Point corner : bPoints) {
            if (aPoly.contains(corner))
                return true;
        }
        // Check if their polygon contains any of our corners
        for (Point corner : aPoints) {
            if (bPoly.contains(corner))
                return true;
        }
        // If none of the corners are within the bounds, we're not colliding
        return false;
    }
}
