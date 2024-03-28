package ru.otus.reflection.testing.framework.runner;

import ru.otus.reflection.testing.framework.core.TestCore;
import ru.otus.reflection.testing.framework.exception.TestException;

import java.lang.reflect.InvocationTargetException;

public class TestRunner {

    public static void runAllTests(String testingClassName) throws IllegalArgumentException {
        try {
            runAllTests(Class.forName(testingClassName));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class '" + testingClassName + "' not found!");
        }
    }

    public static void runAllTests(Class<?> cls) throws RuntimeException {
        System.out.println("========= Run tests for class " + "[" + cls + "]" + " ===========");

        TestCore testCore = new TestCore(cls, instanceTestingObject(cls));
        testCore.runBeforeSuite();
        testCore.runTests();
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