package Decorator_Pattern;

public class Soy extends CondimentDecorator {
    Beverage beverage;
    String description = "Soy";
    double cost = .15;

    //Set the current beverage to beverage passed into constructor
    public Soy(Beverage beverage) {
        this.beverage = beverage;
    }

    //Add soy to the description
    public String getDescription() {
        return (beverage.getDescription() + ", " + description);
    }

    //Add the cost of Soy
    public double cost() {
        return (cost + beverage.cost());
    }
}
