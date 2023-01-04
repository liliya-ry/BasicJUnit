package errors;

public class AssertionFailedError extends Error {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "expected <%s> but was: <%s>";
    private static final String DEFAULT_NOT_NULL_MESSAGE = "expected not <null>";

    private final String message;

    public AssertionFailedError(Object expected, Object actual, String message) {
        String defaultMsg = String.format(DEFAULT_MESSAGE_TEMPLATE, expected, actual);
        this.message = message == null ? defaultMsg : message + " ==> " + defaultMsg;
    }

    public AssertionFailedError(String message) {
        this.message = message == null ? DEFAULT_NOT_NULL_MESSAGE : message + " ==> " + DEFAULT_NOT_NULL_MESSAGE;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
