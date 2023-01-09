import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;

public class JUnitRunner {
    private final List<TestClassRunner> testClassRunners = new ArrayList<>();
    private int failuresCount = 0;
    private int testsCount = 0;
    private int skippedTests = 0;
    private int startedTests = 0;
    private long startTime;
    private long endTime;

    //Todo - one constructor with package name and array of classes name
    public JUnitRunner(String[] classNames) {
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

    public JUnitRunner(String packageName) throws IOException {
        List<Class<?>> testClasses = findAllClasses(packageName);
        for (Class<?> testClass : testClasses) {
            TestClassRunner testClassRunner = new TestClassRunner(testClass);
            testClassRunners.add(testClassRunner);
        }
    }

    private List<Class<?>> findAllClasses(String packageName) throws IOException {
        List<Class<?>> classesList = new ArrayList<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        if (!packageName.equals(".")) {
            packageName = packageName.replaceAll("[.]", "/");
        }

        try (var inputStream = classLoader.getResourceAsStream(packageName);
             var isr = new InputStreamReader(inputStream);
             var reader = new BufferedReader(isr)) {

            for (String line; (line = reader.readLine()) != null;) {
                if (!line.endsWith(".class") && !line.endsWith(".java")) {
                    continue;
                }

                Class<?> clazz = getClass(line, packageName);
                classesList.add(clazz);
            }
        }

        return classesList;
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + className);
        }
        return null;
    }

    public void startTests() throws Exception {
        startTime = System.currentTimeMillis();
        for (TestClassRunner runner : testClassRunners) {
            if (runner.failed) {
                testsCount += runner.tests.size();
                continue;
            }
            runner.runTestClass();
            testsCount += runner.foundTests;
            failuresCount += runner.failures.size();
            skippedTests += runner.skippedTests;
        }
        startedTests = testsCount < skippedTests ? testsCount : testsCount - skippedTests;
        endTime = System.currentTimeMillis();
    }

    private void printTestsInfo() {
        char successChar = failuresCount != 0 ? '+' : '\'';
        String status = failuresCount < testsCount ? TestMethod.STATUS_OK : TestMethod.STATUS_FAILED;
        System.out.printf(".%n%c-- JUnit Jupiter [%s]%n", successChar, status);
        for (TestClassRunner runner : testClassRunners) {
            if (runner.failed) {
                runner.printFailuresInfo();
            } else {
                runner.printTestsInfo();
            }
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

    public static Options createOptions() {
        Options options = new Options();
        Option option = Option.builder("p").hasArg().build();
        options.addOption(option);
        return options;
    }

    private static CommandLine getCmd(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("JUnit: unknown option --");
            return null;
        }
    }

    private static void printUsage() {
        System.out.println("Invalid command");
    }

    public static void main(String[] args) throws Exception {
        if (args == null) {
            printUsage();
            return;
        }

        Options options = createOptions();
        CommandLine cmd = getCmd(options, args);
        if (cmd == null) {
            return;
        }

        String packageName = cmd.getOptionValue("p");
        JUnitRunner runner = (packageName == null) ? new JUnitRunner(args) : new JUnitRunner(packageName);
        runner.startTests();
        runner.printInfo();
    }
}
