package Decorator_Pattern;

/* This file serves as a demonstration of the decorator design pattern 
-------------------------------------------------------------------------------
 * In this demonstration, Customers are able to order from "JJ's Coffee Shop",
 * with selections of dark roast, house blend, decaf, and expresso, with 
 * additions/condiments of Mocha, Whip, Milk, and Soy. 
-------------------------------------------------------------------------------*/

public class DecoratorDemonstration {
    public static void main(String[] args) {
        /* "Ordering" Four Beverages:
         *  -One Expresso, no additions
         *  -One Dark Roast with 2 Mocha and Whip
         *  -One House Blend with Soy, Mocha, and Whip
         *  -One Decaf with 2 of everything */
        System.out.println("\n---JJ's Coffee Shop---\n");
        //Make the expresso
        Beverage expresso = new Expresso();
        printItem(expresso);
        //Make the Dark Roast
        Beverage darkRoast = new DarkRoast();
        darkRoast = new Mocha(darkRoast);
        darkRoast = new Mocha(darkRoast);
        darkRoast = new Whip(darkRoast);
        printItem(darkRoast);
        //Make the House Blend
        Beverage houseBlend = new HouseBlend();
        houseBlend = new Soy(houseBlend);
        houseBlend = new Mocha(houseBlend);
        houseBlend = new Whip(houseBlend);
        printItem(houseBlend);
        //Make the Decaf
        Beverage decaf = new Decaf();
        decaf = new Mocha(decaf);
        decaf = new Mocha(decaf);
        decaf = new Milk(decaf);
        decaf = new Milk(decaf);
        decaf = new Soy(decaf);
        decaf = new Soy(decaf);
        decaf = new Whip(decaf);
        decaf = new Whip(decaf);
        printItem(decaf);
    }

    /* Print out  the drinks to the console */
    public static void printItem(Beverage item) {
        System.out.println(item.getDescription() + "\n$" + item.cost());
        System.out.println();
    }
}
