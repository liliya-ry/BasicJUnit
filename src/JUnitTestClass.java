import annotations.DisplayName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JUnitTestClass {
    private long testStartTime;
    private long testEndTime;
    private final Class<?> clazz;
    private final Object instance;
    private final List<Method> tests = new ArrayList<>();
    private final List<String> displayNames = new ArrayList<>();
    private final List<Method> beforeEachMethods = new ArrayList<>();
    private final List<Method> afterEachMethods = new ArrayList<>();
    private final List<Method> beforeClassMethods = new ArrayList<>();
    private final List<Method> afterClassMethods = new ArrayList<>();
    private final List<Throwable> failures = new ArrayList<>();

    public JUnitTestClass(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.clazz = clazz;
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        this.instance = constructor.newInstance();
        this.testStartTime = System.currentTimeMillis();
    }

    public void runTestClass() throws InvocationTargetException, IllegalAccessException {
        allocateMethods();
        runMethods();
        printTestNames();
        printFailures();
        printTestsInfo();
    }

    private void allocateMethods() {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            allocateMethod(method, annotations);
        }
    }

    private void allocateMethod(Method method, Annotation[] annotations) {
        for (Annotation a : annotations) {
            String annotationName = a.annotationType().getSimpleName();
            String displayName = null;
            switch (annotationName) {
                case "Test" -> tests.add(method);
                case "BeforeEach" -> beforeEachMethods.add(method);
                case "AfterEach" -> afterEachMethods.add(method);
                case "BeforeClass" -> beforeClassMethods.add(method);
                case "AfterClass" -> afterClassMethods.add(method);
                case "DisplayName" -> displayName = ((DisplayName) a).value();
            }
            displayNames.add(displayName);
        }
    }

    private void runMethods() throws InvocationTargetException, IllegalAccessException {
        runMethodsFromList(beforeClassMethods);

        for (Method method : tests) {
            runMethodsFromList(beforeEachMethods);
            try {
                method.invoke(instance);
            } catch (InvocationTargetException e) {
                failures.add(e.getCause());
            }
            runMethodsFromList(afterEachMethods);
        }

        runMethodsFromList(afterClassMethods);
        testEndTime = System.currentTimeMillis();
    }

    private void runMethodsFromList(List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(instance);
        }
    }

    private void printTestNames() {
        System.out.println("Tests:");
        for (int i = 0; i < tests.size(); i++) {
            String displayName = displayNames.get(i);
            if (displayName == null) {
                Method test = tests.get(i);
                displayName = test.getName() + "()";
            }
            System.out.println(displayName);
        }
        System.out.println();
    }

    private void printFailures() {
        if (failures.isEmpty()) {
            return;
        }

        System.out.printf("Failures (%d):%n", failures.size());
        for (Throwable failure : failures) {
            failure.printStackTrace();
        }
        System.out.println();
    }

    private void printTestsInfo() {
        System.out.printf("Test run finished after %d ms%n", testEndTime - testStartTime);
        System.out.printf("[       %d tests found      ]%n", tests.size());
        System.out.printf("[       %d tests successful ]%n", tests.size() - failures.size());
        System.out.printf("[       %d tests failed     ]%n", failures.size());
    }
}
