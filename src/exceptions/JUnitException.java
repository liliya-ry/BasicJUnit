package exceptions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JUnitException extends Throwable {
    private final String message;

    public JUnitException(Class<?> clazz, Method method, String annotationName) {
        String modifiers = Modifier.toString(method.getModifiers());
        this.message = String.format("@%s method '%s %s.%s()' must be static " +
                        "unless the test class is annotated with @TestInstance(Lifecycle.PER_CLASS).",
                annotationName, modifiers, clazz.getSimpleName(), method.getName());
    }

    public String getMessage() {
        return message;
    }
}
