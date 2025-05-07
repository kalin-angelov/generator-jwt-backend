package app.exceptions;

public class EmailExistInDatabaseException extends RuntimeException{

    public EmailExistInDatabaseException(String message) {
        super(message);
    }

    public EmailExistInDatabaseException() {}

}
