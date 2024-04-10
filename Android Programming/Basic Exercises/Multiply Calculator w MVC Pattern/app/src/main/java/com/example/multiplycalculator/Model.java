package com.example.multiplycalculator;
/* MVC - MODEL CLASS */

import java.math.BigInteger;

/* Acts as the model in the MVC Design Pattern, holding relevant
data and logic for calculations */
public class Model {
    private BigInteger total; //Holds the current result

    //Default Constructor- initializes the total as 1
    public Model() {
        total = BigInteger.valueOf(1);
    }

    //Multiplies the current total by the parameter int and returns the result
    public BigInteger multiply(int num) {
        total = total.multiply(BigInteger.valueOf(num));
        return total;
    }

    //Clears the current total
    public void clear() {
        total = BigInteger.valueOf(1);
    }


}
