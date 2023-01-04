package assertions;

import errors.AssertionFailedError;
import function.Executable;
import function.ThrowingSupplier;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class Assertions {
    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, "");
    }


    public static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual))
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(Object expected, Object actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertEquals(int expected, int actual) {
        assertEquals(expected, actual, "");
    }

    public static void assertEquals(int expected, int actual, String message) {
        if (expected != actual)
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(int expected, int actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertEquals(float expected, float actual) {
        assertEquals(expected, actual, "");
    }

    public static void assertEquals(float expected, float actual, String message) {
        if (expected != actual)
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(float expected, float actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertEquals(double expected, double actual) {
        assertEquals(expected, actual, "");
    }

    public static void assertEquals(double expected, double actual, String message) {
        if (expected != actual)
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(double expected, double actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertEquals(long expected, long actual) {
        assertEquals(expected, actual, "");
    }

    public static void assertEquals(long expected, long actual, String message) {
        if (expected != actual)
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(long expected, long actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertEquals(byte expected, byte actual) {
        assertEquals(expected, actual, "");
    }

    public static void assertEquals(byte expected, byte actual, String message) {
        if (expected != actual)
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(byte expected, byte actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertEquals(boolean expected, boolean actual) {
        assertEquals(expected, actual, "");
    }

    public static void assertEquals(boolean expected, boolean actual, String message) {
        if (expected != actual)
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(boolean expected, boolean actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertEquals(char expected, char actual) {
        assertEquals(expected, actual, "");
    }

    public static void assertEquals(char expected, char actual, String message) {
        if (expected != actual)
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(char expected, char actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertEquals(short expected, short actual) {
        assertEquals(expected, actual, "");
    }

    public static void assertEquals(short expected, short actual, String message) {
        if (expected != actual)
            throw new AssertionFailedError(expected, actual, message);
    }

    public static void assertEquals(short expected, short actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, messageSupplier.get());
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, "");
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition)
            throw new AssertionFailedError(true, false, message);
    }

    public static void assertTrue(boolean condition, Supplier<String> messageSupplier) {
        assertTrue(condition, messageSupplier.get());
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, "");
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition)
            throw new AssertionFailedError(false, true, message);
    }

    public static void assertFalse(boolean condition, Supplier<String> messageSupplier) {
        assertFalse(condition, messageSupplier.get());
    }

    public static void assertNull(Object object) {
        assertNull(object, "");
    }

    public static void assertNull(Object object, String message) {
        if (object != null)
            throw new AssertionFailedError(null, object, message);
    }

    public static void assertNull(Object object, Supplier<String> messageSupplier) {
        assertNull(object, messageSupplier.get());
    }

    public static void assertNotNull(Object object) {
        assertNull(object, "");
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null)
            throw new AssertionFailedError(message);
    }

    public static void assertNotNull(Object object, Supplier<String> messageSupplier) {
        assertNotNull(object, messageSupplier.get());
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable)  {
        return assertThrows(expectedType, executable, "");
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable, String message)  {
        try {
            executable.execute();
            throw new AssertionFailedError(expectedType, message);
        } catch (Throwable throwable) {
            if (!(expectedType.isInstance(throwable)) && !(throwable instanceof AssertionFailedError)) {
                throw new AssertionFailedError(expectedType, throwable.getClass(), message);
            }
            return (T) throwable;
        }
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable, Supplier<String> messageSupplier) {
        return assertThrows(expectedType, executable, messageSupplier.get());
    }

    public static void assertTimeout(Duration timeout, Executable executable)  {
        assertTimeout(timeout, executable, "");
    }

    public static void assertTimeout(Duration timeout, Executable executable, String message) {
       assertTimeout(timeout, () -> {executable.execute(); return null;}, message);
    }

    public static void assertTimeout(Duration timeout, Executable executable, Supplier<String> messageSupplier) {
        assertTimeout(timeout, executable, messageSupplier.get());
    }

    public static <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier) {
        return assertTimeout(timeout, supplier, "");
    }

    public static <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier, String message) {
        long timeoutMillis = timeout.toMillis();
        long startMillis = System.currentTimeMillis();

        T result;
        try {
            result = supplier.get();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        long endMillis = System.currentTimeMillis();
        long durationMillis = endMillis - startMillis;
        if (durationMillis > timeoutMillis) {
            throw new AssertionFailedError(message, timeoutMillis, durationMillis - timeoutMillis);
        }

        return result;
    }

    public static <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier, Supplier<String> messageSupplier) {
        return assertTimeout(timeout, supplier, messageSupplier.get());
    }

    public static <T> T assertTimeoutPreemptively(Duration timeout, Executable executable) {
        return assertTimeoutPreemptively(timeout, executable, "");
    }

    public static <T> T assertTimeoutPreemptively(Duration timeout, Executable executable,  String message) {
        return assertTimeoutPreemptively(timeout, () -> {executable.execute(); return null;}, message);
    }

    public static <T> T assertTimeoutPreemptively(Duration timeout, Executable executable,  Supplier<String> messageSupplier) {
        return assertTimeoutPreemptively(timeout, executable, messageSupplier.get());
    }

    public static <T> T assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier) {
        return assertTimeoutPreemptively(timeout, supplier, "");
    }

    public static <T> T assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier, String message) {
        ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            Runnable runnable = () -> {
                try {
                    supplier.get();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
            Future<T> future = (Future<T>) executorService.submit(runnable);
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new AssertionFailedError(message, timeout.toMillis());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <T> T assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier, Supplier<String> messageSupplier) {
        return assertTimeoutPreemptively(timeout, supplier, messageSupplier.get());
    }
}
