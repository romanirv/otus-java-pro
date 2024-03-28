package ru.otus.reflection.testing.framework.entity;

public class TestResult {

    public TestResult(String name, int order, boolean status) {
        this.name = name;
        this.order = order;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public boolean getStatus() {
        return status;
    }

    private String name;

    private int order;

    private boolean status;
}
