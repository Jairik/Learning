package com.example.multiplycalculator;
/* MVC - MODEL CLASS */

/* Acts as the model in the MVC Design Pattern, holding relevant
data and logic for calculations */
public class Model {
    int total; //Holds the current result

    //Default Constructor- initializes the total as 1
    public Model() {
        total = 1;
    }

    //Multiplies the current total by the parameter int and returns the result
    public int multiply(int num) {
        total = total*num;
        return total;
    }

    //Clears the current total
    public void clear() {
        total = 1;
    }


}
