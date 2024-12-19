package com.todolist.integration.manager;

import com.todolist.model.Task;
import com.todolist.model.TaskManager;
import org.junit.jupiter.api.*;
import javafx.scene.paint.Color;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskManager Integration Tests")
class TaskManagerIntegrationTest {
    private TaskManager taskManager;
    private Task testTask;

    @BeforeEach
    void setUp() {
        taskManager = TaskManager.getInstance();
        taskManager.clearAllTasks();
        testTask = new Task("Test Task", "Test Description", new Date(), 
                          1, Arrays.asList("test"), Color.RED);
    }

    @Nested
    @DisplayName("Persistence Tests")
    class PersistenceTests {
        @Test
        @DisplayName("Should persist and load task")
        void shouldPersistAndLoadTask() {
            // 保存任务
            taskManager.addTask(testTask);
            taskManager.sync();

            // 创建新的TaskManager实例并获取任务
            TaskManager newManager = TaskManager.getInstance();
            Task loadedTask = newManager.getTask(testTask.getTaskId());

            // 验证
            assertNotNull(loadedTask, "Should be able to load saved task");
            assertEquals(testTask.getTitle(), loadedTask.getTitle(), "Loaded task title should match");
        }
    }

    @Nested
    @DisplayName("Query Function Tests")
    class QueryTests {
        @Test
        @DisplayName("Should query tasks by tag")
        void shouldQueryTasksByTag() {
            taskManager.addTask(testTask);
            List<Task> tasks = taskManager.getTags(Arrays.asList("test"));
            
            assertAll("Tag Query Validation",
                () -> assertNotNull(tasks, "Query result should not be null"),
                () -> assertFalse(tasks.isEmpty(), "Should find tasks with specified tag"),
                () -> assertEquals(testTask.getTaskId(), tasks.get(0).getTaskId(), "Task ID should match")
            );
        }

        @Test
        @DisplayName("Should query tasks by date")
        void shouldQueryTasksByDate() {
            taskManager.addTask(testTask);
            List<Task> tasks = taskManager.getTaskByDate(testTask.getDueDate());
            
            assertAll("Date Query Validation",
                () -> assertNotNull(tasks, "Query result should not be null"),
                () -> assertFalse(tasks.isEmpty(), "Should find tasks for specified date"),
                () -> assertEquals(testTask.getTaskId(), tasks.get(0).getTaskId(), "Task ID should match")
            );
        }
    }

    @AfterEach
    void tearDown() {
        taskManager.clearAllTasks();
    }
} 