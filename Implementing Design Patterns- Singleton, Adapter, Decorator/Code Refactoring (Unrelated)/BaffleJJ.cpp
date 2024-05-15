#include <cstdlib>

#include <iostream>
#include <string>
using namespace std;

/*---------------------------------------------------------------------
  JJ) Small fixes all throughout the program:
        -> Correcting Typos (specifically in comments)
        -> Refining comments/documentation 
        -> Fixing odd line breaks/ formatting issues
        -> Cleaning up variable declarations/ initializations

  Major changes are explicitly highlighted 
  
  Note: I deleted all comments that originally came with the code so
  that my comments (highlighted revisions) are clearer
---------------------------------------------------------------------*/

enum Direction { UP, DOWN, LEFT, RIGHT };
enum Grid { EMPTY, RIGHTBAFFLE, LEFTBAFFLE, LEFTFOUND, RIGHTFOUND }; 
enum Level { BEGIN = 4, INTERMEDIATE = 7, ADVANCED = 10 }; 
Level gameLevel; 
enum Side { LEFT1, RIGHT1, TOP, BOTTOM }; //JJ) Change variable names to make it more intuitive
const int MAXROW = 10; 
const int MAXCOL = 10; 
int numLaserShot = 0, numOfGuesses = 0, bafflesFound = 0, laserEnd, 
correctGuesses = 0; 
void PrintBoard(Grid board[][MAXCOL], bool foundOnly); 
void setBoard(Grid board[][MAXCOL], Level); 
void printPrompts(); 
int TrackLaser(Grid board[][MAXCOL], int , int , Direction ); 
int gridConverter(int x, Direction &dir); 
int outBoundsConverter(int laserX, int laserY, Side side); 
void PrintScore(int numLaserShot, int numOfGuess); 
void getInitialPosition(int &x, int &y, Direction dir); 
bool MakeGuess(Grid board[][MAXCOL], int , int , char ); 

