package ru.otus.reflection.testing.framework.runner;

import ru.otus.reflection.testing.framework.core.TestCore;
import ru.otus.reflection.testing.framework.core.TestUtils;
import ru.otus.reflection.testing.framework.entity.TestResult;
import ru.otus.reflection.testing.framework.exception.TestException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class TestRunner {

    public static void runAllTests(String testingClassName) throws IllegalArgumentException {
        try {
            runAllTests(Class.forName(testingClassName));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class '" + testingClassName + "' not found!");
        }
    }

    public static void runAllTests(Class<?> cls) throws RuntimeException {
        if (TestUtils.isTestClassDisabled(cls)) {
            System.out.println("========= Tests for class " + "[" + cls + "] disabled! ===========");
            return;
        }

        System.out.println("========= Run tests for class " + "[" + cls + "]" + " ===========");

        TestCore testCore = new TestCore(cls, instanceTestingObject(cls));
        testCore.runBeforeSuite();

        List<TestResult> results = testCore.runTests();
        int successCount = 0;
        int errorCount = 0;

        for (TestResult result : results) {
            System.out.println("===== Test [" + result.getName() + "] " + result.getOrder() + "/" + results.size() +
                    (result.getStatus() ? " success =====" : " error   ====="));

            if (result.getStatus()) {
                ++successCount;
            } else {
                ++errorCount;
            }
        }
        System.out.println("Tests: " + results.size() + " success: " + successCount + " error: " + errorCount);

        testCore.runAfterSuite();
    }

    private static Object instanceTestingObject(Class<?> cls) {
        try {
            return cls.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            throw new TestException("Instance object of type " + cls + " error!");
        }
    }
}