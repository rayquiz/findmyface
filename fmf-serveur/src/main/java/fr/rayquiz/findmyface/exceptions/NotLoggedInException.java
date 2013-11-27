package fr.rayquiz.findmyface.exceptions;

public class NotLoggedInException extends Exception {

    public NotLoggedInException() {
        super();
    }

    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 7538244327405720468L;

}
