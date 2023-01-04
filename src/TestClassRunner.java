import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class TestClassRunner {
    int skippedTests = 0;
    int foundTests = 0;
    final List<TestMethod> tests = new ArrayList<>();
    final List<Failure> failures = new ArrayList<>();
    private final Class<?> clazz;
    private final List<Method> beforeEachMethods = new ArrayList<>();
    private final List<Method> afterEachMethods = new ArrayList<>();
    private final List<Method> beforeClassMethods = new ArrayList<>();
    private final List<Method> afterClassMethods = new ArrayList<>();

    public TestClassRunner(Class<?> clazz) {
        this.clazz = clazz;
        allocateMethods();
    }

    private void allocateMethods() {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            method.setAccessible(true);
            allocateMethod(method, annotations);
        }
    }

    private void allocateMethod(Method method, Annotation[] annotations) {
        for (Annotation a : annotations) {
            String annotationName = a.annotationType().getSimpleName();
            switch (annotationName) {
                case "Test" -> tests.add(new TestMethod(method));
                case "BeforeEach" -> beforeEachMethods.add(method);
                case "AfterEach" -> afterEachMethods.add(method);
                case "BeforeClass" -> beforeClassMethods.add(method);
                case "AfterClass" -> afterClassMethods.add(method);
            }
        }
    }

    public void runTestClass() throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object instance = constructor.newInstance();
        runMethodsFromList(beforeClassMethods, instance);

        for (TestMethod testMethod : tests) {
            runMethodsFromList(beforeEachMethods, instance);

            if (testMethod.checkDisabled()) {
                skippedTests++;
                continue;
            }

            foundTests += testMethod.repeats;
            for (int i = 0; i < testMethod.repeats; i++) {
                invokeMethod(testMethod, instance, i + 1);
            }

            runMethodsFromList(afterEachMethods, instance);
        }

        runMethodsFromList(afterClassMethods, instance);
    }

    private void invokeMethod(TestMethod testMethod, Object instance, int repetition) throws IllegalAccessException {
        try {
            testMethod.method.invoke(instance);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            testMethod.setFailure(cause.getMessage());
            Failure failure = new Failure(testMethod, clazz.getSimpleName(), cause, repetition);
            failures.add(failure);
        }
    }

    private void runMethodsFromList(List<Method> methods, Object instance) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(instance);
        }
    }

    public void printTestsInfo() {
        char successChar = failures.isEmpty() ? '+' : '\'';
        String status = failures.size() < tests.size() ? TestMethod.STATUS_OK : TestMethod.STATUS_FAILED;
        System.out.printf("| %c-- %s [%s]%n", successChar, clazz.getSimpleName(), status);
        for (TestMethod testMethod : tests) {
            testMethod.printTestMethod();
        }
    }

    public void printFailures() {
        for (Failure failure : failures) {
            failure.printFailure();
        }
    }
}
