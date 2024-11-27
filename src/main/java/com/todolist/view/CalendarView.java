package com.todolist.view;

import com.todolist.model.Task;
import com.todolist.model.TaskManager;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarView extends BorderPane {
    private GridPane calendarGrid;
    private LocalDate currentDate;
    private Label monthLabel;
    private MainView mainView;

    public CalendarView(MainView mainView) {
        this.currentDate = LocalDate.now(); // 获取当前日期
        this.mainView = mainView;
        initializeCalendar();
    }

    private void initializeCalendar() {
        calendarGrid = new GridPane();
        calendarGrid.setHgap(10); // 设置列间距
        calendarGrid.setVgap(10); // 设置行间距
        calendarGrid.setPadding(new Insets(10)); // 设置网格内边距

        // 设置月份标签
        monthLabel = new Label();
        monthLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        updateMonthLabel();

        // 创建左右切换按钮
        Button prevButton = new Button("<");
        Button nextButton = new Button(">");
        prevButton.setOnAction(e -> changeMonth(-1));
        nextButton.setOnAction(e -> changeMonth(1));

        // 创建顶部容器
        HBox topContainer = new HBox(10, prevButton, monthLabel, nextButton);
        topContainer.setAlignment(Pos.CENTER);
        setTop(topContainer);

        // 设置星期标题
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < weekDays.length; i++) {
            Label dayLabel = new Label(weekDays[i]);
            calendarGrid.add(dayLabel, i, 0); // 添加星期标题
        }

        // 填充日历格子
        fillCalendarGrid();

        // 使用 VBox 将日历居中
        VBox centerContainer = new VBox(calendarGrid);
        centerContainer.setAlignment(Pos.CENTER); // 设置居中对齐
        setCenter(centerContainer); // 将居中容器设置为中心内容

        // 添加返回按钮
        Button backButton = new Button("返回");
        backButton.setOnAction(e -> {
            // 返回到 MainView
            mainView.setCenter(new VBox());
            mainView.reloadButtons(); // 重新加载按钮
        });

        // 将返回按钮放置在右上角
        HBox rightContainer = new HBox(backButton);
        rightContainer.setAlignment(Pos.CENTER_RIGHT);
        rightContainer.setPadding(new Insets(20)); // 设置内边距
        setRight(rightContainer); // 将右侧容器设置为右边内容

        // 创建一个 ScrollPane 并将日历网格添加到其中
        ScrollPane scrollPane = new ScrollPane(calendarGrid);
        scrollPane.setFitToWidth(true); // 适应宽度
        scrollPane.setFitToHeight(true); // 适应高度

        // 使用 VBox 将 ScrollPane 居中
        VBox cc = new VBox(scrollPane);
        cc.setAlignment(Pos.CENTER); // 设置居中对齐
        setCenter(cc); // 将居中容器设置为中心内容
    }

    private void fillCalendarGrid() {
        // 清空之前的内容
        calendarGrid.getChildren().clear();

        // 重新添加星期标题
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < weekDays.length; i++) {
            Label dayLabel = new Label(weekDays[i]);
            calendarGrid.add(dayLabel, i, 0); // 添加星期标题
        }

        // 获取当前月份的第一天和最后一天
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        // 获取该月的第一天是星期几
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7; // 将星期一作为 0

        // 填充日历格子
        int dayCounter = 1;
        for (int row = 1; row < 7; row++) { // 6 行
            for (int col = 0; col < 7; col++) { // 7 列
                if (row == 1 && col < startDayOfWeek) {
                    // 在第一行填充空格
                    calendarGrid.add(new Label(""), col, row);
                } else if (dayCounter <= lastDayOfMonth.getDayOfMonth()) {
                    // 添加日期
                    Label dateLabel = new Label(String.valueOf(dayCounter));
                    dateLabel.setStyle("-fx-border-color: black; -fx-padding: 5; -fx-background-color: lightgray; -fx-min-width: 100; -fx-min-height: 100;"); // 设置样式
                    dateLabel.setAlignment(Pos.TOP_LEFT); // 日期数字左上角对齐
                    calendarGrid.add(dateLabel, col, row);

                    // 显示任务
                    displayTasksForDate(LocalDate.of(currentDate.getYear(), currentDate.getMonth(), dayCounter), dateLabel);

                    dayCounter++;
                }
            }
        }
    }

    private void displayTasksForDate(LocalDate date, Label dateLabel) {
        // 从 TaskManager 获取该日期的任务
        List<Task> tasks = TaskManager.getInstance().getTasksForDate(date);
        StringBuilder taskTitles = new StringBuilder();

        for (Task task : tasks) {
            if(task.getIsCompleted()) continue;
            taskTitles.append(task.getTitle()).append(" "); // 将任务标题添加到字符串中
        }

        // 如果有任务，显示在日期格子中
        if (taskTitles.length() > 0) {
            Text taskText = new Text(taskTitles.toString());
            taskText.setFill(Color.BLACK);
            taskText.setWrappingWidth(60); // 设置文本换行宽度

            // 如果任务文本超过一定长度，显示省略号
            if (taskTitles.length() > 30) { // 假设100字符为限制
            taskText.setText(taskTitles.substring(0, 30) + "...\n");
            }

            dateLabel.setText(date.getDayOfMonth() + "\n"); // 只显示日期数字
            dateLabel.setGraphic(taskText); // 将任务文本设置为标签的图形
        }
    }

    private void changeMonth(int increment) {
        currentDate = currentDate.plusMonths(increment); // 改变月份
        updateMonthLabel(); // 更新月份标签
        fillCalendarGrid(); // 重新填充日历
    }

    private void updateMonthLabel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentDate.format(formatter)); // 更新月份名称
    }
} 