package com.example.simple_todo_list;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/* Main Program - JJ's To-Do List (Code copied and modified from simple to-do list) */

public class MainActivity extends AppCompatActivity {
    //Member variables
    ArrayList<String> items; //Will hold the tasks/items
    ArrayAdapter<String> itemsAdapter; //Simplifies displaying the items contents
    ListView lvItems; //Where items will be displayed
    FirebaseDatabase database; //Database to hold tasks
    DatabaseReference tasksReference; //Reference to tasks in dataBase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //Setting up variables
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //Setting up Firebase Database
        database = FirebaseDatabase.getInstance();
        tasksReference = database.getReference("tasks");
        //Invoking ListViewListener to delete items
        setUpListViewListener();
    }

    //OnClick method for the "add item" button
    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        //Add items to firebase
        String taskID = tasksReference.push().getKey(); //Unique ID for each task
        Task task = new Task(itemText, taskID);
        tasksReference.child(taskID).setValue(task);
    }

    //Set up a long click listener to delete entries after long click
    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Remove task from firebase
                removeTask(position);
                //Remove from GUI
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void removeTask(int position) {
        Task taskToRemove = tasks
    }

    /* --- Methods no longer needed due to Firebase Integration ---
    //Method to open a file and read a newline-delimited list of items
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException e){
            items = new ArrayList<String>();
        }
    }

    //Method to save a file and write a newline-delimited list of items
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    } */
}