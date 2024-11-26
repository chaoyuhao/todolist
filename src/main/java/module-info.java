module com.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires static lombok;

    exports com.todolist;
    exports com.todolist.view;
} 