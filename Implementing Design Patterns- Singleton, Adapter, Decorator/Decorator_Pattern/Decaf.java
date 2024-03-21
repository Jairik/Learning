package Decorator_Pattern;

public class Decaf extends Beverage{
    double cost = .89; //Cost of a Decaf

    //Adds the description to the beverage
    public Decaf() {
        description = "Decaf";
    }

    //Returns the cost
    public double cost() {
        return cost;
    }
}
