package com.todolist.view;

import com.todolist.model.Task;
import com.todolist.model.TaskManager;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainView extends BorderPane {
    private Label titleLabel;
    private Label timeLabel;
    private Button calendarButton;
    private Button addTaskButton;
    private Button taskListButton;
    
    public MainView() {
        initializeComponents();
        setupLayout();
        startClock();
    }
    
    private void initializeComponents() {
        // 初始化标题
        titleLabel = new Label("TODO List");
        titleLabel.getStyleClass().add("main-title");
        
        // 初始化时间显示
        timeLabel = new Label();
        timeLabel.getStyleClass().add("time-label");
        
        // 初始化按钮
        calendarButton = new Button("Calendar");
        addTaskButton = new Button("Add Task");
        taskListButton = new Button("Task List");
        
        // 添加样式类
        calendarButton.getStyleClass().add("main-button");
        addTaskButton.getStyleClass().add("main-button");
        taskListButton.getStyleClass().add("main-button");
        
        // 为按钮添加点击事件
        addTaskButton.setOnAction(e -> {
            AddTaskView addTaskView = new AddTaskView();
            addTaskView.showAndWait();
            
            Task newTask = addTaskView.getTask();
            if (newTask != null) {
                // TODO: 处理新创建的任务
                System.out.println("新建任务: " + newTask);
                TaskManager.getInstance().addTask(newTask);
            }
        });

        taskListButton.setOnAction(e -> {
            TaskListView taskListView = new TaskListView(this);
            setCenter(taskListView);
        });

        // 为日历按钮添加事件（如果需要）
        calendarButton.setOnAction(e -> {
            // TODO: 添加日历视图的逻辑
        });
    }
    
    private void setupLayout() {
        // 创建顶部容器
        VBox topContainer = new VBox(10); // 10px spacing
        topContainer.getStyleClass().add("top-container");
        topContainer.getChildren().addAll(titleLabel, timeLabel);
        
        // 创建按钮容器
        HBox buttonContainer = new HBox(10); // 10px spacing
        buttonContainer.getChildren().addAll(calendarButton, addTaskButton, taskListButton);
        buttonContainer.setAlignment(Pos.CENTER); // 居中对齐按钮
        
        // 设置布局
        setTop(topContainer);
        setCenter(buttonContainer); // 将按钮容器设置为中心内容
        
        // 添加整体样式
        getStyleClass().add("main-view");
    }
    
    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            timeLabel.setText(now.format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }
    
    // Getter方法，用于后续添加事件处理
    public Button getCalendarButton() {
        return calendarButton;
    }
    
    public Button getAddTaskButton() {
        return addTaskButton;
    }
    
    public Button getTaskListButton() {
        return taskListButton;
    }
} 