package Adapter_Pattern;

public class Square extends Shape {

    @Override
    public void display() {
        System.out.println("Displaying Square");
    }

    @Override
    public void fill() {
        System.out.println("Filling Square");
    }

    @Override
    public void undisplay() {
        System.out.println("Undisplaying Square");
    }
}
