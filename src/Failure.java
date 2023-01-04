import java.util.Arrays;

public class Failure {
    TestMethod testMethod;
    Throwable cause;
    String className;
    int repetition;

    public Failure(TestMethod testMethod, String className, Throwable cause, int repetition) {
        this.testMethod = testMethod;
        this.className = className;
        this.cause = cause;
        this.repetition = repetition;
    }



    void printFailure() {
        String junitLine = String.format("  JUnit Jupiter:%s:%s", className, testMethod.displayName);
        if (testMethod.repeats > 1) {
            junitLine += String.format(":repetition %d of %d", repetition, testMethod.repeats);
        }
        System.out.println(junitLine);

        Class<?>[] paramTypes = testMethod.method.getParameterTypes();
        String paramTypesStr = paramTypes.length != 0 ? Arrays.toString(paramTypes) : "";
        String methodSourceLine = String.format("    MethodSource [className = '%s', methodName = '%s', methodParameterTypes = '%s']",
                                                className, testMethod.method.getName(), paramTypesStr);
        System.out.println(methodSourceLine);

        System.out.print("    => ");
        cause.printStackTrace();
    }
}
