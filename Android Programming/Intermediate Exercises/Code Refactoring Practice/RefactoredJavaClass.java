import java.util.Scanner;

/*-------------------New-Java-Class----------------------
 * This file will hold the refactored code.
 * Comments have been added to explain functionalities
 * and modifications enacted when refactoring.
 * ------------------------------------------------------
 */


public class RefactoredJavaClass {

    final static int HEIGHT = 7; //Added to easily access board height
    final static int WIDTH = 6; //Added to easily access board width

    /*Skips two lines in the console
    Modifications: Removed
    public static void skip2() {
        System.out.println("");
        System.out.println("");
    }*/

    /*Prints the board and the characters currently inside it
     *Modifications: Instead of manually printing each space, implementing
     a nested for loop. Improves readibility and organization*/
    public static void board(char space[][]) {
        for(int i = space[0].length-1; i >= 0; i--) {
            System.out.print(" ");
            for(int j = 0; j < space.length; j++) {
                System.out.print(space[j][i]);
                if(j < space.length-1) {
                    System.out.print(" | ");
                }
            }
            System.out.println(); //Make a new line after every row is created
        }
        System.out.println("---------------------------");
        System.out.println(" 1 | 2 | 3 | 4 | 5 | 6 | 7\n");
    }

    /*Helper function, checking for a winner at a given index
     *Modifications: Removed uncessary conditional statements and combined
      when possible, reducing the complexity.
     */
    public static boolean checkWinnerAt(char[][] b, int i, int j, char c) {
        boolean winnerFound = false;
        if((i + 3) < b.length && (b[i][j] == c) && (j + 3) < b[i].length) {
            //Checking for up & right diagonal wins
            if ((b[i + 1][j + 1] == c && b[i + 2][j + 2] == c && b[i + 3][j + 3] == c)) {
                winnerFound = true;
            }
            //Checking for vertical wins
            else if ((b[i][j + 1] == c && b[i][j + 2] == c && b[i][j + 3] == c)) {
                winnerFound = true;
            }
            //Checking for horizontal wins
            else if ((b[i + 1][j] == c && b[i + 2][j] == c && b[i + 3][j] == c)) {
                winnerFound = true;
            }
            //checking for down & left diagonal wins
            else if ((j - 3) > 0 && (i + 3) < b.length) {
                if (b[i][j - 1] == c && b[i + 1][j - 2] == c && b[i + 2][j - 3] == c && b[i + 3][j - 4] == c) {
                    winnerFound = true;
                }
            }
        }
        return winnerFound;
    }

    /*Determines if there is a winner within the board
     *Modifications: Slightly increased readibility by 
     adding boolean variable*/
    public static boolean checkWinner(char[][] b) {
        boolean r1, r2;
        boolean winnerFound = false;

        for (int r = 0; r < b.length; r++) {
            for (int c = 0; c < b[0].length; c++) {
                r1 = checkWinnerAt(b, r, c, 'R');
                r2 = checkWinnerAt(b, r, c, 'Y');

                if (r1 || r2) {
                    winnerFound = true;
                }
            }
        }

        return winnerFound;
    }

    /*Determines if the user would like to play again
    Modifications: Optimized variables names and types, closed Scanner, 
    and modified print statement*/
    public static boolean again() {
        Scanner in = new Scanner(System.in);
        char userChoice;

        System.out.print("Would you like to play again (Y/N)? ");
        userChoice = in.next().charAt(0);

        //Clearing the input buffer for any stray characters
        if(in.hasNextLine()) {
            in.nextLine();
        }

        userChoice = Character.toLowerCase(userChoice); //converting to lower case

        return(userChoice == 'y');
    }

    /*Initializes board and collects user input, getting the positions of
    char placements. Iterates through until there is a winner, allowing
    each player to make a move after each iteration.
     *Modifications: Small formatting changes and simplified extensive
     switch statements*/
    public static void main(String[] args) {
        //Declaring variables
        Scanner input = new Scanner(System.in);
        int column;
        boolean win = false;
        int xvalue = 0, yvalue = 0, player1Win = 0, player2Win = 0, count = 0;
        int yval[] = new int[7];
        char space[][] = new char[HEIGHT][WIDTH];

        System.out.println("Welcome to Connect 4");

        //MODIFIED: Although the previous approach may have worked,
        //a nested for loop is more readible and cleaner
        for (int x = 0; x < HEIGHT; x++) {
            for(int y = 0; y < WIDTH; y++) {
                space[x][y] = ' ';
            }
        }

        for (int i = 0; i < 7; i++) {
            yval[i] = 0;
        }

        do {
            for (int z = 1; z > 0; z++) {
                System.out.println("\n"); //skipping two lines
                board(space);

                if (count >= 42) {
                    System.out.println("Tie game!");
                    win = again();
                    count = 0;
                    break;
                }

                if (checkWinner(space)) {
                    System.out.println("Player 2 wins!");
                    win = again();
                    xvalue = 0;
                    yvalue = 0;

                    for (int x = 0; x < 42; x++) {
                        space[xvalue][yvalue] = ' ';
                        yvalue++;
                        if (yvalue > 5) {
                            yvalue = 0;
                            xvalue++;
                        }
                    }

                    for (int i = 0; i < 7; i++) {
                        yval[i] = 0;
                    }

                    player2Win++;
                    count = 0;
                    break;
                }

                do {
                    System.out.print("Player 1: Choose the column that you want: ");
                    column = input.nextInt();

                    if (column >= 8 || column <= 0 || yval[column - 1] > 5) {
                        System.out.println("Error: column already full. Please try again");
                    }
                } while (column >= 8 || column <= 0 || yval[column - 1] > 5);

                count++;
                space[column - 1][yval[column - 1]] = 'R';

                yval[--column]++; //Modification: Simplified

                System.out.println("\n"); //skipping two lines
                board(space);

                if (count >= 42) {
                    System.out.println("Tie game!");
                    win = again();
                    count = 0;
                    break;
                }

                if (checkWinner(space)) {
                    System.out.println("Player 1 wins!");
                    win = again();
                    xvalue = 0;
                    yvalue = 0;

                    for (int x = 0; x < 42; x++) {
                        space[xvalue][yvalue] = ' ';
                        yvalue++;
                        if (yvalue > 5) {
                            yvalue = 0;
                            xvalue++;
                        }
                    }

                    for (int i = 0; i < 6; i++) {
                        yval[i] = 0;
                    }

                    player1Win++;
                    count = 0;
                    break;
                }

                do {
                    System.out.print("Player 2: Choose the column that you want: ");
                    column = input.nextInt();

                    if (column >= 8 || column <= 0 || yval[column - 1] > 5) {
                        System.out.println("Error: Column already full. Please try again");
                    }
                } while (column >= 8 || column <= 0 || yval[column - 1] > 5);

                count++;
                space[column - 1][yval[column - 1]] = 'Y';

                yval[--column]++; //Modification: Simplified
            }
        } while (win);

        input.close(); //Modification: Closing scanner
        System.out.println("Player 1 won " + player1Win + " times!");
        System.out.println("Player 2 won " + player2Win + " times!");
    }
}
