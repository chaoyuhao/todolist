package com.todolist.view;

import com.todolist.model.TaskManager;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.todolist.model.Task;
import javafx.scene.paint.Color;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskListView extends VBox {
    private VBox activeTasksContainer;
    private VBox completedTasksContainer;
    private TitledPane completedTasksPane;
    private ObservableList<Task> tasks;
    private MainView mainView; // 添加 MainView 的引用

    public TaskListView(MainView mainView) { // 修改构造函数以接收 MainView 的引用
        this.mainView = mainView; // 保存 MainView 的引用
        this.tasks = FXCollections.observableArrayList();
        initializeComponents();
        setupLayout();
        loadTasks();
    }

    private void initializeComponents() {
        // 活动任务容器
        activeTasksContainer = new VBox(10);
        activeTasksContainer.getStyleClass().add("tasks-container");
        
        // 已完成任务容器
        completedTasksContainer = new VBox(10);
        completedTasksContainer.getStyleClass().add("tasks-container");
        
        // 创建可折叠的已完成任务面板
        completedTasksPane = new TitledPane("已完成任务", completedTasksContainer);
        completedTasksPane.setExpanded(false);
    }

    private void setupLayout() {
        setPadding(new Insets(20));
        setSpacing(20);
        
        // 标题
        Label titleLabel = new Label("任务列表");
        titleLabel.getStyleClass().add("task-list-title");
        
        // 返回按钮
        Button backButton = new Button("返回");
        backButton.setOnAction(e -> {
            // 返回到 MainView
            mainView.setCenter(new VBox()); // 清空中心内容
            mainView.refreshTasks(); // 刷新任务列表
            mainView.reloadButtons();
        });

        // 创建滚动面板
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(createTaskContainer());
        scrollPane.setFitToWidth(true); // 适应宽度
        scrollPane.setFitToHeight(true); // 适应高度

        getChildren().addAll(backButton, titleLabel, scrollPane);
    }

    private VBox createTaskContainer() {
        VBox taskContainer = new VBox(10);
        taskContainer.getChildren().addAll(activeTasksContainer, completedTasksPane);
        return taskContainer;
    }

    private void loadTasks() {
        // 从TaskManager获取任务并排序
        List<Task> sortedTasks = TaskManager.getInstance().getAllTasks().stream()
            .sorted(Comparator.comparing(Task::getDueDate)).sorted(Comparator.comparingInt(Task::getPriority))
            .collect(Collectors.toList());
            
        updateTaskDisplay(sortedTasks);
    }

    private void updateTaskDisplay(List<Task> taskList) {
        activeTasksContainer.getChildren().clear();
        completedTasksContainer.getChildren().clear();
        
        taskList.forEach(task -> {
            TaskItemView taskItem = new TaskItemView(task);
            if (task.getIsCompleted()) {
                completedTasksContainer.getChildren().add(taskItem);
            } else {
                activeTasksContainer.getChildren().add(taskItem);
            }
        });
    }

    // 内部类：任务项视图
    private class TaskItemView extends HBox {
        private Task task;
        
        public TaskItemView(Task task) {
            this.task = task;
            getStyleClass().add("task-item");
            
            // 设置任务颜色
            setStyle("-fx-background-color: " + toRGBCode(task.getDisplayColor()) + "20;"); // 20是透明度
            setPadding(new Insets(10));
            setSpacing(10);
            
            // 任务标题
            Label titleLabel = new Label(task.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold");
            
            // 到期时间
            Label dateLabel = new Label(formatDate(task.getDueDate()));
            
            // 完成按钮
            Button completeButton = new Button("✓");
            completeButton.getStyleClass().add("complete-button");
            completeButton.setOnAction(e -> completeTask(task));
            
            // 详情按钮
            Button detailsButton = new Button("详情");
            detailsButton.getStyleClass().add("details-button");
            detailsButton.setOnAction(e -> showTaskDetails(task));
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            getChildren().addAll(
                titleLabel, 
                dateLabel, 
                spacer,
                completeButton,
                detailsButton
            );
        }
        
        private void completeTask(Task task) {
            task.setIsCompleted(true);
            TaskManager.getInstance().sync();
            loadTasks(); // 重新加载任务列表

        }
        
        private void showTaskDetails(Task task) {
            TaskDetailView detailView = new TaskDetailView(task);
            detailView.showAndWait(); // 显示任务详情窗口
            loadTasks(); // 如果任务被修改，重新加载列表
        }
        
        private String toRGBCode(Color color) {
            return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        }
        
        private String formatDate(Date date) {
            // 实现日期格式化
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        }
    }
} 