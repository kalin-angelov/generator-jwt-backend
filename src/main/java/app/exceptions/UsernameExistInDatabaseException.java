package app.exceptions;

public class UsernameExistInDatabaseException extends RuntimeException {

    public UsernameExistInDatabaseException(String message) {
        super(message);
    }

    public UsernameExistInDatabaseException() {}
}
