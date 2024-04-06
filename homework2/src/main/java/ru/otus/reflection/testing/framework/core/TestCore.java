package ru.otus.reflection.testing.framework.core;

import ru.otus.reflection.testing.framework.api.*;
import ru.otus.reflection.testing.framework.entity.TestResult;
import ru.otus.reflection.testing.framework.exception.TestException;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class TestCore {
    private final Class<?> cls;
    private final Object instance;

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

        List<Method> testMethods = TestUtils.getMethodsWithTestAnnotations(cls);
        List<Method> beforeTestsMethods = TestUtils.getBeforeTestsMethod(cls);
        List<Method> afterTestsMethods = TestUtils.getAfterMethods(cls);

        int currTestIdx = 1;

        for (Method method : testMethods) {
            if (TestUtils.isTestClassDisabled(method)) {
                continue;
            }

            runBeforeTestsMethods(beforeTestsMethods);

            results.add(new TestResult(method.getName(), currTestIdx, runTestMethod(method)));
            ++currTestIdx;

            runAfterTestsMethods(afterTestsMethods);
        }

        return results;
    }

    public void runAfterSuite() {
        Optional<Method> afterSuiteMethod = TestUtils.getAfterSuiteMethod(cls);
        if (afterSuiteMethod.isPresent()) {
            try {
                afterSuiteMethod.get().invoke(instance);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new TestException("Invoke AfterSuite method '" +
                        afterSuiteMethod.get().getName() + "'error!");
            }
        }
    }

    private void runBeforeTestsMethods(List<Method> beforeTestsMethods) {
        for (Method beforeMethod : beforeTestsMethods) {
            try {
                beforeMethod.invoke(instance);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.getCause().printStackTrace();
            }
        }
    }

    private boolean runTestMethod(Method method) {
        boolean requiredCatchException = TestUtils.isTestRequiredException(method);
        boolean status = false;
        try {
            method.invoke(instance);
            status = !requiredCatchException;
        } catch (Exception e) {
            status = requiredCatchException && TestUtils.isInTestExpectedException(method, e.getCause().getClass());
            if (!status) {
                e.getCause().printStackTrace();
            }
        }

        return status;
    }

    private void runAfterTestsMethods(List<Method> afterTestsMethods) {
        for (Method afterMethod : afterTestsMethods) {
            try {
                afterMethod.invoke(instance);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.getCause().printStackTrace();
            }
        }
    }

    private Optional<Method> getBeforeSuiteMethod() {
        List<Method> methods = TestUtils.findMethodsWithAnnotation(cls, BeforeSuite.class);
        if (methods.size() > 1) {
            throw new TestException("Class " + cls + " has " + methods.size() +
                    " methods with annotation BeforeSuite");
        }

        return methods.stream().findFirst();
    }
}

