package com.example.beerfinder;

import java.util.ArrayList;
import java.util.List;

/* Basic class used to provide beer recommendations */
public class BeerExpert {

    /*Takes in a string parameter, beerColor, and returns a list of the different beer
    recommendations. This will be called in the main activity when the "Find Beer!"
    button is clicked, taking in the selection from the Spinner. */
    public List<String> getBrands(String beerColor) {
        List<String> brands = new ArrayList<>();
        if (beerColor.equals("amber")) {
            brands.add("Jack Amber");
            brands.add("Red Moose");
        } else {
            brands.add("Jail Pale Ale");
            brands.add("Gout Stout");
        }
        return brands;
    }
}
