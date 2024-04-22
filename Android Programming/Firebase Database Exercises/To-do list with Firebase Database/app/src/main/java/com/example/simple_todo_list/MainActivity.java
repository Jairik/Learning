package com.example.simple_todo_list;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    ArrayList<String> itemKeys; //List of keys of items in database

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
        itemKeys = new ArrayList<>();
        //Invoking ListViewListener to delete items
        setUpListViewListener();
        loadItems();
    }

    //OnClick method for the "add item" button
    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if(!itemText.isEmpty()) { //Checking to ensure tasks edit field is not empty
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            //Add items to firebase
            String taskID = tasksReference.push().getKey(); //Unique ID for each task
            itemKeys.add(taskID); //Add key to list of keys
            tasksReference.child(taskID).setValue(itemText); //Add item to database
        }
    }

    //Set up a long click listener to delete entries after long click
    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Remove task from firebase
                String keyToDelete = itemKeys.get(position);
                DatabaseReference itemToDelete = tasksReference.child(keyToDelete);
                itemToDelete.removeValue();
                itemKeys.remove(position); //Remove key from itemKeys list
                //Remove from GUI
                items.remove(position); //Remove item from item list
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    //Loads the items for the to-do list from firebase
    private void loadItems() {
        tasksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                itemKeys.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String curItem = snapshot.getValue(String.class);
                    items.add(curItem); //Adding to items list
                    itemKeys.add(snapshot.getKey());
                }
                itemsAdapter.notifyDataSetChanged();
            }

            //Handling error case when item cannot be read
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read item" + error.toException());
            }
        });
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