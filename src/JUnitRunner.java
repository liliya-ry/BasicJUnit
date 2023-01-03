import java.lang.reflect.InvocationTargetException;

public class JUnitRunner {
    private final String[] classNames;

    public JUnitRunner(String[] classNames) {
        this.classNames = classNames;
    }

    public void startTests() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        for (String className : classNames) {
            Class<?> clazz = Class.forName(className);
            JUnitTestClass jUnitTestClass = new JUnitTestClass(clazz);
            jUnitTestClass.runTestClass();
        }
    }



    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        JUnitRunner runner = new JUnitRunner(args);
        runner.startTests();
    }
}
