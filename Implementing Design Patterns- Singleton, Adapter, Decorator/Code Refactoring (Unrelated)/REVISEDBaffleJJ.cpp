#include <cstdlib>

#include <iostream>
#include <string>
using namespace std;

/*------------------------------------------------------
  Refactored by: JJ McCauley
  Date of Revision: 5/14/24
  Notes: Refactored for readibility and performance 
--------------------------------------------------------*/

enum Direction { UP, DOWN, LEFT, RIGHT }; //Controlling the direction for the laser.
enum Grid { EMPTY, RIGHTBAFFLE, LEFTBAFFLE, LEFTFOUND, RIGHTFOUND }; //The different states for the grid entries
enum Level { BEGIN = 4, INTERMEDIATE = 7, ADVANCED = 10 }; //Game Difficulty
Level gameLevel; //Game difficulty
enum Side { LEFTSIDE, RIGHTSIDE, TOP, BOTTOM }; //Controls the bounds of the grid
const int MAXROW = 10, MAXCOL = 10; //Setting the max column and row size
int numLaserShot = 0, numOfGuesses = 0, bafflesFound = 0, laserEnd, correctGuesses = 0;
void PrintBoard(Grid board[][MAXCOL], bool foundOnly); //Prints the board
void setBoard(Grid board[][MAXCOL], Level); //Sets the board
char printPrompts(); //Prints the prompts for the user and retreives the user's input
int TrackLaser(Grid board[][MAXCOL], int , int , Direction ); //Tracks and moves the laser
int gridConverter(int x, Direction &dir); //Converts user input into array index
int outBoundsConverter(int laserX, int laserY, Side side); 
void PrintScore(int, int); //Prints the current score
void getInitialPosition(int &x, int &y, Direction dir); //Gets the initial position of the laser
bool MakeGuess(Grid board[][MAXCOL]); //Collects guess from user
Direction dir = DOWN;

int main() {
  Grid board[MAXROW][MAXCOL];
  string Gridboard[MAXROW][MAXCOL];
  int x, y, laserEnd = 0;
  char direction, type, promptInput, quitInput, cheatInput;
  bool correctGuess, winner = false, validDifficulty = false;
  string laser;

  cout << "Welcome to the revised Baffle Game!" << endl << endl; //Display a warming welcome message

  //Getting and validating the difficulty from the user
  cout << "Enter Difficulty: Beginner (B), Intermediate (I), or Advanced (A)" << endl;
  do {
    cin >> type;
    if (type == 'B' || type == 'b') {
      gameLevel = BEGIN;
      validDifficulty = true;
    } 
    else if (type == 'I' || type == 'i') {
      gameLevel= INTERMEDIATE;
      validDifficulty = true;
    } 
    else if (type == 'A' || type == 'a') {
      gameLevel= ADVANCED;
      validDifficulty = true;
    } 
    else {
      cout << "Invalid selection - please try again: ";
    }
  } while(!validDifficulty);

  setBoard(board, gameLevel); //Setting up the board with random baffles, dependent on difficulty

  while (!winner) {
    promptInput = printPrompts(); //Printing prompt and receiving input

    switch(promptInput) {

      //Print the score
      case 'S':
        PrintScore(numLaserShot, numOfGuesses);
        break;

      //Shoot the laser
      case 'L':
        int laserStart;
        cin >> laserStart;
        //Validating laser start position
        if(laserStart < 0 || laserStart > 39) {
          cout << "*** Invalid Laser Position" << endl; 
        }
        else { //Position is valid
          numLaserShot++;
          x = gridConverter(laserStart, dir);
          getInitialPosition(x,y, dir);
          laserEnd = TrackLaser(board, x, y, dir);
          cout << "Laser shot #" << numLaserShot << " exited the box at " << laserEnd << "." << endl;
        }
        break;

      //Make a guess
      case 'G':
        if (MakeGuess(board)) {
          correctGuesses++;
          if (correctGuesses == gameLevel) {
            winner = true;
          }
        } 
        break;
      
      //Print the grid
      case 'P':
        PrintBoard(board, true);
        break;
      
      //Print the grid in 'cheat' mode
      case 'C':
        cout << "Are you sure? (y/n)" << endl;
        cin >> cheatInput;
        if (cheatInput == 'Y' || cheatInput == 'y') {
          PrintBoard(board, false);
        } 
        else { 
          cout << endl << "Only printing found baffles" << endl;
          PrintBoard(board, true);
        }
        break;

      //Quit the program
      default:
        cout << "Are you sure you want to quit? (y / n)" << endl;
        cin >> quitInput;
        if (quitInput == 'Y' || quitInput == 'y') { //User would like to quit
          PrintBoard(board, false); //Show all unfound baffles
          cout << endl << "Thank you for playing the Baffle Game!" << endl;
          return 0; //Exiting program
        }
        else
          cout << "Continuing, please enter next input" << endl;
    }
  }
}
    
