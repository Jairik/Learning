package com.example.coffeeshop;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    boolean hasChocolate = false, hasCream = false; //Booleans for additions
    //Defining different components that will be modified
    CheckBox cream;
    CheckBox chocolate;
    TextView totalInfo;
    TextView qty;
    Button incrementQty;
    Button decrementQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Find all components
        
    }

    //Calculate the total and set the Order_Summary text
    public void Order(View view) {

    }

    //Increment the quantity count and update the text field
    public void IncrementQuantity(View view) {

    }

    public void DecrementQuantity(View view) {

    }

    /* Helper function for Order(), will determine the status of the checkboxes and update
    * the corresponding booleans accordingly*/
    public void checkCheckBoxes(View view) {

    }


}