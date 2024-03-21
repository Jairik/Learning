package Adapter_Pattern;

/* This file uses the object adapter desgin pattern */

public class ObjectCircleAdapter extends Shape{
    //Private adaptee variable
    private AnotherCircle anotherCircle;
    
    ObjectCircleAdapter() {
        this.anotherCircle = new AnotherCircle();
    }

    @Override
    public void display() {
        anotherCircle.drawIt();
    }

    @Override
    public void fill() {
        anotherCircle.fillIt();
    }

    @Override
    public void undisplay() {
        anotherCircle.unDrawIt();
    }
}
