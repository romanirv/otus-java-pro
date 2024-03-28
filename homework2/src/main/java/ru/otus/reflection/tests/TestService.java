package ru.otus.reflection.tests;

import ru.otus.reflection.testing.framework.api.AfterSuite;
import ru.otus.reflection.testing.framework.api.BeforeSuite;
import ru.otus.reflection.testing.framework.api.Test;

public class TestService {

    public TestService() {

    }

    @BeforeSuite
    public void setUp() {
        System.out.println("setUp()");
    }

    @AfterSuite
    public void after() {
        System.out.println("tearDown()");
    }

    @Test(priority = 1)
    public void test1() {
        System.out.println("test1");
    }

    public void test2() {
        System.out.println("test2");
    }

    @Test(priority = 9)
    public void test3() throws Exception {
        System.out.println("test3");
        throw new Exception("Exception");
    }

    @Test(priority = 0)
    public void test4() {
        System.out.println("test4");
    }

}
