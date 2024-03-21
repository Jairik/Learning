package Adapter_Pattern;

public class AdapterDemonstration {
    public static void main(String[] args) {
        //Declaring objects used to testing
        Line line = new Line();
        ObjectCircleAdapter circle = new ObjectCircleAdapter();

        //Testing objects by displaying, filling, and undisplaying from both objects
        System.out.println("This program will firstly test the line object, then the adapted Circle object");
        //Testing an unadapted "Line" Object
        System.out.println("\n-----Line-----");
        line.display();
        line.fill();
        line.undisplay();
        //Testing the adapted "Circle" Object
        System.out.println("\n-----Circle-----");
        circle.display();
        circle.fill();
        circle.undisplay();
        System.out.println(); //Cleaning up output
    }
}
