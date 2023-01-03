package assertions;

import errors.AssertionFailedError;
import java.util.Objects;

public class Assertions {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "expected <%s> but was: <%s>";
    private static final String DEFAULT_NOT_NULL_MESSAGE = "expected not <null>";
    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            String finalMsg = buildMessage(expected, actual, message);
            throw new AssertionFailedError(finalMsg);
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, null);
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            String finalMsg = buildMessage(true, false, message);
            throw new AssertionFailedError(finalMsg);
        }
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, null);
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            String finalMsg = buildMessage(false, true, message);
            throw new AssertionFailedError(finalMsg);
        }
    }

    public static void assertNull(Object object) {
        assertNull(object, null);
    }

    public static void assertNull(Object object, String message) {
        if (object != null) {
            String finalMsg = buildMessage(null, object, message);
            throw new AssertionFailedError(finalMsg);
        }
    }

    public static void assertNotNull(Object object) {
        assertNull(object, null);
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            String finalMsg = message == null ? DEFAULT_NOT_NULL_MESSAGE : message + " ==> " + DEFAULT_NOT_NULL_MESSAGE;
            throw new AssertionFailedError(finalMsg);
        }
    }

    private static String buildMessage(Object expected, Object actual, String message) {
        String defaultMsg = String.format(DEFAULT_MESSAGE_TEMPLATE, expected, actual);
        return message == null ? defaultMsg : message + " ==> " + defaultMsg;
    }
}
