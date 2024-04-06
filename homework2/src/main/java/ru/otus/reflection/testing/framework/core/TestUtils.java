package ru.otus.reflection.testing.framework.core;

import ru.otus.reflection.testing.framework.api.*;
import ru.otus.reflection.testing.framework.exception.TestException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TestUtils {

    public static List<Method> getMethodsWithTestAnnotations(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(TestUtils::isTestMethod)
                .sorted(Comparator.comparingInt((Method m) -> m.getAnnotation(Test.class).priority()).reversed())
                .toList();
    }

    public static Optional<Method> getAfterSuiteMethod(Class<?> cls) {
        List<Method> methods = findMethodsWithAnnotation(cls, AfterSuite.class);
        if (methods.size() > 1) {
            throw new TestException("Class " + cls +
                    " has " + methods.size() + " methods with annotation AfterSuite");
        }

        return methods.stream().findFirst();
    }

    public static List<Method> getBeforeTestsMethod(Class<?> cls) {
        return findMethodsWithAnnotation(cls, Before.class);
    }

    public static List<Method> getAfterMethods(Class<?> cls) {
        return findMethodsWithAnnotation(cls, After.class);
    }

    public static boolean isTestClassDisabled(Class<?> cls) {
        return cls.isAnnotationPresent(Disabled.class);
    }

    public static boolean isTestClassDisabled(Method method) {
        return method.isAnnotationPresent(Disabled.class);
    }

    public static boolean isTestRequiredException(Method method) {
        return method.isAnnotationPresent(ThrowsException.class);
    }

    public static boolean isInTestExpectedException(Method method, Class<?> exceptionType) {
        return exceptionType.equals(method.getAnnotation(ThrowsException.class).exception());
    }

    public static List<Method> findMethodsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationType) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotationType))
                .toList();
    }

    public static boolean isTestMethod(Method method) {
        return method.isAnnotationPresent(Test.class)
                && method.getAnnotation(Test.class).priority() >= TestUtils.MIN_PRIORITY
                && method.getAnnotation(Test.class).priority() <= TestUtils.MAX_PRIORITY;
    }

    public static final int MIN_PRIORITY = 0;
    public static final int MAX_PRIORITY = 10;
}
