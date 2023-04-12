package final_project;

import java.awt.Point;
import java.awt.Polygon;

public class SpriteHelper {
    // This shouldn't be instantiated
    private SpriteHelper(){};

    public static boolean isColliding(Point[] rect1, Point[] rect2){
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
