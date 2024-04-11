package com.example.coffeeshop;

import static java.lang.String.*;

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

import java.text.Format;

public class MainActivity extends AppCompatActivity {

    //Method variables necessary for the logic of the program
    double coffeeCost = 4.00, creamCost = .50, chocolateCost = 1.00;
    boolean hasChocolate = false, hasCream = false; //Booleans for additions
    int cQty = 0; //Current quantity, beneficial for simplicity

    //Defining different components that will be modified
    CheckBox cream;
    CheckBox chocolate;
    TextView totalInfo;
    TextView totalInfoText; //Just to make visible
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
        cream = findViewById(R.id.Cream_CheckBox);
        chocolate = findViewById(R.id.Chocolate_CheckBox);
        totalInfo = findViewById(R.id.Order_Summary);
        totalInfoText = findViewById(R.id.Order_Summary_Text);
        qty = findViewById(R.id.Quantity);
        incrementQty = findViewById(R.id.Plus_Quantity);
        decrementQty = findViewById(R.id.Minus_Quantity);
    }

    //Calculate the total and set the Order_Summary text
    public void Order(View view) {
        //Getting the button status & setting up variables
        checkCheckBoxes(view);
        String totalMessage = "";
        double orderTotal = coffeeCost;

        //Add topping status to string message and calculating total cost
        totalMessage += "Add whipped cream? ";
        if(hasCream) {
            totalMessage += "yes\n";
            orderTotal += ((double)cQty * creamCost);
        }
        else {
            totalMessage += "no\n";
        }
        totalMessage += "Add chocolate? ";
        if(hasChocolate) {
            totalMessage += "yes\n";
            orderTotal += ((double)cQty * chocolateCost);
        }
        else {
            totalMessage += "no\n";
        }

        //Add quantity to string message
        totalMessage += ("Quantity: " + Integer.toString(cQty) + "\n\n");

        //Add price & thank you message to string message
        String orderTotalString = String.format("%.2f", orderTotal);
        totalMessage += ("Price: $" + orderTotalString + "\n");
        totalMessage += "THANK YOU, SEE YOU AGAIN SOON!";

        //Adding totalMessage to totalInfo text field
        totalInfo.setText(totalMessage);

        //Setting all relevant components to visible
        totalInfo.setVisibility(View.VISIBLE);
        totalInfoText.setVisibility(View.VISIBLE);
    }

    //Increment the quantity count and update the text field
    public void IncrementQuantity(View view) {
        cQty++;
        qty.setText(valueOf(cQty));
    }

    public void DecrementQuantity(View view) {
        if(cQty > 0) {
            cQty--;
            qty.setText(valueOf(cQty));
        }
    }

    /* Helper function for Order(), will determine the status of the checkboxes and update
    * the corresponding booleans accordingly*/
    public void checkCheckBoxes(View view) {
        hasCream = cream.isChecked();
        hasChocolate = chocolate.isChecked();
    }


}