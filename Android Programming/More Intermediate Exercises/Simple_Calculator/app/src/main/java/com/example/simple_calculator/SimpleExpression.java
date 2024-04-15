package com.example.simple_calculator;

/* Author: JJ McCauley
*  Date: 4/15/24
* This class acts like that of the controller, holding relevant values and performing
* the calculation logic */

public class SimpleExpression {
    private int operand1; //The first number typed into the calculator
    private int operand2; //The second number typed into the calculator
    private String operator; //The operator (+ - x /)
    private int endValue; //The value of operand1 <operator> operand2

    //Default constructor that sets all the values to 0
    public SimpleExpression() {
        operand1 = 0;
        operand2 = 0;
        operator = "+";
        endValue = 0;
    }

    /* ----- Getters & Setters for the different values ----- */
    public void setOperand1(int value) {
        operand1 = value;
    }

    public int getOperand1() {
        return operand1;
    }

    public void setOperand2(int value) {
        operand2 = value;
    }

    public int getOperand2() {
        return operand2;
    }

    public void setOperator(String value) {
        operator = value;
    }

    public String getOperator() {
        return operator;
    }

    public int getEndValue() {
        calculate(); //Helper function that will change the value of endOperator
        return endValue;
    }

    /* Apply the correct operation to appropriately change the endValue */
    public void calculate() {
        endValue = 0; //Resetting
        if(operator.equals("+")) {
            endValue = operand1 + operand2;
        }
        else if(operator.equals("-")) {
            endValue = operand1 - operand2;
        }
        else if(operator.equals("x")) {
            endValue = operand1 * operand2;
        }
        else if(operator.equals("/") && operand2 != 0) {
            endValue = operand1 / operand2;
        }
        else { //operator = %
            endValue = operand1 % operand2;
        }
    }

    /* Clear the operands */
    public void clearAll() {
        operand1 = 0;
        operand2 = 0;
    }
}
