package co.edu.udea.onomastico.exceptions;

import java.util.Date;

public class ExceptionResponse {
    private Date timestamp;
    private String message;

    public ExceptionResponse(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }
}