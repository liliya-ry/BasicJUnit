import java.util.ArrayList;
import java.util.List;

public class JUnitRunner {
    private final List<TestClassRunner> testClassRunners;
    private int failuresCount = 0;
    private int testsCount = 0;
    private int skippedTests = 0;
    private long startTime;
    private long endTime;

    public JUnitRunner(String[] classNames) {
        testClassRunners = new ArrayList<>();
        for (String className : classNames) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found " + className);
            }
            TestClassRunner testClassRunner = new TestClassRunner(clazz);
            testClassRunners.add(testClassRunner);
        }
    }

    public void startTests() throws Exception {
        startTime = System.currentTimeMillis();
        for (TestClassRunner runner : testClassRunners) {
            runner.runTestClass();
            testsCount += runner.foundTests;
            failuresCount += runner.failures.size();
            skippedTests += runner.skippedTests;
        }
        endTime = System.currentTimeMillis();
    }

    private void printTestsInfo() {
        char successChar = failuresCount != 0 ? '+' : '\'';
        String status = failuresCount < testsCount ? TestMethod.STATUS_OK : TestMethod.STATUS_FAILED;
        System.out.printf(".%n%c-- JUnit Jupiter [%s]%n", successChar, status);
        for (TestClassRunner runner : testClassRunners) {
            runner.printTestsInfo();
        }
    }

    public void printFailures() {
        if (failuresCount == 0) {
            return;
        }

        System.out.printf("Failures (%d):%n", failuresCount);
        for (TestClassRunner runner : testClassRunners) {
            runner.printFailures();
        }
    }

    private void printFinishInfo() {
        System.out.printf("Test run finished after %d ms%n", endTime - startTime);
        System.out.printf("[       %d tests found      ]%n", testsCount);
        System.out.printf("[       %d tests skipped      ]%n", skippedTests);
        int startedTests = testsCount - skippedTests;
        System.out.printf("[       %d tests started      ]%n", startedTests);
        System.out.printf("[       %d tests successful ]%n", startedTests - failuresCount);
        System.out.printf("[       %d tests failed     ]%n", failuresCount);
    }

    public void printInfo() {
        printTestsInfo();
        System.out.println();
        printFailures();
        System.out.println();
        printFinishInfo();
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        JUnitRunner runner = new JUnitRunner(args);
        runner.startTests();
        runner.printInfo();
    }
}
