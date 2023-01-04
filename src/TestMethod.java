import annotations.Disabled;
import annotations.DisplayName;

import java.lang.reflect.Method;

public class TestMethod {
    static final String STATUS_OK = "OK";
    static final String STATUS_FAILED = "X";
    static final String STATUS_DISABLED = "S";

    Method method;
    String displayName;
    String status = STATUS_OK;
    String failureMessage = "";

    TestMethod(Method method) {
        this.method = method;
        setDisplayName();
    }

    private void setDisplayName() {
        DisplayName displayNameAnn = method.getAnnotation(DisplayName.class);
        displayName = displayNameAnn != null ? displayNameAnn.value() : method.getName() + "()";
    }

    public void printTestMethod() {
        char successChar = !status.equals(STATUS_FAILED) ?  '+' : '\'';
        System.out.printf("|      %c-- %s [%s] %s%n", successChar, displayName, status, failureMessage);
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
