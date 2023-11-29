package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the database.
 * @author Sergiy Redko
 * @version 1.0
 * @since 1.0
 */
public class DatabaseTest {
    /**
     * Tests that the database can be connected to.
     */
    @Test
    public void testCanConnectToDatabase() {
        boolean canConnect = true;

        SqlConnector connector = new SqlConnector("jdbc:mysql://localhost/ewr", "user1", "ensf");
        try {
            connector.createConnection();
        }
        catch (Exception e) {
            canConnect = false;
        }
        finally {
            try {
                connector.closeConnection();
            }
            catch (Exception e) {
            }
        }

        assertTrue("Could not connect to database with default credentials.", canConnect);
    }

    /**
     * Tests that an appropriate exception is thrown when the database cannot be connected to.
     */
    @Test
    public void testDatabaseConnectionThrowsException(){
        boolean threwException = false;

        SqlConnector connector = new SqlConnector("jdbc:mysql://localhost/ewr", "bobo", "wolowolo");
        try {
            connector.createConnection();
        }
        catch (CouldNotConnectToDatabaseException e) {
            threwException = true;
        }
        catch (Exception e) {
        }
        finally {
            try {
                connector.closeConnection();
            }
            catch (Exception e) {
            }
        }

        assertTrue("createConnection() did not throw CouldNotConnectToDatabaseException when provided with wrong credentials.", threwException);
    }
}
