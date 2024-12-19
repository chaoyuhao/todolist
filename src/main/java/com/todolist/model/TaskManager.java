package com.todolist.model;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import javafx.scene.paint.Color;
import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.stream.Collectors;

public class TaskManager {
    private static TaskManager instance;
    private List<Task> tasks;
    private static final String SAVE_PATH = "src/main/resources/data/taskList.json";
    private final Gson gson;

    private TaskManager() {
        tasks = new ArrayList<>();
        // 创建支持@Expose注解的Gson实例
        gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation() // 只序列化带@Expose注解的字段
            .registerTypeAdapter(Task.class, new TaskSerializer())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .registerTypeAdapter(Color.class, new ColorTypeAdapter())
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .setPrettyPrinting()
            .create();
        loadTasks();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    private void loadTasks() {
        try {
            // 确保目录存在
            File directory = new File(SAVE_PATH).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 如果文件不存在，创建空文件
            File file = new File(SAVE_PATH);
            if (!file.exists()) {
                file.createNewFile();
                tasks = new ArrayList<>();
                return;
            }

            // 读取文件内容
            String json = new String(Files.readAllBytes(Paths.get(SAVE_PATH)));
            if (json.trim().isEmpty()) {
                tasks = new ArrayList<>();
                return;
            }

            // 将JSON转换为任务列表
            Type listType = new com.google.gson.reflect.TypeToken<ArrayList<Task>>(){}.getType();
            tasks = gson.fromJson(json, listType);
            if (tasks == null) {
                tasks = new ArrayList<>();
            }

        } catch (IOException e) {
            e.printStackTrace();
            tasks = new ArrayList<>();
        }
    }

    public void sync() {
        try {
            String json = gson.toJson(tasks);
            Files.write(Paths.get(SAVE_PATH), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
        sync();
    }

    public void removeTask(String taskId) {
        tasks.removeIf(task -> task.getTaskId().equals(taskId));
        sync();
    }

    public Task getTask(String taskId) {
        return tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getRecurringTasks() {
        return tasks.stream()
                .filter(Task::getIsRecurring)
                .collect(Collectors.toList());
    }

    public List<Task> getTaskByDate(Date date) {
        return tasks.stream()
                .filter(task -> {
                    Calendar cal1 = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    cal1.setTime(task.getDueDate());
                    cal2.setTime(date);
                    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
                })
                .collect(Collectors.toList());
    }

    public List<Task> getTasksForDate(LocalDate date) {
        return tasks.stream()
                .filter(task -> {
                    LocalDate taskDate = task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return taskDate.isEqual(date);
                })
                .collect(Collectors.toList());
    }

    // Task序列化器
    private static class TaskSerializer implements JsonSerializer<Task> {
        @Override
        public JsonElement serialize(Task task, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            
            jsonObject.addProperty("taskId", task.getTaskId());
            jsonObject.addProperty("title", task.getTitle());
            jsonObject.addProperty("description", task.getDescription());
            jsonObject.add("dueDate", context.serialize(task.getDueDate()));
            jsonObject.addProperty("isRecurring", task.getIsRecurring());
            jsonObject.addProperty("isCompleted", task.getIsCompleted());
            jsonObject.addProperty("priority", task.getPriority());
            jsonObject.add("tags", context.serialize(task.getTags()));
            jsonObject.add("displayColor", context.serialize(task.getDisplayColor()));
            
            return jsonObject;
        }
    }

    // Task反序列化器
    private static class TaskDeserializer implements JsonDeserializer<Task> {
        @Override
        public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            
            String title = jsonObject.get("title").getAsString();
            String description = jsonObject.get("description").getAsString();
            Date dueDate = context.deserialize(jsonObject.get("dueDate"), Date.class);
            int priority = jsonObject.get("priority").getAsInt();
            Color displayColor = context.deserialize(jsonObject.get("displayColor"), Color.class);
            List<String> init_tag = new ArrayList<>();
            init_tag.add("Default");
            Task task = new Task(title, description, dueDate, priority, init_tag, displayColor);
            
            // 设置其他属性
            task.setIsRecurring(jsonObject.get("isRecurring").getAsBoolean());
            task.setIsCompleted(jsonObject.get("isCompleted").getAsBoolean());
            
            if (jsonObject.has("tags")) {
                Type listType = new TypeToken<List<String>>(){}.getType();
                List<String> tags = context.deserialize(jsonObject.get("tags"), listType);
                task.setTags(tags);
            }
            
            return task;
        }
    }

    // 用于处理Color类的序列化和反序列化
    private static class ColorTypeAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
        @Override
        public JsonElement serialize(Color color, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)));
        }

        @Override
        public Color deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            String colorStr = json.getAsString();
            return Color.web(colorStr);
        }
    }

    // 用于处理Date类的序列化和反序列化
    private static class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        @Override
        public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(date.getTime());
        }

        @Override
        public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            return new Date(json.getAsLong());
        }
    }

    public List<Task> getAlart() {
        loadTasks();
        System.out.println("alart task->" + tasks);

        return tasks.stream()
            .filter(task -> {
                LocalDate taskDate = task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return taskDate.isEqual(LocalDate.now());
            }).filter(task -> {
                    return !task.getIsCompleted();
                })
            .collect(Collectors.toList());
    }

    public List<Task> getTags(List<String> tags) {
        if(tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        
        String name = tags.get(0);
        return tasks.stream()
            .filter(task -> task.getTags() != null && task.getTags().contains(name))
            .collect(Collectors.toList());
    }

    public void completeTask(String taskId) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setIsCompleted(true);
            sync();
        }
    }

    public void uncompleteTask(String taskId) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setIsCompleted(false);
            sync();
        }
    }

    public void updateTask(Task task) {
        Task oldTask = getTask(task.getTaskId());
        if (oldTask != null) {
            oldTask.setTitle(task.getTitle());
            oldTask.setDescription(task.getDescription());
            oldTask.setDueDate(task.getDueDate());
            oldTask.setPriority(task.getPriority());
            oldTask.setDisplayColor(task.getDisplayColor());
            oldTask.setIsRecurring(task.getIsRecurring());
            oldTask.setTags(task.getTags());
            sync();
        }
    }
    
    public void clearCompletedTasks() {
        tasks.removeIf(Task::getIsCompleted);
        sync();
    }

    public void clearAllTasks() {
        tasks.clear();
        sync();
    }

    public void updateTaskTags(String taskId, List<String> tags) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setTags(tags);
            sync();
        }
    }

    public void updateTaskColor(String taskId, Color color) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setDisplayColor(color);
            sync();
        }
    }

    public void updateTaskRecurring(String taskId, boolean isRecurring) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setIsRecurring(isRecurring);
            sync();
        }
    }

    public void updateTaskPriority(String taskId, int priority) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setPriority(priority);
            sync();
        }
    }

    public void updateTaskDueDate(String taskId, Date dueDate) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setDueDate(dueDate);
            sync();
        }
    }

    public void updateTaskDescription(String taskId, String description) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setDescription(description);
            sync();
        }
    }

    public void updateTaskTitle(String taskId, String title) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setTitle(title);
            sync();
        }
    }

    public void updateTaskCompletion(String taskId, boolean isCompleted) {
        Task task = getTask(taskId);
        if (task != null) {
            task.setIsCompleted(isCompleted);
            sync();
        }
    }

    
} 