package exceptions;

import java.lang.reflect.Method;

public class PreconditionViolationException extends Exception {
    private final String message;

    public PreconditionViolationException(Class<?> clazz, Method method) {
        String returnType = method.getReturnType().getSimpleName();
        this.message = String.format("Configuration error: @RepeatedTest on method [%s %s.%s()] " +
                "must be declared with a positive 'value'.", returnType, clazz.getSimpleName(), method.getName());
    }

    public String getMessage() {
        return message;
    }
}
