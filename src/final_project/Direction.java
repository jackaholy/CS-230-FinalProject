package final_project;

/**
 * A direction to rotate
 */
public enum Direction {
    /**
     * The sprite will rotate turningSpeed degrees clockwise every second when
     * tick() is called
     */
    CLOCKWISE,
    /**
     * The sprite will rotate turningSpeed degrees counter clockwise every second
     * when tick() is called
     */
    COUNTER_CLOCKWISE,
    /** The sprite will not rotate */
    NOT_ROTATING
}
