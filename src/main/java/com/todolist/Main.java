package com.todolist;

import com.todolist.view.*;
import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.todolist.model.TaskManager;

public class Main extends Application {
    private static final int STATE_MAIN_VIEW = 0;
    private static final int STATE_TASK_LIST = 1;
    private static final int STATE_CALENDAR = 2;

    private TaskManager taskManager;
    private MainView mainView;

    @Override
    public void start(Stage primaryStage) {
        try {
            // 初始化TaskManager
            taskManager = TaskManager.getInstance();
            mainView = new MainView();

//            // 设置按钮事件
//            mainView.getAddTaskButton().setOnAction(e -> {
//                // 处理添加任务的逻辑
//                AddTaskView addTaskView = new AddTaskView();
//                addTaskView.showAndWait();
//                // 刷新任务列表
//                mainView.refreshTasks();
//            });



            mainView.getTaskListButton().setOnAction(e -> {
                // 切换到任务列表状态
                setState(STATE_TASK_LIST);
            });

            mainView.getCalendarButton().setOnAction(e -> {
                // 切换到日历状态
                setState(STATE_CALENDAR);
            });

            Scene scene = new Scene(mainView, 1200, 800);
            // 加载CSS样式
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

            primaryStage.setTitle("ToDo List");
            primaryStage.setScene(scene);
            primaryStage.show();

            // 检查是否有警报任务
            if (!taskManager.getAlart().isEmpty()) {
                AlertView alertView = new AlertView(taskManager.getAlart());
                alertView.showAndWait();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setState(int state) {
        switch (state) {
            case STATE_MAIN_VIEW:
                mainView.setCenter(new VBox()); // 清空中心内容
                break;
            case STATE_TASK_LIST:
                TaskListView taskListView = new TaskListView(mainView);
                mainView.setCenter(taskListView);
                break;
            case STATE_CALENDAR:
                CalendarView calendarView = new CalendarView(mainView);
                mainView.setCenter(calendarView); // 切换到日历视图
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}