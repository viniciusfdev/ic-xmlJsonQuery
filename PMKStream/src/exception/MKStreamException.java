package exception;

/**
 * @version 1.0
 * @author Jônatas Tonholo
 */
public class MKStreamException extends Exception {
    private String message;

    public MKStreamException(String message) {
        super(message);
    }
}
