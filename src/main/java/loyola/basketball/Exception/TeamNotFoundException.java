package loyola.basketball.Exception;

/**
 * Exception thrown when a Team is not found in the database
 */
public class TeamNotFoundException extends RuntimeException {
    
    public TeamNotFoundException(String message) {
        super(message);
    }
    
    public TeamNotFoundException(int teamId) {
        super("Team with ID " + teamId + " not found");
    }
}

