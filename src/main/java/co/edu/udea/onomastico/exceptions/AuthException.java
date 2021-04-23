package co.edu.udea.onomastico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AuthException() {
		super();
	 }
	public AuthException(String message) {
		super(message);
	}
	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}
}
