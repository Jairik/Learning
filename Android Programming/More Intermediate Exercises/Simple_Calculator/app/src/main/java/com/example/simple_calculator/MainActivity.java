package com.example.simple_calculator;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/* Author: JJ McCauley
 * Date of Last Update: 4/15/24
 * Interacts with the view and SimpleExpression class to make a simple calculator */
public class MainActivity extends AppCompatActivity {

    private TextView numberDisplay;
    private SimpleExpression expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); */
        //Getting the number display and making a SimpleExpression object
        numberDisplay = (TextView) findViewById(R.id.output);
        expression = new SimpleExpression();
    }


    public void clear(View view) {
        expression.clearAll();
        numberDisplay.setText("0");
    }

    //Handling operands (number buttons)
    public void operand(View view) {
        String val = (String) numberDisplay.getText();
        String digit = (String) view.getContentDescription();
        if (val.charAt(0) == '0') {
            numberDisplay.setText(digit);
        }
        else {
            numberDisplay.setText((String) numberDisplay.getText() + digit.charAt(0));
        }
    }

    //handling operators
    public void operator(View view) {
        String operator = (String) view.getContentDescription();
        try {
            String val = (String) numberDisplay.getText();
            expression.setOperand1((int) Integer.parseInt(val.toString()));
        }
        catch(NumberFormatException e) {
            expression.setOperand1(0);
        }
        numberDisplay.setText("0");
        expression.setOperator(operator);
    }

    //Handles the event that the = button is pushed, calculates total and updates output
    public void computeTotal(View view) {
        try {
            String val = (String) numberDisplay.getText();
            expression.setOperand2((int) Integer.parseInt(val.toString()));
        }
        catch (NumberFormatException e) {
            expression.setOperand2(0);
        }
        numberDisplay.setText((expression.getEndValue()));
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu item) {
        //Inflates the menu
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    } */
}