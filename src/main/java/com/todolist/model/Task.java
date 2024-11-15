package com.todolist.model;

import javafx.scene.paint.Color;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Task {
    private String taskId;
    private String title;
    private String description;
    private Date dueDate;
    private Boolean isRecurring;
    private Boolean isCompleted;
    private int priority;
    private List<String> tags;
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
    
    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Boolean getIsRecurring() {
        return isRecurring;
    }
    
    public void setIsRecurring(Boolean recurring) {
        isRecurring = recurring;
    }
    
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(Boolean completed) {
        isCompleted = completed;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public Color getDisplayColor() {
        return displayColor;
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
} 