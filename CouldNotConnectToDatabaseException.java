package edu.ucalgary.oop;

/**
 * This exception is thrown when the database connection could not be established.
 *
 * @author Sergiy Redko
 * @version 1.0
 * @since 1.0
 */
public class CouldNotConnectToDatabaseException extends Exception{
    /**
     * This constructor creates a new CouldNotConnectToDatabaseException object.
     *
     * @param message The message to be displayed when the exception is thrown.
     */
    public CouldNotConnectToDatabaseException(String message) {
        super("Could not connect to database: " + message);
    }
}
