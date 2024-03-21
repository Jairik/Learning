package Decorator_Pattern;

public class Whip extends CondimentDecorator {
    Beverage beverage;
    String description = "Whip";
    double cost = .10;

    //Set the current beverage to beverage passed into constructor
    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    //Add whip to the description
    public String getDescription() {
        return (beverage.getDescription() + ", " + description);
    }

    //Add the cost of Whip
    public double cost() {
        return (cost + beverage.cost());
    }
}
