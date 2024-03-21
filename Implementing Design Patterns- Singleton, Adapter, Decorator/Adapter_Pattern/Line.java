package Adapter_Pattern;

public class Line extends Shape {

    @Override
    public void display() {
        System.out.println("Displaying Line");
    }

    @Override
    public void fill() {
        System.out.println("Filling Line");
    }

    @Override
    public void undisplay() {
        System.out.println("Undisplaying Line");
    }
}
