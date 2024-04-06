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

public class MainActivity extends AppCompatActivity {

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
        Spinner color = (Spinner) findViewById(R.id.beer_colors);

    }
}