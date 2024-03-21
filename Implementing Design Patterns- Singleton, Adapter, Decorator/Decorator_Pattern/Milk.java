package Decorator_Pattern;

public class Milk extends CondimentDecorator {
    Beverage beverage;
    String description = "Milk";
    double cost = .15;

    //Set the current beverage to beverage passed into constructor
    public Milk(Beverage beverage) {
        this.beverage = beverage;
    }

    //Add milk to the description
    public String getDescription() {
        return (beverage.getDescription() + ", " + description);
    }

    //Add the cost of Milk
    public double cost() {
        return (cost + beverage.cost());
    }
}
