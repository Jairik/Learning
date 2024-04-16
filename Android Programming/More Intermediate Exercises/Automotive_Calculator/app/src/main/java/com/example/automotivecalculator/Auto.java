package com.example.automotivecalculator;

/* Acts as the model for the application, holding relevant data and logic */

public class Auto {
    static final double STATE_TAX = .07;
    static final double INTEREST_RATE = .09;

    private double price;
    private double downPayment ;
    private int loanTerm;

    /* Getters and Setters */

    public void setPrice(double p) {
        price = p;
    }

    public double getPrice() {
        return price;
    }

    public void setDownPayment(double down) {
        downPayment = down;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public void setLoanTerm(String term) {
        if(term.contains("2")) {
            loanTerm = 2;
        }
        else if(term.contains("3")) {
            loanTerm = 3;
        }
        else { //It must contain 4
            loanTerm = 4;
        }
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    /* Calculating tax Amount */
    public double taxAmount() {
        return (price * STATE_TAX);
    }

    /* Calculating the total cost */
    public double totalCost() {
        return (price + taxAmount());
    }

    /* Calculating the borrowed amount */
    public double borrowedAmount() {
        return (totalCost() - downPayment);
    }

    /* Calculating the interest rate */
    public double interestAmount() {
        return (borrowedAmount() * INTEREST_RATE);
    }

    /* Calculating the monthly payment */
    public double monthlyPayment() {
        return (borrowedAmount() / (loanTerm * 12));
    }
}
