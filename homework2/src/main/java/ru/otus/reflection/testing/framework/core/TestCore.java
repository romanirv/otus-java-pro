package ru.otus.reflection.testing.framework.core;

import ru.otus.reflection.testing.framework.api.AfterSuite;
import ru.otus.reflection.testing.framework.api.BeforeSuite;
import ru.otus.reflection.testing.framework.api.Test;
import ru.otus.reflection.testing.framework.entity.TestResult;
import ru.otus.reflection.testing.framework.exception.TestException;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

    public List<TestResult> runTests() {
        List<TestResult> results = new ArrayList<>();

        List<Method> testMethods = getMethodsWithTestAnnotations();
        int currTestIdx = 1;
        boolean status = false;

        for (Method method : testMethods) {
            try {
                method.invoke(instance);
                status = true;
            } catch (Exception e) {
                e.getCause().printStackTrace();
                status = false;
            }

            results.add(new TestResult(method.getName(), currTestIdx, status));
            ++currTestIdx;
        }

        return results;
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

