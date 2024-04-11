package ru.outus.streamapi.entiry;

public class Task {

    public enum Status {
        OPENED,
        IN_PROCESS,
        CLOSED
    }

    private final long id;

    private final String name;


    private Status status;

    public Task(long id, String name, Status status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }
}
