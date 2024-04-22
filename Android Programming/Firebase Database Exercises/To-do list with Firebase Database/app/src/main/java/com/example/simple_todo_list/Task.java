package com.example.simple_todo_list;

/* Tasks class to interact with firebase database */

import com.google.firebase.database.DatabaseReference;

public class Task {
    String item;
    String uniqueID;

    public Task(String item, String uniqueID) {
        this.item = item;
        this.uniqueID = uniqueID;
    }

    public String getItem() {
        return item;
    }

    public String getUniqueID() {
        return uniqueID;
    }

}
