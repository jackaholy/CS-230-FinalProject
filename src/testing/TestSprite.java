import java.awt.Point;

import final_project.SpriteHelper;

public class TestSprite {
    public static void main(String[] args) {
        testCollidingPolygons();
        testNonCollidingPolygons();
        testGetCorners();
    }

    public static void testCollidingPolygons() {
        Point[] aPoints = { new Point(0, 0), new Point(0, 10), new Point(10, 10), new Point(10, 0) };
        Point[] bPoints = { new Point(5, 5), new Point(5, 15), new Point(15, 15), new Point(15, 5) };
        boolean result = SpriteHelper.isColliding(aPoints, bPoints);
        if (result) {
            System.out.println("PASSED: The objects collided as expected");
        } else {
            System.out.println("FAILED: The objects didn't collide like they were supposed to");
        }
    }

    public static void testNonCollidingPolygons() {
        Point[] aPoints = { new Point(0, 0), new Point(0, 10), new Point(10, 10), new Point(10, 0) };
        Point[] bPoints = { new Point(15, 15), new Point(15, 25), new Point(25, 25), new Point(25, 15) };
        boolean result = SpriteHelper.isColliding(aPoints, bPoints);
        if (!result) {
            System.out.println("PASSED: The objects didn't collide as expected");
        } else {
            System.out.println("FAILED: The objects didn't collided even though they weren't supposed to");
        }
    }

    public static void testGetCorners() {
        // Create a rectangle at position (0, 0) with width 10 and height 5, rotated 45
        // degrees
        int x = 0;
        int y = 0;
        int width = 10;
        int height = 5;
        double rotationDegrees = 45;
        Point[] corners = SpriteHelper.getCorners(x, y, width, height, rotationDegrees);

        // Check that each corner is in the correct position
        if (new Point(7, -2) != corners[0] || new Point(-2, 7) != corners[1] || new Point(-7, 2) != corners[2]
                || new Point(2, -7) != corners[3]) {
            System.out.println("FAILED: At least one of the points doesn't match up correctly");
        } else {
            System.out.println("PASSED: All of the points seem to be correct");
        }
    }
}
