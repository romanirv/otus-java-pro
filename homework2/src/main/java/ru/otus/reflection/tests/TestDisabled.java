package ru.otus.reflection.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.reflection.testing.framework.api.Disabled;
import ru.otus.reflection.testing.framework.api.Test;

@Disabled
public class TestDisabled {

    private static final Logger logger = LoggerFactory.getLogger(TestDisabled.class);

    @Test(priority = 10)
    public void test() {
        logger.debug("Run disables test!");
    }
}
