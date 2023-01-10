package com.company.todolistproject;

/**
 * Created by Rafal Zaborowski on 07.01.2023.
 */
public class ToDoItem {
    int id;
    String data;
    String text;
    boolean isDeleted;

    public ToDoItem(int id, String data, String text, boolean isDeleted) {
        this.id = id;
        this.data = data;
        this.text = text;
        this.isDeleted = isDeleted;
    }
}
