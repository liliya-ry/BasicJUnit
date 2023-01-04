package assertions;

import errors.AssertionFailedError;
import java.util.Objects;

public class Assertions {
    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertEquals(int expected, int actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertEquals(float expected, float actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(float expected, float actual, String message) {
        if (expected != actual) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertEquals(double expected, double actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(double expected, double actual, String message) {
        if (expected != actual) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertEquals(long expected, long actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(long expected, long actual, String message) {
        if (expected != actual) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertEquals(byte expected, byte actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(byte expected, byte actual, String message) {
        if (expected != actual) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertEquals(boolean expected, boolean actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(boolean expected, boolean actual, String message) {
        if (expected != actual) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertEquals(char expected, char actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(char expected, char actual, String message) {
        if (expected != actual) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertEquals(short expected, short actual) {
        assertEquals(expected, actual, null);
    }

    public static void assertEquals(short expected, short actual, String message) {
        if (expected != actual) {
            throw new AssertionFailedError(expected, actual, message);
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, null);
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionFailedError(true, false, message);
        }
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, null);
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionFailedError(false, true, message);
        }
    }

    public static void assertNull(Object object) {
        assertNull(object, null);
    }

    public static void assertNull(Object object, String message) {
        if (object != null) {
            throw new AssertionFailedError(null, object, message);
        }
    }

    public static void assertNotNull(Object object) {
        assertNull(object, null);
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new AssertionFailedError(message);
        }
    }
}
