package Decorator_Pattern;

public class DarkRoast extends Beverage {
    double cost = .99; //Cost of a Dark Roast

    //Adds the description to the beverage
    public DarkRoast() {
        description = "Dark Roast";
    }

    //Returns the cost
    public double cost() {
        return cost;
    }
}
