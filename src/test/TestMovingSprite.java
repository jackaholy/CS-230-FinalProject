package test;

import final_project.MovingSpriteHelper;
import final_project.MovingSprite.Direction;

public class TestMovingSprite {
    public static void main(String[] args) {
        testCalculateAngle();
        testCalculateDirectionToAngle();
        testOppositeDirection();
    }

    public static boolean testCalculateAngle() {
        double result = MovingSpriteHelper.calculateAngle(0, 0, 10, 0);
        double expectation = 0.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }

        result = MovingSpriteHelper.calculateAngle(0, 0, 0, 10);
        expectation = 90.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }

        result = MovingSpriteHelper.calculateAngle(0, 0, 10, 10);
        expectation = 45.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }

        result = MovingSpriteHelper.calculateAngle(0, 0, -10, 0);
        expectation = 180.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }

        result = MovingSpriteHelper.calculateAngle(0, 0, 0, -10);
        expectation = 270.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }
        System.out.println("PASSED: All angle calculations within expectations");
        return true;
    }

    public static boolean testCalculateDirectionToAngle() {
        // Test case 1: clockwise rotation
        Direction result = MovingSpriteHelper.calculateDirectionToDesiredAngle(30, 300);
        Direction expectation = Direction.CLOCKWISE;
        if (result != expectation) {
            System.out.println("FAILED: Direction different than expected");
            return false;
        }

        // Test case 2: counter-clockwise rotation
        result = MovingSpriteHelper.calculateDirectionToDesiredAngle(300, 30);
        expectation = Direction.COUNTER_CLOCKWISE;
        if (result != expectation) {
            System.out.println("FAILED: Direction different than expected");
            return false;
        }

        // Test case 3: no rotation required
        result = MovingSpriteHelper.calculateDirectionToDesiredAngle(90, 90);
        expectation = Direction.NOT_ROTATING; // no direction required
        if (result != expectation) {
            System.out.println("FAILED: Direction different than expected");
            return false;
        }

        // All tests passed
        System.out.println("PASSED: All direction tests pass");
        return true;
    }

    public static boolean testOppositeDirection() {
        Direction clockwise = Direction.CLOCKWISE;
        Direction counterClockwise = Direction.COUNTER_CLOCKWISE;
        Direction notRotating = Direction.NOT_ROTATING;

        // Test that CLOCKWISE returns COUNTER_CLOCKWISE
        if (MovingSpriteHelper.oppositeDirection(clockwise) != counterClockwise) {
            System.out.println("FAILED: oppositeDirection(CLOCKWISE) did not return COUNTER_CLOCKWISE");
            return false;
        }

        // Test that COUNTER_CLOCKWISE returns CLOCKWISE
        if (MovingSpriteHelper.oppositeDirection(counterClockwise) != clockwise) {
            System.out.println("FAILED: oppositeDirection(COUNTER_CLOCKWISE) did not return CLOCKWISE");
            return false;
        }

        // Test that NOT_ROTATING returns NOT_ROTATING
        if (MovingSpriteHelper.oppositeDirection(notRotating) != notRotating) {
            System.out.println("FAILED: oppositeDirection(NOT_ROTATING) did not return NOT_ROTATING");
            return false;
        }

        System.out.println("PASSED: All tests passed for oppositeDirection()");
        return true;
    }

}