//Print the board, displaying the baffles dependent on parameter
  void PrintBoard(Grid board[][MAXCOL], bool foundOnly) {
    cout << endl << "  ";
    for (int top = 10; top <= 19; top++) {
      cout << " " << top;
    }

    cout << endl;
    for (int row = 0; row < MAXROW; row++) {
      cout << (9 - row) << " "; // prints left-hand numbers
      for (int col = 0; col < MAXCOL; col++) {
        if (foundOnly == true) {
          if (board[row][col] == LEFTFOUND) {
            cout << " L ";
          } else if (board[row][col] == RIGHTFOUND) {
            cout << " R ";
          } else {
            cout << " - ";
          }
        } else { // foundOnly is false
          if (board[row][col] == LEFTBAFFLE || board[row][col] == LEFTFOUND) {
            cout << " L ";
          } else if (board[row][col] == RIGHTBAFFLE ||
                     board[row][col] == RIGHTFOUND) {
            cout << " R ";
          } else {
            cout << " - ";
          }
        }
      }
      cout << (20 + row) << endl; 
    }

    cout << "  ";
    for (int bottom = 39; bottom >= 30; bottom--) {
      cout << " " << bottom;
    }
  }

//Set up the board, randomly setting up baffles based on the game difficulty
void setBoard(Grid board[][MAXCOL], Level gameLevel){
  //Initializing the board to be all empty spaces
  for (int i = 0; i < MAXROW; i++) {
    for (int j = 0; j < MAXCOL; j++) {
      board[i][j] = EMPTY;
    }
  }
  srand(static_cast<int>(time(0)));
  //Adding baffles in correspondance with the game difficulty, also ensuring that baffles are not replaced
  int remainingBaffles = gameLevel;
  while(remainingBaffles > 0) {
    int row = rand() % MAXROW;
    int col = rand() % MAXCOL;
    if(board[row][col] == EMPTY) {
      board[row][col] = (rand() % 2 == 0) ? LEFTBAFFLE : RIGHTBAFFLE;
      remainingBaffles--;
    }
  }
  cout << "Board is set" << endl;
}

//Prints the options (prompt) to the console
char printPrompts() {
  cout << endl << endl << "Here are the inputs: " << endl << endl;
  cout << "L: Shoot a laser from the particular box" << endl;
  cout << "G: Makes a guess of where a baffle is: (row)(column)(L/R baffle) "<< endl;
  cout << "S: Display the current score" << endl;
  cout << "P: Print the box, displaying only found baffles" << endl;
  cout << "C: Print the box, displaying all baffles. Note: This will mark you as a cheater" << endl;
  cout << "Q: Quit" << endl;

  //Collect, validate, and return input
  char input;
  cin >> input;
  input = toupper(input); 
  if(input == 'L' || input == 'G' || input == 'S' || input == 'P' || input == 'C' || input == 'Q') {
    return input;
  }
  else {
    cout << "*** Illegal Command - Try Again ***" << endl;
    return printPrompts();
  }
}

//Gets the initial position of the laser
void getInitialPosition(int &x, int &y, Direction dir) {
  if (dir == UP) {
    y = x, x = MAXROW - 1;
  }
  else if (dir == DOWN) {
    y = x, x = 0;
  }
  else if (dir == RIGHT) {
    y = 0;
  }
  else if (dir == LEFT) {
    y = MAXCOL - 1;
  }
}

