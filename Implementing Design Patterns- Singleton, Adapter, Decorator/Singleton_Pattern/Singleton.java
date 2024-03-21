package Singleton_Pattern;

import javax.swing.*; //Include all java swing utilities
import java.awt.Font; //Formatting the label

/* This class acts as the pop-up window, demonstrating singleton properties */

public class Singleton extends JFrame {
    private static Singleton instance = null;
    JFrame singletonFrame;
    JPanel panel;
    JLabel label;
    
    private Singleton() {
        /* Creating formatting for the window */
        singletonFrame = new JFrame("Singleton Frame");
        singletonFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
        singletonFrame.setSize(500, 200);
        label = new JLabel("Singleton Instance");
        label.setHorizontalAlignment(JLabel.CENTER); //Centering text
        label.setFont(new Font("Arial", Font.BOLD, 24));
        singletonFrame.add(label);

        singletonFrame.setVisible(true);
    }

    //Ensures that this is the only instance of the class
    public static Singleton getInstance() {
        if(instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
