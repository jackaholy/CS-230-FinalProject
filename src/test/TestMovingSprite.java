package test;

import final_project.MovingSpriteHelper;

public class TestMovingSprite {
    public static void main(String[] args) {
        testCalculateAngle();
    }

    public static boolean testCalculateAngle() {
        double result = MovingSpriteHelper.calculateAngle(0, 0, 10, 0);
        double expectation = 0.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }
        System.out.println("PASSED: Angle falls within expectations");

        result = MovingSpriteHelper.calculateAngle(0, 0, 0, 10);
        expectation = 90.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }
        System.out.println("PASSED: Angle falls within expectations");

        result = MovingSpriteHelper.calculateAngle(0, 0, 10, 10);
        expectation = 45.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }
        System.out.println("PASSED: Angle falls within expectations");

        result = MovingSpriteHelper.calculateAngle(0, 0, -10, 0);
        expectation = 180.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }
        System.out.println("PASSED: Angle falls within expectations");

        result = MovingSpriteHelper.calculateAngle(0, 0, 0, -10);
        expectation = 270.0;
        if (Math.abs(expectation - result) < 0.001) {
            System.out.println("FAILED: Angle different than expected");
            return false;
        }
        System.out.println("PASSED: Angle falls within expectations");
        return true;
    }
}
