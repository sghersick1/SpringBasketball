package loyola.basketball.Exception;

/**
 * Exception thrown when a Player is not found in the database
 */
public class PlayerNotFoundException extends RuntimeException {
    
    public PlayerNotFoundException(String message) {
        super(message);
    }
    
    public PlayerNotFoundException(int playerId) {
        super("Player with ID " + playerId + " not found");
    }
}

