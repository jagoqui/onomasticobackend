package co.edu.udea.onomastico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class CredentialsException  extends RuntimeException {
		
		private static final long serialVersionUID = 1L;

		public CredentialsException() {
			super();
		 }
		public CredentialsException(String message) {
			super(message);
		}
		public CredentialsException(String message, Throwable cause) {
			super(message, cause);
		}
	}
