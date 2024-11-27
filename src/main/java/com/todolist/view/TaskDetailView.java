package com.todolist.view;

import com.todolist.model.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class TaskDetailView extends Stage {
    public TaskDetailView(Task task) {
        initializeComponents(task);
        setupWindow();
    }

    private void initializeComponents(Task task) {
        VBox mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(20));

        // 任务标题
        Label titleLabel = new Label("任务标题: " + task.getTitle());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 任务描述
        Label descriptionLabel = new Label("描述: " + task.getDescription());

        // 到期日期
        Label dueDateLabel = new Label("到期日期: " + task.getDueDate());

        // 优先级
        Label priorityLabel = new Label("优先级: " + task.getPriority());

        // 循环任务状态
        Label recurringLabel = new Label("循环任务: " + (task.getIsRecurring() ? "是" : "否"));

        // 完成状态
        Label completedLabel = new Label("完成状态: " + (task.getIsCompleted() ? "已完成" : "未完成"));

        // 关闭按钮
        Button closeButton = new Button("关闭");
        closeButton.setOnAction(e -> close());

        mainContainer.getChildren().addAll(titleLabel, descriptionLabel, dueDateLabel, priorityLabel, recurringLabel, completedLabel, closeButton);
        Scene scene = new Scene(mainContainer, 300, 250);
        setScene(scene);
    }

    private void setupWindow() {
        setTitle("任务详情");
        setResizable(false);
    }
} 