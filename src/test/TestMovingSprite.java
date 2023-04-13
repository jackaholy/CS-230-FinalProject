package test;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import final_project.MovingSprite;
import final_project.MovingSpriteHelper;
import final_project.Sprite;
import final_project.MovingSprite.Direction;

/**
 * Unit tests for MovingSprites. Much of this code has been written at least in
 * part by ChatGPT
 */
public class TestMovingSprite {
    public static void main(String[] args) {
        testCalculateAngle();
        testCalculateDirectionToAngle();
        testOppositeDirection();
        testMoveForward();
        testMoveAway();
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

    public static boolean testMoveForward() {
        JFrame jframe = new JFrame();
        jframe.setBounds(0, 0, 100, 100);
        TestableMovingSprite sprite = new TestableMovingSprite(jframe);

        // Move the sprite at a 45 degree angle (xChangeExact and yChangeExact should be
        // approximately 3.5355)
        sprite.setRotation(45);
        sprite.moveForward();

        // Check that the sprite's position has changed appropriately
        if (sprite.getX() != -3 || sprite.getY() != -3) {
            System.out.println("FAILED: Sprite position did not change as expected");
            return false;
        }

        // Move the sprite at a 90 degree angle (xChangeExact should be 0 and
        // yChangeExact should be approximately 5)
        sprite.setRotation(90);
        sprite.moveForward();

        // Check that the sprite's position has changed appropriately
        if (sprite.getX() != -3 || sprite.getY() != -8) {
            System.out.println("FAILED: Sprite position did not change as expected");
            return false;
        }

        // Move the sprite at a 180 degree angle (xChangeExact should be approximately 5
        // and yChangeExact should be 0)
        sprite.setRotation(180);
        sprite.moveForward();

        // Check that the sprite's position has changed appropriately
        if (sprite.getX() != 1 || sprite.getY() != -8) {
            System.out.println("FAILED: Sprite position did not change as expected");
            return false;
        }

        // Move the sprite at a 270 degree angle (xChangeExact should be 0 and
        // yChangeExact should be approximately -5)
        sprite.setRotation(270);
        sprite.moveForward();

        // Check that the sprite's position has changed appropriately
        if (sprite.getX() != 1 || sprite.getY() != -4) {
            System.out.println("FAILED: Sprite position did not change as expected");
            return false;
        }

        // If all tests passed, return true
        System.out.println("PASSED: All tests passed for move");
        return true;
    }

    public static boolean testMoveAway() {
        JFrame jframe = new JFrame();
        jframe.setBounds(0, 0, 100, 100);
        TestableMovingSprite sprite1 = new TestableMovingSprite(jframe);
        TestableMovingSprite sprite2 = new TestableMovingSprite(jframe);

        // Test 1: Sprites are horizontally aligned
        sprite1.setX(0);
        sprite1.setY(0);
        sprite2.setX(1);
        sprite2.setY(0);
        sprite2.moveAway(sprite1);
        if (sprite2.getX() <= 1 || sprite2.getY() != 0) {
            System.out.println("FAILED: Test 1 - Sprite didn't move or moved in an unexpected way!");
            return false;
        }

        // Test 2: Sprites are vertically aligned
        sprite1.setX(0);
        sprite1.setY(0);
        sprite2.setX(0);
        sprite2.setY(1);
        sprite2.moveAway(sprite1);
        if (sprite2.getX() != 0 || sprite2.getY() <= 1) {
            System.out.println("FAILED: Test 2 - Sprite didn't move or moved in an unexpected way!");
            return false;
        }

        // Test 3: Sprites are at a diagonal
        sprite1.setX(0);
        sprite1.setY(0);
        sprite2.setX(1);
        sprite2.setY(1);
        sprite2.moveAway(sprite1);
        if (sprite2.getX() <= 1 || sprite2.getY() <= 1) {
            System.out.println("FAILED: Test 3 - Sprite didn't move or moved in an unexpected way!");
            return false;
        }

        // Test 4: Sprites are very far apart
        sprite1.setX(0);
        sprite1.setY(0);
        sprite2.setX(100);
        sprite2.setY(100);
        sprite2.moveAway(sprite1);
        if (sprite2.getX() <= 100 || sprite2.getY() <= 100) {
            System.out.println("FAILED: Test 4 - Sprite didn't move or moved in an unexpected way!");
            return false;
        }

        // Test 5: Sprites are overlapping
        sprite1.setX(0);
        sprite1.setY(0);
        sprite2.setX(0);
        sprite2.setY(0);
        sprite2.moveAway(sprite1);
        if (sprite2.getX() == 0 && sprite2.getY() == 0) {
            System.out.println("FAILED: Test 5 - Sprite didn't move or moved in an unexpected way!");
            return false;
        }
        System.out.println("PASSED: Sprites move away successfully");

        return true;
    }

    /**
     * An implementation of MovingSprite for testing purposes.
     */
    private static class TestableMovingSprite extends MovingSprite {
        public TestableMovingSprite(JFrame jframe) {
            super(jframe, new ImageIcon(), 0, 0, 5, 0);
        }

        public void moveForward() {
            changeTime = 1;
            super.moveForward();
        }

        public void moveAway(Sprite other) {
            changeTime = 1;
            super.moveAway(other);
        }

        @Override
        protected boolean canMoveDown() {
            return true;
        }

        @Override
        protected boolean canMoveLeft() {
            return true;
        }

        @Override
        protected boolean canMoveRight() {
            return true;
        }

        @Override
        protected boolean canMoveUp() {
            return true;
        }
    }

}
