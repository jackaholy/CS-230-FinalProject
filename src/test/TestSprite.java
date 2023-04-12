package test;

import java.awt.Point;
import final_project.SpriteHelper;

public class TestSprite {
    public static void main(String[] args) {
        testIsColliding();
    }

    public static boolean testIsColliding() {
        // Non-overlapping rectangles
        Point[] nonOverlappingRect1 = { new Point(0, 0), new Point(0, 2), new Point(2, 2), new Point(2, 0) };
        Point[] nonOverlappingRect2 = { new Point(3, 3), new Point(3, 5), new Point(5, 5), new Point(5, 3) };
        if (SpriteHelper.isColliding(nonOverlappingRect1, nonOverlappingRect2)) {
            System.out.println("FAILED: Non overlapping rectangles collided");
            return false;
        }
        // Overlapping rectangles
        Point[] overlappingRect1 = { new Point(0, 0), new Point(0, 2), new Point(2, 2), new Point(2, 0) };
        Point[] overlappingRect2 = { new Point(1, 1), new Point(1, 3), new Point(3, 3), new Point(3, 1) };
        if (!SpriteHelper.isColliding(overlappingRect1, overlappingRect2)) {
            System.out.println("FAILED: Overlapping rectangles didn't collide");
            return false;
        }
        // Identical rectangles
        Point[] indenticalRect1 = {new Point(0, 0), new Point(0, 2), new Point(2, 2), new Point(2, 0)};
        Point[] indenticalRect2 = {new Point(0, 0), new Point(0, 2), new Point(2, 2), new Point(2, 0)};
        if (!SpriteHelper.isColliding(indenticalRect1, indenticalRect2)){
            System.out.println("FAILED: Identical rectangles didn't collide");
            return false;
        }
        // One rectangle inside the other
        Point[] outerRect = {new Point(0, 0), new Point(0, 4), new Point(4, 4), new Point(4, 0)};
        Point[] innerRect = {new Point(1, 1), new Point(1, 2), new Point(2, 2), new Point(2, 1)};
        if (!SpriteHelper.isColliding(innerRect, outerRect)){
            System.out.println("FAILED: Rectangles inside each other didn't collide");
            return false;
        }
        System.out.println("PASSED: Collisions seem to be working as expected");
        return true;
    }
}
