package Adapter_Pattern;

/* This serves as the new interface (adaptee) that we must 'adapt' to allow us to 
 * use with the circle class */

public class AnotherCircle {

    public void setLocation() {
        System.out.println("Setting Circle Location from Adaptee");
    }

    public void drawIt() {
        System.out.println("Drawing Circle from Adaptee");
    }

    public void fillIt() {
        System.out.println("Filling Circle from Adptee");
    }

    public void setItColor() {
        System.out.println("Setting Color of Circle from adptee");
    }

    public void unDrawIt() {
        System.out.println("Undrawing Circle from adptee");
    }

}
