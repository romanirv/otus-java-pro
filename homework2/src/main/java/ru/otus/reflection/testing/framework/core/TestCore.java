package ru.otus.reflection.testing.framework.core;

import ru.otus.reflection.testing.framework.api.AfterSuite;
import ru.otus.reflection.testing.framework.api.BeforeSuite;
import ru.otus.reflection.testing.framework.api.Test;
import ru.otus.reflection.testing.framework.exception.TestException;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TestCore {

    private Class<?> cls;
    private Object instance;

    public TestCore(Class<?> cls, Object instance) {
        this.cls = cls;
        this.instance = instance;
    }

    public void runBeforeSuite() {
        Optional<Method> beforeSuiteMethod = getBeforeSuiteMethod();
        if (beforeSuiteMethod.isPresent()) {
            try {
                beforeSuiteMethod.get().invoke(instance);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new TestException("Invoke BeforeSuite method '" +
                        beforeSuiteMethod.get().getName() + "' error!");
            }
        }
    }

    public void runTests() {
        int successCount = 0;
        int errorCount = 0;

        List<Method> testMethods = getMethodsWithTestAnnotations();
        int currTestIdx = 1;
        for (Method method : testMethods) {
            try {
                System.out.println("=========== Start test " +
                        currTestIdx + "/" + testMethods.size() +
                        " [" + method.getName() + "] ============ ");
                method.invoke(instance);
                ++successCount;
            } catch (Exception e) {
                System.out.println("Test error: " + e.getLocalizedMessage());
                ++errorCount;
            }

            ++currTestIdx;
        }

        System.out.println("=========== Run tests: " + testMethods.size() + "," +
                " success: " + successCount + ", error: " + errorCount + " =========");

    }

    public void runAfterSuite() {
        Optional<Method> afterSuiteMethod = getAfterSuiteMethod();
        if (afterSuiteMethod.isPresent()) {
            try {
                afterSuiteMethod.get().invoke(instance);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new TestException("Invoke AfterSuite method '" +
                        afterSuiteMethod.get().getName() + "'error!");
            }
        }
    }

    private Optional<Method> getBeforeSuiteMethod() {
        List<Method> methods = findMethodsWithAnnotation(this.cls, BeforeSuite.class);
        if (methods.size() > 1) {
            throw new TestException("Class " + cls + " has " + methods.size() +
                    " methods with annotation BeforeSuite");
        }

        return methods.stream().findFirst();
    }

    private List<Method> getMethodsWithTestAnnotations() {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(TestCore::isTestMethod)
                .sorted(Comparator.comparingInt((Method m) -> m.getAnnotation(Test.class).priority()).reversed())
                .toList();
    }

    private Optional<Method> getAfterSuiteMethod() {
        List<Method> methods = findMethodsWithAnnotation(cls, AfterSuite.class);
        if (methods.size() > 1) {
            throw new TestException("Class " + cls +
                    " has " + methods.size() + " methods with annotation AfterSuite");
        }

        return methods.stream().findFirst();
    }

    private static List<Method> findMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation > annotationType) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotationType))
                .toList();
    }

    private static boolean isTestMethod(Method method) {
        return method.isAnnotationPresent(Test.class)
                && method.getAnnotation(Test.class).priority() >= TestUtils.MIN_PRIORITY
                && method.getAnnotation(Test.class).priority() <= TestUtils.MAX_PRIORITY;
    }
}

