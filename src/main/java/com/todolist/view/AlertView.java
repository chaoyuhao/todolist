package com.todolist.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import com.todolist.model.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.List;

public class AlertView extends Stage {
    public AlertView(List<Task> tasks) {
        initializeComponents(tasks);
        setupWindow();
    }

    private void initializeComponents(List<Task> tasks) {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_LEFT);

        for (Task task : tasks) {
            VBox taskBox = new VBox(5);
            taskBox.setPadding(new Insets(10));
            taskBox.setStyle("-fx-background-color: white; -fx-background-radius: 5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 0);");

            Label titleLabel = new Label("标题: " + task.getTitle());
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label timeLabel = new Label("时间: " + task.getDueDate().toString());
            timeLabel.setStyle("-fx-font-size: 14px;");

            Rectangle colorRect = new Rectangle(20, 20);
            colorRect.setFill(task.getColor());

            taskBox.getChildren().addAll(titleLabel, timeLabel, colorRect);
            mainContainer.getChildren().add(taskBox);
        }

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox scrollPaneScene = new VBox();
        scrollPaneScene.getChildren().add(scrollPane);

        Scene scene = new Scene(scrollPaneScene, 400, 600);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        setScene(scene);
    }

    private void setupWindow() {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("任务警报");
        setResizable(false);
    }
}