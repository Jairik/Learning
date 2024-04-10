package com.example.beerfinder;

//Importing Android-Specific Libraries
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Declaring a beer expert object to later retrieve suggestions from
    private BeerExpert expert = new BeerExpert();

    //Pre-created Function
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

    //Event Handling for "Find Beer!" button click
    public void onClickFindBeer(View view) {
        //Getting the bottom text component so we can change it
        TextView brands = (TextView) findViewById(R.id.brands);

        //Retrieving the current value of the Spinner
        Spinner color = (Spinner) findViewById(R.id.spinner);

        //Getting the selected item from the Spinner
        String beerType = String.valueOf(color.getSelectedItem());

        //Using this information to get a suggestion from BeerExpert class
        List<String> suggestionList = expert.getBrands(beerType); //Getting list
        StringBuilder suggestionsFormatted = new StringBuilder();
        for(String suggestion: suggestionList) {
            suggestionsFormatted.append(suggestion).append('\n');
        }

        //Displaying the suggestions in the TextView
        brands.setText(suggestionsFormatted);

    }
}