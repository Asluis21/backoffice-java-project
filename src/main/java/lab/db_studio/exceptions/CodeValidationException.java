package lab.db_studio.exceptions;

public class CodeValidationException extends RuntimeException{

    public CodeValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
