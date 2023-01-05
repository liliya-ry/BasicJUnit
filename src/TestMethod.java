import annotations.Disabled;
import annotations.DisplayName;
import annotations.RepeatedTest;
import annotations.Test;

import java.lang.reflect.Method;

public class TestMethod {
    static final String STATUS_OK = "OK";
    static final String STATUS_FAILED = "X";
    static final String STATUS_DISABLED = "S";

    Method method;
    String displayName;
    String status = STATUS_OK;
    String failureMessage = "";
    int repeats;
    String[] dependsMethods;

    TestMethod(Method method) {
        this.method = method;
        setDisplayName();
        setRepeats();
        setDependsMethods();
    }

    private void setDisplayName() {
        DisplayName displayNameAnn = method.getAnnotation(DisplayName.class);
        displayName = displayNameAnn != null ? displayNameAnn.value() : method.getName() + "()";
    }

    private void setRepeats() {
        RepeatedTest repeatedTestAnn = method.getAnnotation(RepeatedTest.class);
        repeats = repeatedTestAnn != null ? repeatedTestAnn.value() : 1;
    }

    private void setDependsMethods() {
        Test testAnn = method.getAnnotation(Test.class);
        dependsMethods = testAnn.dependsOnMethods();
    }

    void printTestMethod() {
        char successChar = !status.equals(STATUS_FAILED) ?  '+' : '\'';
        System.out.printf("|   %c-- %s [%s] %s%n", successChar, displayName, status, failureMessage);

        if (repeats == 1) {
            return;
        }

        for (int i = 0; i < repeats; i++) {
            System.out.printf("|      +-- repetition %d of %d [%s] %s%n", i + 1, repeats, status, failureMessage);
        }
    }

    void setFailure(String failureMessage) {
        status = TestMethod.STATUS_FAILED;
        this.failureMessage = failureMessage;
    }

    boolean checkDisabled() {
        Disabled disabledAnn = method.getAnnotation(Disabled.class);
        if (disabledAnn == null) {
            return false;
        }

        failureMessage = disabledAnn.value();
        status = STATUS_DISABLED;
        return true;
    }
}
