package com.example.multiplycalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigInteger;
/* MVC- CONTROLLER CLASS */

/* This file acts as the Controller to the MVC Pattern, communicating between the GUI
and the model class */

public class MainActivity extends AppCompatActivity {

    Model model = new Model(); //Creating Model Object
    String reset = "1"; //The initial value when clearing the calculator

    //Pre-made function by Android Studio
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
    }

    public void OnClickMultiply(View view) {
        //Getting the bottom total text to later modify
        TextView totalTextBox = (TextView) findViewById(R.id.total);
        //Retrieving the current value of the input box as a String
        EditText input = (EditText) findViewById(R.id.editTextNumber);
        String inputText = input.getText().toString();

        //Converting the string inputText into an int
        int inputValue;
        try {
            inputValue = Integer.parseInt(inputText);
        } catch (NumberFormatException e) { //catching if the text is null
            inputValue = 1;
        }
        //Getting the total from the model
        BigInteger total = model.multiply(inputValue);

        //Converting total to a String to set text
        String totalString = total.toString();

        //Setting the value of totalTextBox to total
        totalTextBox.setText(totalString); //setting as inputText for testing
    }

    public void OnClickClear(View view) {
        //Getting the bottom total text to later modify
        TextView totalTextBox = (TextView) findViewById(R.id.total);

        //Clear the total from the model
        model.clear();

        //Set the total in the GUI to 1 (the new total)
        totalTextBox.setText(reset);
    }
}