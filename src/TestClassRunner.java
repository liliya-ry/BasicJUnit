import static assertions.Assertions.*;

import annotations.Test;
import exceptions.JUnitException;
import exceptions.PreconditionViolationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

public class TestClassRunner {
    private static final Logger LOGGER = Logger.getLogger("TestClassRunner");

    private final List<Method> beforeEachMethods = new ArrayList<>();
    private final List<Method> afterEachMethods = new ArrayList<>();
    private final List<Method> beforeClassMethods = new ArrayList<>();
    private final List<Method> afterClassMethods = new ArrayList<>();
    private final Class<?> clazz;
    final Map<String, TestMethod> tests = new HashMap<>();
    final List<Failure> failures = new ArrayList<>();
    boolean failed = false;
    int skippedTests = 0;
    int foundTests = 0;

    public TestClassRunner(Class<?> clazz) {
        this.clazz = clazz;
        allocateMethods();
    }

    private void allocateMethods() {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            method.setAccessible(true);
            try {
                allocateMethod(method, annotations);
            } catch (JUnitException e) {
                Failure failure = new Failure(clazz.getSimpleName(), e);
                failures.add(failure);
                failed = true;
            }
        }
    }

    private void allocateMethod(Method method, Annotation[] annotations) throws JUnitException {
        boolean isTest = false;
        for (Annotation a : annotations) {
            String annotationName = a.annotationType().getSimpleName();
            switch (annotationName) {
                case "Test" -> {
                    tests.put(method.getName(), new TestMethod(method));
                    isTest = true;
                }
                case "RepeatedTest" -> {
                    if (isTest) {
                        logMultipleTestDescriptors(method);
                        continue;
                    }
                    tests.put(method.getName(), new TestMethod(method));
                    isTest = true;
                }
                case "BeforeEach" -> beforeEachMethods.add(method);
                case "AfterEach" -> afterEachMethods.add(method);
                case "BeforeClass" -> addStaticMethod(method, beforeClassMethods, annotationName);
                case "AfterClass" -> addStaticMethod(method, afterClassMethods, annotationName);
            }
        }
    }

    public void runTestClass() throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object instance = constructor.newInstance();
        runMethodsFromList(beforeClassMethods, instance);

        for (Map.Entry<String, TestMethod> testEntry : tests.entrySet()) {
            runMethodsFromList(beforeEachMethods, instance);

            TestMethod testMethod = testEntry.getValue();
            if (testMethod.checkDisabled()) {
                skippedTests++;
                continue;
            }

            foundTests += testMethod.repeats;
            invokeDependsMethods(testMethod, instance);
            invokeTestMethod(testMethod, instance);

            runMethodsFromList(afterEachMethods, instance);
        }

        runMethodsFromList(afterClassMethods, instance);
    }

    private void addStaticMethod(Method method, List<Method> methods, String annotationName) throws JUnitException {
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new JUnitException(clazz, method, annotationName);
        }

        methods.add(method);
    }

    private void invokeDependsMethods(TestMethod testMethod, Object instance) {
        for (String methodName : testMethod.dependsMethods) {
            TestMethod dependsMethod = tests.get(methodName);
            if (dependsMethod == null) {
                //todo throw exception
            }
            invokeTestMethod(testMethod, instance);
        }
    }

    private void invokeTestMethod(TestMethod testMethod, Object instance) {
        if (testMethod.repeats < 1) {
            var exception = new PreconditionViolationException(clazz, testMethod.method);
            testMethod.setFailure(exception.getMessage());
            Failure failure = new Failure(clazz.getSimpleName(), exception);
            failures.add(failure);
        }

        if (testMethod.timeout > 0) {
            for (int i = 0; i < testMethod.repeats; i++) {
                assertTimeoutPreemptively(Duration.ofMillis(testMethod.timeout), () -> testMethod.method.invoke(instance));
            }
            return;
        }

        if (!testMethod.expectedExceptionClass.equals(Test.None.class)) {
            for (int i = 0; i < testMethod.repeats; i++) {
                assertThrows(testMethod.expectedExceptionClass, () -> testMethod.method.invoke(instance));
            }
            return;
        }

        invokeTest(testMethod, instance);
    }

    private void invokeTest(TestMethod testMethod, Object instance) {
        int i = 0;
        try {
            for (; i < testMethod.repeats; i++) {
                testMethod.method.invoke(instance);
            }
        } catch (IllegalAccessException ignored) {
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            testMethod.setFailure(cause.getMessage());
            Failure failure = new Failure(testMethod, clazz.getSimpleName(), cause, i);
            failures.add(failure);
        }
    }

    private void runMethodsFromList(List<Method> methods, Object instance) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(instance);
        }
    }

    private void logMultipleTestDescriptors(Method method) {
        String warningMsg = String.format("Possible configuration error: " +
                        "method [%s.%s()] resulted in multiple TestDescriptors.%n" +
                        "This is typically the result of annotating a method with multiple competing annotations such as " +
                        "@Test, @RepeatedTest, @ParameterizedTest, @TestFactory, etc.",
                clazz, method.getName());

        LOGGER.warning(warningMsg);
    }

    public void printTestsInfo() {
        char successChar = failures.isEmpty() ? '+' : '\'';
        String status = failures.size() < tests.size() ? TestMethod.STATUS_OK : TestMethod.STATUS_FAILED;
        System.out.printf("| %c-- %s [%s]%n", successChar, clazz.getSimpleName(), status);
        for (Map.Entry<String, TestMethod> testEntry : tests.entrySet()) {
            testEntry.getValue().printTestMethod();
        }
    }

    public void printFailuresInfo() {
        for (Failure failure : failures) {
            System.out.printf("| '-- %s [%s] %s%n", clazz.getSimpleName(), TestMethod.STATUS_FAILED, failure.cause.getMessage());
        }
    }

    public void printFailures() {
        for (Failure failure : failures) {
            failure.printFailure();
        }
    }
}
