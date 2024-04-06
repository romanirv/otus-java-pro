package ru.otus.reflection.testing.framework.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.reflection.testing.framework.core.TestCore;
import ru.otus.reflection.testing.framework.core.TestUtils;
import ru.otus.reflection.testing.framework.entity.TestResult;
import ru.otus.reflection.testing.framework.exception.TestException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    public static void runAllTests(String testingClassName) throws IllegalArgumentException {
        try {
            runAllTests(Class.forName(testingClassName));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class '" + testingClassName + "' not found!");
        }
    }

    public static void runAllTests(Class<?> cls) throws RuntimeException {
        if (checkIsTestCaseDisabled(cls)) {
            return;
        }

        logger.info("Run tests for class " + "[" + cls + "]");
        TestCore testCore = new TestCore(cls, instanceTestingObject(cls));

        testCore.runBeforeSuite();
        testResultsPrepare(testCore.runTests());
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

    private static boolean checkIsTestCaseDisabled(Class<?> cls) {
        if (TestUtils.isTestClassDisabled(cls)) {
            logger.error("Tests for class " + "[" + cls + "] disabled!");
            return true;
        }

        return false;
    }

    private static void testResultsPrepare(List<TestResult> results) {
        int successCount = 0;
        int errorCount = 0;

        for (TestResult result : results) {
            if (result.getStatus()) {
                logger.info("Test [" + result.getName() + "] " + result.getOrder() + "/" + results.size() + " success");
            } else {
                logger.error("Test [" + result.getName() + "] " + result.getOrder() + "/" + results.size() + " error");
            }

            if (result.getStatus()) {
                ++successCount;
            } else {
                ++errorCount;
            }
        }
        logger.info("Tests: " + results.size() + " success: " + successCount + " error: " + errorCount);

    }
}