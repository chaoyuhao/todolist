package com.todolist;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.todolist.view.MainView;
import com.todolist.model.TaskManager;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // 初始化TaskManager
            TaskManager.getInstance();
            
            MainView mainView = new MainView();
            Scene scene = new Scene(mainView, 1200, 800);
            
            // 加载CSS样式
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            
            primaryStage.setTitle("ToDo List");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}