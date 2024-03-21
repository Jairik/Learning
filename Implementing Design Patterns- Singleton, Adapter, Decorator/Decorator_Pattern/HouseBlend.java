package Decorator_Pattern;

public class HouseBlend extends Beverage {
    double cost = .89; //Cost of a House Blend

    //Adds the description to the beverage
    public HouseBlend() {
        description = "House Blend";
    }

    //Returns the cost
    public double cost() {
        return cost;
    }
}
