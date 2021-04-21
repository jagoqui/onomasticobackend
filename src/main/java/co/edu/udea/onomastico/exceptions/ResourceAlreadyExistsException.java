package co.edu.udea.onomastico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class ResourceAlreadyExistsException extends RuntimeException{

	public ResourceAlreadyExistsException(String property, String value) {
	        super(String.format(
	            "Resource with property %s and value %s already exists." +
	            "Make sure to insert a unique value for %s",
	            property, value, property));
	    }
}
