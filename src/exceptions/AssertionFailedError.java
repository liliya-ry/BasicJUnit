package exceptions;

public class AssertionFailedError extends Error {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "expected <%s> but was: <%s>";
    private static final String DEFAULT_NOT_NULL_MESSAGE = "expected not <null>";
    private static final String DEFAULT_EXCEPTIONS_MESSAGE_TEMPLATE = "Unexpected exception type thrown, ";
    private static final String DEFAULT_NO_EXCEPTION_MESSAGE = " Expected %s to be thrown, but nothing was thrown.";
    private static final String DEFAULT_EXCEEDED_DURATION_MESSAGE = "execution exceeded timeout of %d ms by %d ms";
    private static final String DEFAULT_DURATION_MESSAGE = "execution timed out after %s ms";
    private static final String ARROW = " ==> ";

    private final String message;

    public AssertionFailedError(Object expected, Object actual, String message) {
        String defaultMsg = String.format(DEFAULT_MESSAGE_TEMPLATE, expected, actual);
        this.message = message.isEmpty() ? defaultMsg : message + ARROW + defaultMsg;
    }

    public AssertionFailedError(String message) {
        this.message = message.isEmpty() ? DEFAULT_NOT_NULL_MESSAGE : message + ARROW + DEFAULT_NOT_NULL_MESSAGE;
    }

    public AssertionFailedError(Class<?> expected, Class<?> actual, String message) {
        String defaultMsg = DEFAULT_EXCEPTIONS_MESSAGE_TEMPLATE + String.format(DEFAULT_MESSAGE_TEMPLATE, expected, actual);
        this.message = message.isEmpty() ? defaultMsg : message + ARROW + defaultMsg;
    }

    public AssertionFailedError(Class<?> expected, String message) {
        String defaultMsg = String.format(DEFAULT_NO_EXCEPTION_MESSAGE, expected);
        this.message = message.isEmpty() ? defaultMsg : message + ARROW + defaultMsg;
    }

    public AssertionFailedError(String message, long duration, long elapsed) {
        String defaultMsg = String.format(DEFAULT_EXCEEDED_DURATION_MESSAGE, duration, elapsed);
        this.message = message.isEmpty() ? defaultMsg : message + ARROW + defaultMsg;
    }

    public AssertionFailedError(String message, long duration) {
        String defaultMsg = String.format(DEFAULT_DURATION_MESSAGE, duration);
        this.message = message.isEmpty() ? defaultMsg : message + ARROW + defaultMsg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
