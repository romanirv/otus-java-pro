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

        List<Method> testMethods = TestUtils.getMethodsWithTestAnnotations(cls);
        List<Method> beforeTestsMethods = TestUtils.getBeforeTestsMethod(cls);
        List<Method> afterTestsMethods = TestUtils.getAfterMethods(cls);

        int currTestIdx = 1;
        boolean status = false;

        for (Method method : testMethods) {
            if (TestUtils.isTestClassDisabled(method)) {
                continue;
            }

            for (Method beforeMethod : beforeTestsMethods) {
                try {
                    beforeMethod.invoke(instance);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.getCause().printStackTrace();
                }
            }

            boolean requiredCatchException = TestUtils.isTestRequiredException(method);

            try {
                method.invoke(instance);
                status = !requiredCatchException;
            } catch (Exception e) {
                status = requiredCatchException && TestUtils.isInTestExpectedException(method, e.getCause().getClass());
                if (!status) {
                    e.getCause().printStackTrace();
                }
            }

            results.add(new TestResult(method.getName(), currTestIdx, status));
            ++currTestIdx;

            for (Method afterMethod : afterTestsMethods) {
                try {
                    afterMethod.invoke(instance);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.getCause().printStackTrace();
                }
            }
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

    private Optional<Method> getBeforeSuiteMethod() {
        List<Method> methods = TestUtils.findMethodsWithAnnotation(cls, BeforeSuite.class);
        if (methods.size() > 1) {
            throw new TestException("Class " + cls + " has " + methods.size() +
                    " methods with annotation BeforeSuite");
        }

        return methods.stream().findFirst();
    }
}

