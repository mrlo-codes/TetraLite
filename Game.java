import java.util.*;

public class Game{
    private Board b;
    private Player[] players;
    private Scanner in;
    private String command;
    private boolean plainText;
    private ArrayList<String> colTest;
    private String currCol;
    private static final String STR_RESET = "\033[0;0m";  
    private static final String STR_BOLD = "\033[0;1m";

    public Game(){
        in = new Scanner(System.in);
        setup();
    }

    //Creates a 2 player game and creates the board with the option of setting up the game with colors
    public void setup(){
        players = new Player[2];

        System.out.println("Play in plain text (no colors)? Y/N: ");
        command = in.nextLine();
        plainText = command.toLowerCase().equals("y") ? true:false;

        b = new Board(plainText);

        players[0] = new Player("blue", plainText);
        players[1] = new Player("red", plainText);

        currCol = "";
    }

    //Starts one entire game
    //Game ends when all spaces on the board are filled (16).
    public void play(){
        while(b.numFilled() != 16){
            for(Player p : players){
                takeTurn(p);
            }
        }
    }
    
    /**
     * A player takes a turn by placing a card on the board
     * A player places a card by entering the card # and the position on the board to place it on (row, col)
     * Example command: 2, (3, 4) places the card numbered 2 on position (3, 4)
     * If a tile is blocked or a move is illegal, turn is denied and player is asked to re-enter a move
     * @param p the current player
     */
    public void takeTurn(Player p){
        int cNum = -1, row = -1, col = -1; //Sets card number, and position (X, Y) to default value of invalid, -1

        while(cNum == -1){

            //Prompt user to enter command
            if(plainText){
                System.out.println("Player " + p.getColor().toUpperCase() + ", please make your move in the following format: #, (X, Y).\nExample: 2, (3, 4) places your tile numbered 2 onto the 3rd column, 4th row tile (if not blocked).\n");
            } else {
                System.out.println("Player " + STR_BOLD + p.getColor().toUpperCase() + STR_RESET + ", please make your move in the following format: " + STR_BOLD + "#, (X, Y)." + STR_RESET + "\nExample: 2, (3, 4) places your tile numbered 2 onto the 3rd column, 4th row tile (if not blocked).\n");
            }
            System.out.println(p);
            System.out.println("\nEnter your move:");

            command = in.nextLine();

            //Break command into separate values as long as the input is valid
            if(inputValid(command, 1)){
                cNum = Integer.valueOf(command.substring(0, 1));
                col = Integer.valueOf(command.substring(command.indexOf("(") + 1, command.indexOf("(") + 2));
                row = Integer.valueOf(command.substring(command.indexOf(")") - 1, command.indexOf(")")));

                //System.out.println("cNum: " + cNum + " Row: " + row + " Col: " + col);

                //Test for illegal moves.
                if(p.isEmpty(cNum)){
                    System.out.println("Sorry, you have no card there. Try again.\n");
                    cNum = -1;
                } else if (!b.isEmpty(col, row)) {
                    if(b.isWall(col, row)){
                        System.out.println("\nSorry, you can't place your card on a wall. Try again.\n");
                    cNum = -1;
                    } else {
                        System.out.println("\nSorry, you can't place your card on an occupied space. Try again.\n");
                        cNum = -1;
                    }
                }
            } else {
                System.out.println("You entered the command incorrectly. Please try again.");
            }
        }

        clearScreen(); //Prepare screen to show the results of the move.

        Card curr = p.getCard(cNum);

        b.setCard(col, row, curr); //Place card on board
        p.removeCard(cNum); //remove card from hand
        System.out.println(p); //print out the contents of the hand
        System.out.println(b); //print out the contents of the board

        ArrayList<Integer> currEnemies = b.findEnemies(col, row); //check if there are any enemies against the card just placed

        //System.out.println(currEnemies);

        if(!currEnemies.isEmpty()){
            if(battle(curr, col, row, currEnemies)){
                System.out.println("Battle won");
            } else {
                System.out.println("Battle lost");
            }
        }


    }

