package Decorator_Pattern;

public abstract class Beverage {
    //Class variables - Description & Cost of the beverage
    String description = "Unknown Beverage";

    //Returns the description
    String getDescription() {
        return description;
    }

    //Will be implemented in the subclasses
    public abstract double cost();

}
