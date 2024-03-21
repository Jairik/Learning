package Singleton_Pattern;

import javax.swing.*; //Include all java swing utilities

import java.awt.Color;
//Add actions when the button is clicked
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font; //Formatting the label
import java.awt.GridLayout;

/* This class acts as the "base" window */

public class myFrame extends JFrame{
    JPanel panel;
    JButton sButton;
    JLabel label;
    Singleton singletonInstance;

    myFrame() {
        //Formatting the frame
        this.setSize(600, 400);
        this.setTitle("Singleton Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2, 1));

        //Creating the panel and button
        sButton = new JButton("Click me!"); //Creating the button
        sButton.setFont(new Font("Arial", Font.BOLD, 24));

        //Creating and formatting the top label
        label = new JLabel("JJ's Singleton Example");
        label.setSize(500, 500);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLUE);
        label.setHorizontalAlignment(JLabel.CENTER); //Centering text
        
        //Adding everything to the frame & making it visible
        this.add(label);
        this.add(sButton);
        this.setVisible(true);

        //Adding an action listener for the button
        sButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                singletonInstance = Singleton.getInstance();
            }
        });
    }
}