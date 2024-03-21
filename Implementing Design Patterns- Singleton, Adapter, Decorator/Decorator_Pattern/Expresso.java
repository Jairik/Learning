package Decorator_Pattern;

public class Expresso extends Beverage {
    double cost = 1.99; //Cost of an expresso

    //Adds the description to the beverage
    public Expresso() {
        description = "Expresso";
    }

    //Returns the cost
    public double cost() {
        return cost;
    }
}
