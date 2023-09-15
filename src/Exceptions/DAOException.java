package Exceptions;
//
public class DAOException extends Exception {

    public DAOException(String message) {
        super(message);
    }

public static class DuplicateException extends DAOException {
    public DuplicateException(String message, Throwable cause) {
        super(message);

    }
}
}
