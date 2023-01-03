package errors;

public class AssertionFailedError extends Error {
    public AssertionFailedError(String message) {
        super(message);
    }
}
