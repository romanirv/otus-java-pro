package ru.outus.streamapi.dao;

import ru.outus.streamapi.entiry.Task;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TaskDao {

    private final List<Task> tasks;

    public TaskDao(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasksByStatus(Task.Status status) {
        return tasks.stream().filter(s -> s.getStatus() == status).toList();
    }

    public Optional<Task> getTaskById(long id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst();
    }

    public List<Task> getTasksSortedByStatus() {
        return tasks.stream().sorted(Comparator.comparing(task -> {
            if (task.getStatus() == Task.Status.OPENED) {
                return -1;
            } else if (task.getStatus() == Task.Status.IN_PROCESS) {
                return 0;
            }
            return 1;
        })).toList();
    }

    public long getTasksCountByStatus(Task.Status status) {
        return tasks.stream().filter(t -> t.getStatus() == status).count();
    }
}
