package ru.otus.reflection.tests;

import ru.otus.reflection.testing.framework.api.*;

public class TestService {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("beforeSuite()");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("afterSuite()");
    }

    @Before
    public void before() {
        System.out.println("before()");
    }

    @After
    public void after() {
        System.out.println("after()");
    }

    @Test(priority = 1)
    public void test1() {
        System.out.println("test1() -> success");
    }

    public void test2() {
        System.out.println("test2() -> ");
    }

    @Test(priority = 9)
    public void test3() throws RuntimeException {
        System.out.println("test3() -> error");
        throw new RuntimeException("Test error!");
    }

    @Test(priority = 0)
    @ThrowsException(exception = RuntimeException.class)
    public void test4() {
        System.out.println("test4() -> error");
        throw new RuntimeException("Runtime exception");
    }

    @Test(priority = 10)
    @Disabled
    public void test5() {
        System.out.println("test5() -> disabled");
    }
}
