package ru.otus.streamapi.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.outus.streamapi.dao.TaskDao;
import ru.outus.streamapi.entiry.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDaoTest {
    @Test
    public void testGetTaskByStatusEmpty() {
        TaskDao taskDao = new TaskDao(new ArrayList<>());
        List<Task> inProcessTasks = taskDao.getTasksByStatus(Task.Status.IN_PROCESS);
        Assertions.assertTrue(inProcessTasks.isEmpty());
    }
    @Test
    public void testGetTasksByStatusFound() {
        List<Task> openedTasks = List.of(
                new Task(1, "task1", Task.Status.OPENED),
                new Task(2, "task2", Task.Status.OPENED),
                new Task(3, "task3", Task.Status.OPENED)
        );
        List<Task> inProcessTasks = List.of(
                new Task(4, "task4", Task.Status.IN_PROCESS),
                new Task(5, "task5", Task.Status.IN_PROCESS),
                new Task(6, "task6", Task.Status.IN_PROCESS)
        );
        List<Task> closedTasks = List.of(
                new Task(7, "task7", Task.Status.CLOSED),
                new Task(7, "task8", Task.Status.CLOSED),
                new Task(7, "task9", Task.Status.CLOSED)
        );

        List<Task> tasks = new ArrayList<>();
        tasks.addAll(openedTasks);
        tasks.addAll(inProcessTasks);
        tasks.addAll(closedTasks);

        TaskDao taskDao = new TaskDao(tasks);

        List<Task> openedTasksActual = taskDao.getTasksByStatus(Task.Status.OPENED);
        List<Task> inProcessTasksActual = taskDao.getTasksByStatus(Task.Status.IN_PROCESS);
        List<Task> closedTasksActual = taskDao.getTasksByStatus(Task.Status.CLOSED);

        Assertions.assertIterableEquals(openedTasks, openedTasksActual);
        Assertions.assertIterableEquals(inProcessTasks, inProcessTasksActual);
        Assertions.assertIterableEquals(closedTasks, closedTasksActual);
    }

    @Test
    public void testGetTasksByStatusNotFound() {
        TaskDao taskDao = new TaskDao(List.of(
                new Task(1, "task1", Task.Status.IN_PROCESS),
                new Task(2, "task2", Task.Status.IN_PROCESS),
                new Task(3, "task3", Task.Status.CLOSED)
        ));
        List<Task> openedTasksActual = taskDao.getTasksByStatus(Task.Status.OPENED);
        Assertions.assertTrue(openedTasksActual.isEmpty());
    }

    @Test
    public void testGetTaskByIdFound() {

        TaskDao taskDao = new TaskDao(List.of(
                new Task(1, "task1", Task.Status.IN_PROCESS),
                new Task(2, "task2", Task.Status.CLOSED)));

        Optional<Task> foundTask = taskDao.getTaskById(1);

        Assertions.assertTrue(foundTask.isPresent());
        Assertions.assertEquals(foundTask.get().getId(), 1);
        Assertions.assertEquals(foundTask.get().getName(), "task1");
        Assertions.assertEquals(foundTask.get().getStatus(), Task.Status.IN_PROCESS);
    }

    @Test
    public void testGetTaskByIdNotFound() {

        TaskDao taskDao = new TaskDao(List.of(
                new Task(1, "task1", Task.Status.IN_PROCESS),
                new Task(2, "task2", Task.Status.CLOSED))
        );
        Optional<Task> foundTask = taskDao.getTaskById(3);
        Assertions.assertTrue(foundTask.isEmpty());
    }

    @Test
    public void testGetTasksSortedByStatus() {
        List<Task> unsortedByStatusTasks = List.of(
                new Task(1, "task1", Task.Status.CLOSED),
                new Task(2, "task2", Task.Status.CLOSED),
                new Task(3, "task3", Task.Status.IN_PROCESS),
                new Task(4, "task4", Task.Status.CLOSED),
                new Task(5, "task5", Task.Status.OPENED),
                new Task(6, "task6", Task.Status.OPENED)
        );
        TaskDao taskDao = new TaskDao(unsortedByStatusTasks);

        List<Task> sortedByStatusTasks = taskDao.getTasksSortedByStatus();

        Assertions.assertEquals(sortedByStatusTasks.size(), unsortedByStatusTasks.size());

        int currIdx = 0;
        for (Task sortedByStatusTask : sortedByStatusTasks) {
            Task.Status status = sortedByStatusTask.getStatus();
            if (currIdx < 2) {
                Assertions.assertEquals(status, Task.Status.OPENED);
            } else if (currIdx == 2) {
                Assertions.assertEquals(status, Task.Status.IN_PROCESS);
            } else {
                Assertions.assertEquals(status, Task.Status.CLOSED);
            }
            ++currIdx;
        }
    }

    @Test
    public void testGetTasksCountByStatus() {
        TaskDao taskDao = new TaskDao(List.of(
                new Task(1, "task1", Task.Status.OPENED),
                new Task(2, "task2", Task.Status.OPENED),
                new Task(3, "task3", Task.Status.IN_PROCESS),
                new Task(4, "task4", Task.Status.OPENED)
        ));

        long tasksOpenedCount = taskDao.getTasksCountByStatus(Task.Status.OPENED);
        long taskInProcessCount = taskDao.getTasksCountByStatus(Task.Status.IN_PROCESS);
        long taskClosedCount = taskDao.getTasksCountByStatus(Task.Status.CLOSED);

        Assertions.assertEquals(tasksOpenedCount, 3);
        Assertions.assertEquals(taskInProcessCount, 1);
        Assertions.assertEquals(taskClosedCount, 0);
    }
}
