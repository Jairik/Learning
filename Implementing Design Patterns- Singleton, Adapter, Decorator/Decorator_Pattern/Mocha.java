package Decorator_Pattern;

public class Mocha extends CondimentDecorator {
    Beverage beverage;
    String description = "Mocha";
    double cost = .20;

    //Set the current beverage to beverage passed into constructor
    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    //Add mocha to the description
    public String getDescription() {
        return (beverage.getDescription() + ", " + description);
    }

    //Add the cost of Mocha
    public double cost() {
        return (cost + beverage.cost());
    }
}
