package com.todolist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
public class Task {
    // Getters and Setters
    @Expose
    @SerializedName("taskId")
    private String taskId;
    
    @Expose
    @SerializedName("title")
    private String title;
    
    @Expose
    @SerializedName("description")
    private String description;
    
    @Expose
    @SerializedName("dueDate")
    private Date dueDate;
    
    @Expose
    @SerializedName("isRecurring")
    private Boolean isRecurring;
    
    @Expose
    @SerializedName("isCompleted")
    private Boolean isCompleted;
    
    @Expose
    @SerializedName("priority")
    private int priority;
    
    @Expose
    @SerializedName("tags")
    private List<String> tags;
    
    @Expose
    @SerializedName("displayColor")
    private Color displayColor;
    
    // 构造函数
    public Task(String title, String description, Date dueDate, int priority, Color displayColor) {
        this.taskId = UUID.randomUUID().toString(); // 生成唯一ID
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.displayColor = displayColor;
        this.isRecurring = false;
        this.isCompleted = false;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setIsRecurring(Boolean recurring) {
        isRecurring = recurring;
    }

    public void setIsCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setDisplayColor(Color displayColor) {
        this.displayColor = displayColor;
    }
    
    // 业务方法
    public void markAsComplete() {
        this.isCompleted = true;
    }
    
    public void updateTask(String title, String description, Date dueDate, 
                         Boolean isRecurring, int priority, List<String> tags, Color displayColor) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isRecurring = isRecurring;
        this.priority = priority;
        this.tags = tags;
        this.displayColor = displayColor;
    }
    
    public Task getDetails() {
        return this;
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", title='" + title + '\'' +
                ", dueDate=" + dueDate +
                ", isCompleted=" + isCompleted +
                ", priority=" + priority +
                '}';
    }

    public Paint getColor() {
        return this.displayColor;
    }
}