package ru.otus.reflection.tests;

import ru.otus.reflection.testing.framework.api.Disabled;
import ru.otus.reflection.testing.framework.api.Test;

@Disabled
public class TestDisabled {

    @Test(priority = 10)
    public void test() {
        System.out.println("TestDisable.test()");
    }
}
