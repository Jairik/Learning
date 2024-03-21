package Adapter_Pattern;

/* Shape class - contains the methods used in all shape objects */

public abstract class Shape {

    public void display() {
        System.out.println("Displaying Shape");
    }

    public void fill() {
        System.out.println("Filling Shape");
    }

    public void undisplay() {
        System.out.println("Undisplaying Shape");
    }
}
