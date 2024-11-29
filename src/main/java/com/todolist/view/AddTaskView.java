package com.todolist.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import com.todolist.model.Task;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTaskView extends Stage {
    private TextField titleField;
    private DatePicker datePicker;
    private ComboBox<LocalTime> hourComboBox, minuteComboBox;
    private TextArea descriptionArea;
    private ComboBox<String> colorComboBox;
    private VBox tagsContainer;
    private List<String> tags;
    private Task resultTask;
    private CheckBox isRecurringCheckBox;
    private TextField recurringDaysField;
    private Slider prioritySlider;

    public AddTaskView() {
        this.tags = new ArrayList<>();
        initializeComponents();
        setupLayout();
        setupWindow();
    }

    private void initializeComponents() {
        // 标题输入
        titleField = new TextField();
        titleField.setPromptText("输入任务标题");
        titleField.getStyleClass().add("task-input");

        // 日期选择
        datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("task-date-picker");

        // 时间选择
        hourComboBox = new ComboBox<>();
        minuteComboBox = new ComboBox<>();
        for (int i=0; i<24; i++) {
            hourComboBox.getItems().add(LocalTime.of(i, 0));
        }
        for (int i=0; i<60; i++) {
            minuteComboBox.getItems().add(LocalTime.of(0, i));
        }
        hourComboBox.getStyleClass().add("task-time-picker");
        minuteComboBox.getStyleClass().add("task-time-picker");


        // 描述输入
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("输入任务描述");
        descriptionArea.setPrefRowCount(6);
        descriptionArea.setWrapText(true);
        descriptionArea.getStyleClass().add("task-description");

        // 颜色选择
        String[] colors = {"#4CAF50", "#2196F3", "#FFC107", "#E91E63", "#9C27B0"};
        colorComboBox = new ComboBox<>(FXCollections.observableArrayList(colors));
        colorComboBox.setValue(colors[0]);
        colorComboBox.setCellFactory(list -> new ColorCell());
        colorComboBox.setButtonCell(new ColorCell());
        colorComboBox.getStyleClass().add("task-color-picker");

        // Tags容器
        tagsContainer = new VBox(5);
        tagsContainer.getStyleClass().add("tags-container");

        // 重复任务选择
        isRecurringCheckBox = new CheckBox("循环任务");
        recurringDaysField = new TextField();
        recurringDaysField.setPromptText("循环天数");
        recurringDaysField.setDisable(true);
        recurringDaysField.getStyleClass().add("task-input");

        // 设置循环任务复选框的监听器
        isRecurringCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            recurringDaysField.setDisable(!newVal);
        });

        // 优先级滑动条
        prioritySlider = new Slider(0, 10, 0); // 设置滑动条范围为0到10，初始值为0
        prioritySlider.setShowTickLabels(true);
        prioritySlider.setShowTickMarks(true);
        prioritySlider.setMajorTickUnit(1);
        prioritySlider.setBlockIncrement(1);
        
        // 优先级标签
        Label priorityLabel = new Label("优先级: " + (int) prioritySlider.getValue());
        prioritySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            priorityLabel.setText("优先级: " + newVal.intValue());
        });

        // 只允许输入整数
        recurringDaysField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("-?\\d*")) {
                recurringDaysField.setText(oldVal);
            }
        });
    }

    private void setupLayout() {
        VBox mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(20));
        mainContainer.getStyleClass().add("add-task-container");

        // 标题部分
        Label titleLabel = new Label("任务标题");
        mainContainer.getChildren().addAll(titleLabel, titleField);

        // 日期和时间部分
        HBox dateTimeBox = new HBox(10);
        VBox dateBox = new VBox(5);
        VBox hourBox = new VBox(3);
        VBox minuteBox = new VBox(3);
        
        dateBox.getChildren().addAll(new Label("日期"), datePicker);
        hourBox.getChildren().addAll(new Label("小时"), hourComboBox);
        minuteBox.getChildren().addAll(new Label("分钟"), minuteComboBox);

        dateTimeBox.getChildren().addAll(dateBox, hourBox, minuteBox);
        mainContainer.getChildren().add(dateTimeBox);

        // 描述部分
        mainContainer.getChildren().addAll(new Label("描述"), descriptionArea);

        // 颜色选择部分
        mainContainer.getChildren().addAll(new Label("颜色"), colorComboBox);

        // Tags部分
        HBox tagInputBox = new HBox(10);
        TextField tagInput = new TextField();
        tagInput.setPromptText("添加标签");
        Button addTagButton = new Button("+");
        addTagButton.setOnAction(e -> {
            addTag(tagInput.getText());
            tagInput.setText("");
        });
        tagInputBox.getChildren().addAll(tagInput, addTagButton);

        mainContainer.getChildren().addAll(new Label("标签"), tagInputBox, tagsContainer);

        // 循环任务设置部分
        HBox recurringBox = new HBox(10);
        VBox recurringContainer = new VBox(5);
        recurringContainer.getChildren().addAll(isRecurringCheckBox, recurringDaysField);
        recurringBox.getChildren().add(recurringContainer);
        mainContainer.getChildren().add(recurringBox);

        // 优先级部分
        mainContainer.getChildren().addAll(prioritySlider, new Label("优先级: 0"));

        // 确认和取消按钮
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button confirmButton = new Button("确认");
        Button cancelButton = new Button("取消");
        
        confirmButton.setOnAction(e -> createTask());
        cancelButton.setOnAction(e -> close());
        
        buttonBox.getChildren().addAll(cancelButton, confirmButton);
        mainContainer.getChildren().add(buttonBox);

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("add-task-scroll-pane");

        VBox scrollPaneScene = new VBox();
        scrollPaneScene.getChildren().add(scrollPane);

        // 设置场景
        Scene scene = new Scene(scrollPaneScene, 400, 600);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        setScene(scene);
    }

    private void setupWindow() {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("添加新任务");
        setResizable(false);
    }

    private void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty() && tags.size() < 5 && !tags.contains(tag)) {
            System.out.println("add tags->" + tag);
            tags.add(tag);
            updateTagsDisplay();
        }else {
            showAlert("非法标签");
        }
    }

    private void updateTagsDisplay() {
        tagsContainer.getChildren().clear();
        for (String tag : tags) {
            HBox tagBox = new HBox(5);
            Label tagLabel = new Label(tag);
            Button removeButton = new Button("×");
            removeButton.setOnAction(e -> {
                tags.remove(tag);
                updateTagsDisplay();
            });
            tagBox.getChildren().addAll(tagLabel, removeButton);
            tagsContainer.getChildren().add(tagBox);
        }
    }

    private void createTask() {
        if (titleField.getText().trim().isEmpty()) {
            showAlert("请输入任务标题");
            return;
        }

        // 验证循环天数
        if (isRecurringCheckBox.isSelected() && recurringDaysField.getText().trim().isEmpty()) {
            showAlert("请输入循环天数");
            return;
        }

        // 获取优先级，默认为0
        int priority = (int) prioritySlider.getValue(); // 从滑动条获取优先级

        LocalDate date = datePicker.getValue();
        LocalTime hour = hourComboBox.getValue();
        LocalTime min = minuteComboBox.getValue();
        Date dueDate = Date.from(date.atTime(LocalTime.of(hour.getHour(), min.getMinute()))
                .atZone(ZoneId.systemDefault())
                .toInstant());


        System.out.println("新建任务" + tags);
        resultTask = new Task(
            titleField.getText(),
            descriptionArea.getText(),
            dueDate,
            priority,
            tags,
            Color.web(colorComboBox.getValue())
        );
        
        resultTask.setIsRecurring(isRecurringCheckBox.isSelected());
        if (isRecurringCheckBox.isSelected()) {
            // TODO: 处理循环天数，这里需要在Task类中添加相应的属性
            System.out.println("循环天数: " + recurringDaysField.getText());
        }
        
        close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Task getTask() {
        // TODO : 处理任务的获取
        System.out.println("获取任务" + resultTask);
        return resultTask;
    }

    // 用于颜色选择框的自定义单元格
    private static class ColorCell extends ListCell<String> {
        @Override
        protected void updateItem(String color, boolean empty) {
            super.updateItem(color, empty);
            if (empty || color == null) {
                setText(null);
                setGraphic(null);
            } else {
                Rectangle rect = new Rectangle(20, 20);
                rect.setFill(Color.web(color));
                setGraphic(rect);
            }
        }
    }
} 