/*Uses the direction of the laser to move the laser over, recursively calling itself until 
it reaches the end of the grid */
int TrackLaser(Grid board[][MAXCOL], int laserX, int laserY, Direction dir) {
  //Adjusting the current direction of the laser if it hit a baffle
  if (board[laserX][laserY] == RIGHTBAFFLE || board[laserX][laserY] == RIGHTFOUND) {
    switch(dir) {
      case UP: 
        dir = RIGHT; 
        break;
      case DOWN: 
        dir = LEFT;
        break;
      case LEFT:
        dir = DOWN;
        break;
      case RIGHT:
        dir = UP;
        break;
    }
  } 
  else if (board[laserX][laserY] == LEFTBAFFLE || board[laserX][laserY] == LEFTFOUND) {
    switch(dir) {
      case UP:
        dir = LEFT;
        break;
      case DOWN:
        dir = RIGHT;
        break;
      case LEFT: 
        dir = UP;
        break;
      case RIGHT:
        dir = DOWN;
        break;
    }
  }

  // Adjusts the laser position based on the (potentially new) direction
  switch(dir) {
    case UP:
      if (laserX == 0) 
        return outBoundsConverter(laserX, laserY, TOP);
      else 
        laserX--;
      break; 
    case DOWN:
      if (laserX == MAXCOL - 1)
        return outBoundsConverter(laserX, laserY, BOTTOM); // Assuming MAXCOL for bottom boundary
      else 
        laserX++;
      break;
    case LEFT:
      if (laserY == 0) 
        return outBoundsConverter(laserX, laserY, LEFTSIDE);
      else 
        laserY--;
      break;
    case RIGHT:
      if (laserY == MAXCOL - 1) 
        return outBoundsConverter(laserX, laserY, RIGHTSIDE); // Assuming MAXCOL for right boundary
      else 
        laserY++;
      break;
  }

  // Recursively calls itself with the new position and direction until an exit condition is met
  return TrackLaser(board, laserX, laserY, dir);
}

//Converts the direction and coordinate into positions readible by the array
int gridConverter(int x, Direction &dir) {
  bool invalidInput = true;

  while (x > 39 || x < 0) {
    cout << "*** Wrong input, please try again: ";
    cin >> x;
    x = static_cast<int>(x); // converting to int
  }
  if (x < 10) {
      x = (9 - x);
      dir = RIGHT;
  } else if (x < 20) {
      x = (x - 10);
      dir = DOWN;
  } else if (x < 30) {
      x = (x - 20);
      dir = LEFT;
  } else  {
      x = (39 - x);
      dir = UP;
  }

  return x;
}

//The out of bounds converter is used to convert the out of bounds to the correct grid.
int outBoundsConverter(int laserX, int laserY, Side side) {
  if (side == BOTTOM) { // laser exiting out the bottom
      return laserY + 30;
  } else if (side == TOP) { // laser exiting out the top
      return laserY + 10;
  } else if (side == RIGHTSIDE) { // laser exiting out the right
      return laserX + 20;
  } else { // laser exiting out the left
      return 9 - laserX;
  }
}

/*Allows the user to guess where the baffle is, updating the grid and returning whether or not they 
found the baffle*/
bool MakeGuess(Grid board[][MAXCOL]) {
  bool found = false, validInput = false;
  int guessR, guessC;
  char direction;

  //Gathering and validating user input
  while(!validInput) {
    cout << "Enter your guess-" << endl;
    cout << "Row: ";
    cin >> guessR;
    if(guessR < 0 || (guessR > 9 && guessR < 20) || guessR > 29) {
      cout << "Incorrect row, please try again" << endl;
      validInput = false;
      continue; //skip to next iteration of the while loop
    }
    cout << "Column: ";
    cin >> guessC;
    if(guessC < 10 || (guessC > 19 && guessC < 30) || guessC > 39) {
      cout << "Incorrect column, please try again" << endl;
      validInput = false;
      continue;
    }
    cout << "Direction (L/R/U/D): "; 
    cin >> direction;
    direction = toupper(direction);
    if(direction != 'L' && direction != 'R' && direction != 'U' && direction != 'D') {
      validInput = false;
    } 
    else { //Correct positions were chosen, guess is valid
      validInput = true;
    }
  }

  cout << "This is guess #" << ++numOfGuesses << ". ";

  guessR = gridConverter(guessR, dir);
  guessC = gridConverter(guessC, dir);

  if ((direction == 'L' && board[guessR][guessC] == LEFTBAFFLE) ||
    (direction == 'R' && board[guessR][guessC] == RIGHTBAFFLE)) {
    cout << "Congratulations, you have now found " << ++bafflesFound << " baffle(s)." << endl;
    board[guessR][guessC] = (direction == 'L' ? LEFTFOUND : RIGHTFOUND);
    found = true;
  } 
  else if (board[guessR][guessC] == LEFTFOUND || board[guessR][guessC] == RIGHTFOUND) {
    cout << "You have already found this baffle." << endl;
  } 
  else {
    cout << "Sorry, no baffle at this location." << endl;
  }

  return found;
}

void PrintScore(int numLasers, int numGuesses) {
  cout << "Number of shots: " << numLaserShot << endl;
  cout << "Number of guesses: " << numOfGuesses << endl;
  cout << "Current Score: " << (numLaserShot + (numOfGuesses*2));
}

