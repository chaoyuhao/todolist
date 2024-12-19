package com.todolist.integration.view;

import com.todolist.model.Task;
import com.todolist.model.TaskManager;
import com.todolist.view.TaskListView;
import org.junit.jupiter.api.*;
import javafx.scene.paint.Color;
import java.util.*;

@DisplayName("任务视图集成测试")
class TaskViewIntegrationTest {
    private TaskManager taskManager;
    private TaskListView taskListView;

    @BeforeEach
    void setUp() {
        // 初始化JavaFX环境
        // 注意：这里需要特殊处理JavaFX测试环境
        taskManager = TaskManager.getInstance();
        // taskListView = new TaskListView(...);
    }

    @Test
    @DisplayName("添加任务后视图应更新")
    void shouldUpdateViewWhenTaskAdded() {
        // 添加任务
        Task newTask = new Task("测试任务", "描述", new Date(), 
                              1, Arrays.asList("测试"), Color.RED);
        taskManager.addTask(newTask);

        // 验证视图更新
        // 注意：这里需要等待JavaFX线程
        // Platform.runLater(() -> {
        //     验证视图是否正确显示新任务
        // });
    }
} 