Direction dir = DOWN;
int main() {
  Grid board[MAXROW][MAXCOL];
  string Gridboard[MAXROW][MAXCOL];
  string type;
  int lazarEnd = 0;
  char direction;
  int x,y;
  bool correctGuess, winner = false;
  string laser;

  //JJ) Making expected input clearer and validating input for difficulty
  cout << "Welcome to the baffle game what difficulty would you like? Beginner "
          ", Intermediate, or Advance"
       << endl;
  cin >> type;

  cout << "Please enter an B, I, or A" << endl;

  if (type == "B") {
    gameLevel = BEGIN;
  } else if (type == "I") {
    gameLevel= INTERMEDIATE;
  } else if (type == "A") {
    gameLevel= ADVANCED;
  }
  
   setBoard(board, gameLevel); 
  while (winner == false){ 

    PrintBoard(board, true); //JJ) Removing printBoard on every turn (redundancy with 'P' input)
    printPrompts(); 
  char input;
  cin >> input;
  cout << endl;
  //JJ) Convert this if else chain to a switch statement, optimizing performance
  if (input == 'Z') { //JJ) Removing this case, as it seems redundant
    printPrompts();
  } else if (input == 'S') {
    PrintScore(numLaserShot, numOfGuesses);
  } else if (input == 'L') { 
    int laserStart;
    cin >> laserStart; //JJ) Add input validation
    numLaserShot++;
    x = gridConverter(laserStart, dir);
    getInitialPosition(x,y, dir);
    laserEnd = TrackLaser(board, x, y, dir);
    cout << "Laser shot #" << numLaserShot << " exited the box at "
         << laserEnd << "." << endl;
  } else if (input == 'G') {
 correctGuess = MakeGuess(board, x, y, direction);
    if (correctGuess == true) { //JJ) Can pass MakeGuess() directly into conditional, improving readibility
      correctGuesses++;
      if (correctGuesses == gameLevel) {
        winner = true;
      }
    }
  } else if (input == 'P') {
    PrintBoard(board, true);
  } else if (input == 'C') {
    cout << "Are you sure? (y / n)" << endl;
    cin >> input;
    if (input == 'Y' || 'y') {
      PrintBoard(board, false);
    } else {
      cout << endl << "Only printing found baffles" << endl;
      PrintBoard(board, true);
    }
  } else if (input == 'Q') {
    cout << "Are you sure you want to quit? (y / n)" << endl;
    cin >> input;
    if (input == 'Y' || 'y') {
      PrintBoard(board, false);
      break; //JJ) Break is unnecessary, will remove

      cout << endl << "Thank you for playing the Baffle Game!" << endl;
      return 0;
    } else
      cout << "Continuing, please enter next input" << endl;
  }

  else {
    cout << "*** Illegal Command - Try Again ***" << endl;
  }

  
}
}
    

  
        
           
    
  

  void PrintBoard(Grid board[][MAXCOL], bool foundOnly) {
    cout << "  ";
    //JJ) Slightly revising this for loop
    for (int top = 10; top <= 19; top++) {
      cout << " " << top;
    }

    cout << endl;
    for (int row = 0; row < MAXROW; row++) {
      cout << (9 - row) << " "; 
      for (int col = 0; col < MAXCOL; col++) {
        if (foundOnly == true) {
          if (board[row][col] == LEFTFOUND) {
            cout << " L ";
          } else if (board[row][col] == RIGHTFOUND) {
            cout << " R ";
          } else {
            cout << " - ";
          }
        } else { 
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




void setBoard(Grid board[][MAXCOL], Level gameLevel){
  for (int i = 0; i < MAXROW; i++) {
    for (int j = 0; j < MAXCOL; j++) {
      board[i][j] = EMPTY;
    }
  }
   srand(static_cast<int>(time(0)));
  //JJ) Will clean up logic here, also adding validation so that no two baffles are placed in the same spot
  for (int i = 0; i < gameLevel; i++) {
    int row, col;
    
      
      row = rand() % MAXROW;
      col = rand() % MAXCOL;

    int Random = rand() % 2;
    if (Random == 0) {
      board[row][col] = LEFTBAFFLE;
    } else {
      board[row][col] = RIGHTBAFFLE;
    }
  }
  cout << "Board is set" << endl;
}

//JJ) Modify this method to have it gather, validate, and return the input in order to clean the main
void printPrompts() {
  cout << endl;
  cout << "Here are the inputs: " << endl << endl;
  cout << "L: Shoot a laser from the particular box" << endl;
  cout << "G: Makes a guess of where a baffle is: (row)(column)(L/R "
          "baffle) "
       << endl;
  cout << "S : Display the current score" << endl;
  cout << "P : Print the box, displaying only found baffles" << endl;
  cout << "C : Print the box, displaying all baffles. Note: This will mark you "
          "as a cheater"
       << endl;
  cout << "Q : Quit" << endl;
  cout << "Z : Print out this prompt message" << endl << endl;
}

//JJ) Change to if else chain to aid in efficiency
void getInitialPosition(int &x, int &y, Direction dir) {
  if (dir == UP) {
    y = x, x = MAXROW - 1;
  }
  if (dir == DOWN) {
    y = x, x = 0;
  }
  if (dir == RIGHT) {
    y = 0;
  }
  if (dir == LEFT) {
    y = MAXCOL - 1;
  }
}

//JJ) Converting this into a switch statement, increasing performance and readibility
int TrackLaser(Grid board[][MAXCOL], int laserX, int laserY, Direction dir) {
  if (board[laserX][laserY] == RIGHTBAFFLE ||
      board[laserX][laserY] == RIGHTFOUND) {
    if (dir == UP) dir = RIGHT;
    else if (dir == DOWN) dir = LEFT;
    else if (dir == LEFT) dir = DOWN;
    else if (dir == RIGHT) dir = UP;
  } else if (board[laserX][laserY] == LEFTBAFFLE ||
             board[laserX][laserY] == LEFTFOUND) {
    if (dir == UP) dir = LEFT;
    else if (dir == DOWN) dir = RIGHT;
    else if (dir == LEFT) dir = UP;
    else if (dir == RIGHT) dir = DOWN;
  }

  if (dir == UP) {
    if (laserX == 0) return outBoundsConverter(laserX, laserY, TOP);
    else laserX--;
  } else if (dir == DOWN) {
    if (laserX == MAXCOL - 1) return outBoundsConverter(laserX, laserY, BOTTOM); 
    else laserX++;
  } else if (dir == LEFT) {
    if (laserY == 0) return outBoundsConverter(laserX, laserY, LEFT1);
    else laserY--;
  } else if (dir == RIGHT) {
    if (laserY == MAXCOL - 1) return outBoundsConverter(laserX, laserY, RIGHT1); 
    else laserY++;
  }

  return TrackLaser(board, laserX, laserY, dir);
}



int gridConverter(int x, Direction &dir) {
    bool invalidInput = true;
    //JJ) Cleaning up this validation
    if (x > 39 || x < 0) {
        while (invalidInput == true) {
            cout << "*** Wrong input" << endl;
            cin >> x;
            x = static_cast<int>(x);
            if (x < 40 && x >= 0) {
                invalidInput = false;
            }
        }
    //JJ) Since this is independent from previous statement, changing to if
    } else if (x < 10) {
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

int outBoundsConverter(int laserX, int laserY, Side side) {
       if (side == BOTTOM) { 
           return laserY + 30;
       } else if (side == TOP) { 
           return laserY + 10;
       } else if (side == RIGHT1) { 
           return laserX + 20;
       } else { 
           return 9 - laserX;
       }
   }



//JJ) Add in the total score (numOfLasers + numOfGuesses*2)
void PrintScore(int numLaserShot, int numOfGuess) {
  cout << "Number of shots: " << numLaserShot << endl;
  cout << "Number of guesses: " << numOfGuess << endl;
}

/*JJ) Since the user makes a guess within the actual function, will remove the guessR, guessC, and direction
parameters. Additionally, will add input validation and clarify expected input in the cout statement */ 
bool MakeGuess(Grid board[][MAXCOL], int guessR, int guessC, char direction) {
    bool found = false;
    cout << "Enter your guess (row column side): "; //JJ) <- Expected output is unclear
    cin >> guessR >> guessC >> direction;

    direction = toupper(direction);

    cout << "This is guess #" << ++numOfGuesses << ". ";

    guessR = gridConverter(guessR, dir);
    guessC = gridConverter(guessC, dir);

    if ((direction == 'L' && board[guessR][guessC] == LEFTBAFFLE) ||
        (direction == 'R' && board[guessR][guessC] == RIGHTBAFFLE)) {
        cout << "Congratulations, you have now found " << ++bafflesFound << " baffle(s)." << endl;
        board[guessR][guessC] = (direction == 'L' ? LEFTFOUND : RIGHTFOUND);
        found = true;
    } else if (board[guessR][guessC] == LEFTFOUND || board[guessR][guessC] == RIGHTFOUND) {
        cout << "You have already found this baffle." << endl;
    } else {
        cout << "Sorry, no baffle at this location." << endl;
    }

    return found;
}



