package exceptions;

import java.lang.reflect.Method;

public class PreconditionViolationException extends Exception {
    private final String message;

    public PreconditionViolationException(Class<?> clazz, Method method) {
        String returnType = method.getReturnType().getSimpleName();
        this.message = String.format("Configuration error: @RepeatedTest on method [%s %s.%s()] " +
                "must be declared with a positive 'value'.", returnType, clazz.getSimpleName(), method.getName());
    }

    public PreconditionViolationException(Class<?> clazz, String methodName1, String methodName2) {
        this.message = String.format("Configuration error: Circular dependency in %s , methods: %s, %s", clazz.getSimpleName(), methodName1, methodName2);
    }

    public String getMessage() {
        return message;
    }
}