    /**
     * battle() conducts a fight between the target and an enemy around it. 
     * @return results of the battle as a String.
     *          "win" if the fight has won
     */
    public boolean battle(Card target, int tCol, int tRow, ArrayList<Integer> enemyList){
        Card enemy;
        int tDir;
        ArrayList<Integer> eDir = new ArrayList<Integer>();
        int[][] shiftDir = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
        String loc;
        int col = -1, row = -1;
        int currDir, currCol, currRow;
        int counter = 0;

        //Debugging purposes only
        System.out.println("Current list of targets: " + enemyList);

        //Determine which spaces the target can look at based on the target's direction information and whether there are any enemies in those spaces

        tDir = Integer.valueOf(target.getDirections()); //get target's list of directions

        //check if the target directions point to an enemy from enemyList.
        //Any enemies not targeted are removed from the list
        while(tDir != -1){
            System.out.print(tDir + " ");
            //identify one of the directions
            currDir = tDir % 10;

            //Determine what the coordinate is
            currCol = tCol + shiftDir[currDir][0];
            currRow = tRow + shiftDir[currDir][1];
            System.out.print("looking at: " + (currCol*10+currRow));

            //Check to see if any of the enemies in the list match the coordinate the target is looking at
            //If there is a match, move it to the front of the list.
            for(int i = 0; i < enemyList.size(); i++){
                if(currCol*10+currRow == enemyList.get(i)){
                    System.out.print(" match found: " + (currCol*10 + currRow));
                    enemyList.add(counter, enemyList.get(i));
                    enemyList.remove(enemyList.get(i + 1));
                    counter++;
                }
            }

            System.out.println();

            if(tDir > 9){
                tDir /= 10;
            } else {
                tDir = -1;
                
                for(int i = counter; i < enemyList.size(); i++){
                    enemyList.remove(i);
                }
            }

        }
        
        System.out.println("Possible targets: " + enemyList);

        //get all of the enemies' directions
        for(Integer e:enemyList){
            eDir.add(e);
        }


        //Obtain the enemy card information. If there is more than one enemy available, player chooses which enemy to fight.
        if(enemyList.size() > 1){
            while(!enemyList.contains((col * 10) + row)){
                System.out.print("Enter target to fight as coordinates (x, y): ");

                loc = in.nextLine();
                
                if(inputValid(loc, 2)){
                    col = Integer.valueOf(loc.substring(loc.indexOf("(") + 1, loc.indexOf("(") + 2));
                    row = Integer.valueOf(loc.substring(loc.indexOf(")") - 1, loc.indexOf(")")));
                }
                
                if(!enemyList.contains((col * 10) + row)){
                    System.out.println("You entered an invalid coordinate. Please try again.");
                }
            }
            enemy = b.getCard(col, row);
        } else if (enemyList.size() == 1){
            col = enemyList.get(0) / 10;
            row = enemyList.get(0) % 10;
            enemy = b.getCard(col, row);
        } 

        //Check to see if 
        return ((int)(Math.random() * 2)) == 1 ? false : true;

    }

    public void clearScreen(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public boolean isPlainTextMode(){
        return plainText;
    }

    public boolean inputValid(String in, int type){

        if(type == 1){ //If input is for placing cards on board
            return in.length() == 9 && in.substring(3, 4).equals("(") && in.substring(5, 6).equals(",") && in.substring(8).equals(")");
        } else if (type == 2){ //If input is for choosing an enemy tile
            return in.length() == 6 && in.substring(0, 1).equals("(") && in.substring(2, 3).equals(",") && in.substring(5).equals(")");
        }

        return false;
        
    }


    public String toString(){
        String out = "";

        //print out the player's decks
        out += "Player 1: \n" + players[0] + "\nPlayer 2: \n" + players[1];

        //print out the current game board
        out += "\nCurrent Game Board:\n" + b;

        return out;
    }
}