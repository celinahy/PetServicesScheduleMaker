package edu.ucalgary.oop;

/**
 * An exception that is thrown when a schedule cannot be created.
 * @author Nicole Heather
 * @version 1.0
 * @since 1.0
 */
public class CannotCreateScheduleException extends Exception {
    /**
     * Constructor for CannotCreateScheduleException.
     * @param message is the message to be displayed when the exception is thrown.
     */
    public CannotCreateScheduleException(String message){
        super("Cannot create schedule: " + message);
    }
}
