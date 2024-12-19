package com.todolist.unit.model;

import com.todolist.model.Task;
import org.junit.jupiter.api.*;
import javafx.scene.paint.Color;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Class Unit Tests")
class TaskTest {
    private Task task;
    private final String VALID_TITLE = "Test Task";
    private final String VALID_DESCRIPTION = "This is a test task";
    private final Date VALID_DUE_DATE = new Date();
    private final int VALID_PRIORITY = 1;
    private final List<String> VALID_TAGS = Arrays.asList("work", "important");
    private final Color VALID_COLOR = Color.RED;

    @BeforeEach
    void setUp() {
        task = new Task(VALID_TITLE, VALID_DESCRIPTION, VALID_DUE_DATE, 
                       VALID_PRIORITY, VALID_TAGS, VALID_COLOR);
    }

    @Nested
    @DisplayName("Basic Property Tests")
    class BasicPropertyTests {
        @Test
        @DisplayName("Properties should be set correctly when creating task")
        void shouldSetPropertiesCorrectly() {
            assertAll("Task Property Validation",
                () -> assertNotNull(task.getTaskId(), "Task ID should not be null"),
                () -> assertEquals(VALID_TITLE, task.getTitle(), "Title should match"),
                () -> assertEquals(VALID_DESCRIPTION, task.getDescription(), "Description should match"),
                () -> assertEquals(VALID_PRIORITY, task.getPriority(), "Priority should match")
            );
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {
        @Test
        @DisplayName("New task should be incomplete")
        void shouldBeIncompleteWhenCreated() {
            assertFalse(task.getIsCompleted(), "New task should be incomplete");
        }

        @Test
        @DisplayName("Status should update when marked as complete")
        void shouldBeCompleteWhenMarked() {
            task.markAsComplete();
            assertTrue(task.getIsCompleted(), "Task should be marked as complete");
        }
    }
} 