package edu.ucalgary.oop;

/**
 * This exception is thrown when the database connection could not be closed.
 *
 * @author Sergiy Redko
 * @version 1.0
 * @since 1.0
 */
public class CouldNotCloseDatabaseConnectionException extends Exception{
    /**
     * This constructor creates a new CouldNotCloseDatabaseConnectionException object.
     *
     * @param message The message to be displayed when the exception is thrown.
     */
    public CouldNotCloseDatabaseConnectionException(String message) {
        super("Could not close database connection: " + message);
    }
}
