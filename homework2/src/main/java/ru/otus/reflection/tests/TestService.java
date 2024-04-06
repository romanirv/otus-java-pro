package ru.otus.reflection.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.reflection.testing.framework.api.*;

public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);
    @BeforeSuite
    public void beforeSuite() {
        logger.debug("beforeSuite()");
    }

    @AfterSuite
    public void afterSuite() {
        logger.debug("afterSuite()");
    }

    @Before
    public void before() {
        logger.debug("before()");
    }

    @After
    public void after() {
        logger.debug("after()");
    }

    @Test(priority = 1)
    public void test1() {
        logger.debug("test1() -> success");
    }

    public void test2() {
        logger.debug("test2() -> ");
    }

    @Test(priority = 9)
    public void test3() throws RuntimeException {
        logger.debug("test3() -> error");
        throw new RuntimeException("Test error!");
    }

    @Test(priority = 0)
    @ThrowsException(exception = RuntimeException.class)
    public void test4() {
        logger.debug("test4() -> error");
        throw new RuntimeException("Runtime exception");
    }

    @Test(priority = 10)
    @Disabled
    public void test5() {
        logger.debug("test5() -> disabled");
    }
}
