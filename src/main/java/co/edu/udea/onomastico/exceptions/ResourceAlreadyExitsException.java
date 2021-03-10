package co.edu.udea.onomastico.exceptions;

public class ResourceAlreadyExitsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExitsException(String message) {
        super(message);
    }

    public ResourceAlreadyExitsException(String message, Throwable cause) {
        super(message, cause);
    }
}
