import java.util.*;

public class Board
{
    public static final String ANSI_RESET = "\u001B[0m";
    private boolean plainText;
    
    private Card[][] board;
    
    /**
     * Constructor for objects of class Board
     */
    public Board()
    {
        board = new Card[4][4];
        int wallsFilled = 0;

        int x = (int)(Math.random() * 4);
        int y = (int)(Math.random() * 4);

        while(wallsFilled != 3){
            if(board[x][y] == null){
                board[x][y] = new Card();
                wallsFilled++;
            }
            x = (int)(Math.random() * 4);
            y = (int)(Math.random() * 4);
        }

        plainText = false;

    }

    public Board(boolean p){
        board = new Card[4][4];
        int wallsFilled = 0;

        int x = (int)(Math.random() * 4);
        int y = (int)(Math.random() * 4);

        while(wallsFilled != 3){
            if(board[x][y] == null){
                board[x][y] = new Card(p);
                wallsFilled++;
            }
            x = (int)(Math.random() * 4);
            y = (int)(Math.random() * 4);
        }

        plainText = p;
    }
    
    public Board(Card c, int xPos, int yPos){
        board = new Card[4][4];
        
        board[xPos][yPos] = c;

        plainText = false;
    }

    public Board(String s){
        board = new Card[4][4];

        if(s.toLowerCase().equals("debug")){
            for(int r = 0; r < board.length; r++){
                for(int c = 0; c < board[r].length; c++){
                    board[r][c] = new Card((int)(Math.random() * 10));
                }
            }
        }

        plainText = false;

    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int sampleMethod(int y)
    {
        // put your code here
        return 0;
    }
    public int findHighestLength(){
        int max = -1;
        
        
        for(Card[] row : board){
            for(Card c : row){
                if(c != null){
                    if(c.numTotalCharacters() > max){
                        max = c.numTotalCharacters();
                    }
                }
            }
        }
        
        return max;
    }

    public String printBlank(int lineNum){
        String out = plainText==true ? "" : ANSI_RESET;
        int spacing = findHighestLength();
        int offset = 0;

        if(spacing % 2 != 0){
            offset = 1;
        }

        if(lineNum == 0 || lineNum == 4){ //Prints out the top/bottom of the card
            for(int c = 0; c < spacing * 2 + offset; c++){
                out += "-";
            }
        } else { 
            out += "|";

            for(int c = 0; c < spacing * 2 - 2 + offset; c++){  
                out += " ";
            }
            out += "|";
        }       

        return out;
    }

    /**
     * isEmpty check if the point (x, y) on the board is empty.
     * @return false if is not empty, true if it is empty
     */
    public boolean isEmpty(int x, int y){
        return board[y - 1][x - 1] == null;
    }

    /**
     * isWall checks if the point (x, y) on the board is a wall
     * @return false if not a wall, true if it is a wall
     */
    public boolean isWall(int x, int y){
        return board[y - 1][x - 1].getOwner().toLowerCase().equals("none");
    }

    /**
     * setCard sets the card at the point (x, y) on the board
     */
    public void setCard(int x, int y, Card cd){
        board[y - 1][x - 1] = cd;
    }

    /**
     * getCard returns the card at the point (x, y) from the board
     */
    public Card getCard(int x, int y){
        return board[y - 1][x - 1];
    }

    /**
     * findCard returns the point xy from the board where the card is found.
     * @param c A card to search
     * @return a point on the board where the card is found in the format: xy. If not found, returns -1. It will return the first occuring card from the top left corner.
     */
    public int findCard(Card crd){
        for(int r = 0; r < board.length; r++){
            for(int c = 0; c < board[r].length; c++){
                if(board[r][c].equals(crd)){
                    return (c * 10) + r;
                }
            }
        }

        return -1;
    }


    /**
     * removeCard returns the card at the point (x, y) on the board
     * and removes the card from the board at that point
     */
    private Card removeCard(int x, int y){
        Card removed = board[y - 1][x - 1];
        board[y - 1][x - 1] = null;

        return removed;
    }

    public void reset(){
        for(int r = 0; r < board.length; r++){
            for(int c = 0; c < board[0].length; c++){
                removeCard(r, c);
            }
        }

        int wallsFilled = 0;

        int x = (int)(Math.random() * 4);
        int y = (int)(Math.random() * 4);

        while(wallsFilled != 3){
            if(board[x][y] == null){
                board[x][y] = new Card();
                wallsFilled++;
            }
            x = (int)(Math.random() * 4);
            y = (int)(Math.random() * 4);
        }
    }

    public int numFilled(){
        int occupied = 0;

        for(Card[] r : board){
            for(Card c : r){
                if(c != null){
                    occupied++;
                }
            }
        }

        return occupied;
    }

    public ArrayList<String> checkCollisions(Card c1, Card c2){
        ArrayList<String> colData = new ArrayList<String>();
        if(c1 == null || c2 == null){
            return colData;
        }

        String dir1 = c1.getDirections();
        String dir2 = c2.getDirections();


        if(dir1.length() >= dir2.length()){
            for(int i = 0; i < dir1.length(); i++){
                if(dir2.indexOf(dir1.substring(i, i+1)) != -1){
                    colData.add(dir1.substring(i, i+1));
                }
            }
        }

        return colData;
    }

    /**
     * findEnemies checks whether a target at (x, y) has collided with
     * an opponent around it.
     * @param x the column # of the target to check for collisions
     * @param y the row # of the target to check for collisions
     * @return An ArrayList of possible collisions in the form of two digits (integers), col#row#
     */
    public ArrayList<Integer> findEnemies(int x, int y){
        ArrayList<Integer> result = new ArrayList<Integer>();

        Card target = getCard(x, y);

        for(int row = y - 2; row <= y; row++){
            for(int col = x - 2; col <= x; col++){
                //Check if the (col, row) refers to a valid coordinate & not the target's location
                if(row >= 0 && col >= 0 && row < board[0].length && col < board.length && !(row == y - 1 && col == x - 1)){
                    //Check if the tile in (col, row) is not empty and not a wall
                    if(!isEmpty(col + 1, row + 1) && !isWall(col + 1, row + 1)){
                        //Check if the card in (col, row) belongs to the opponent
                        if(getCard(col + 1, row + 1).getOwner() != target.getOwner()){
                            //Add the position of the opposing unit in the form of two digits, col#row#
                            // to the ArrayList, result
                            result.add(((col + 1) * 10) + (row + 1));
                        } 
                    }
                }
            }
        }

        return result;
    }

    public ArrayList<Integer> findEnemiesDebug(int x, int y){
        ArrayList<Integer> result = new ArrayList<Integer>();

        Card target = getCard(x, y);
        System.out.println("X: " + x + ", Y: " + y);

        for(int row = y - 2; row <= y; row++){
            for(int col = x - 2; col <= x; col++){
                System.out.print("Checking (" + (col + 1) + ", " + (row + 1) + "): ");
                if(row >= 0 && col >= 0 && row <= board[0].length && col <= board.length && !(row == y - 1 && col == x - 1)){
                    System.out.print("Valid check. ");
                    if(!isEmpty(col + 1, row + 1) || !checkCollisions(getCard(y, x), getCard(col + 1, row + 1)).isEmpty()){
                        if(!isWall(col + 1, row + 1)){
                            if(getCard(col + 1, row + 1).getOwner() != target.getOwner()){
                                result.add(((col + 1) * 10) + (row + 1));
                                System.out.print("Collision found!");
                            } else {
                                System.out.print("Friendly Unit");
                            }
                        } else {
                            System.out.print("Wall detected.");
                        }
                    } else {
                        System.out.print("No collisions found.");
                    }
                    
                }
                System.out.println();
            }
        }

        return result;
    }




    /*
    public ArrayList<String> checkCollisions(){
        ArrayList<String> col = new ArrayList<String>();

        int currDir = 5;
        
        for(int r = -1; r < 1; r++){
            for(int c = -1; c < 1; c++){
                if(x > 1 && x < 4){
                    if(y > 1 && y < 4){
                        colTest = collision(p.getCard(cNum - 1), b.getCard(x + c, y + r));
                    }
                } else if ()
                //CONTINUE FROM HERE
                if(colTest != null && colTest.get(0).equals("true")){
                    currCol+= currDir % 8;
                }

                currDir++;
            }
        }

        if(currCol.length() == 0){
            System.out.println("No Collisions so far...");
        } else {
            System.out.print("Collision detected in the following directions: " + currCol);
        }

        return null;
    }
    */
    
    
    public String toString(){
        String out = "   ";
        int spacing = findHighestLength();
        int offset = 0;

        if(spacing % 2 != 0){
            offset = 1;
        }
        
        for(int c = 0; c < 4 ; c++){
            for(int j = 0; j < spacing * 2; j++){
                if(j == spacing + offset){
                    out += c + 1;
                }

                out += " ";
            }
        }
        
        out += "\n";
        
        for(int r = 0; r < board[0].length; r++){
            for(int i = 0; i < 5; i++){
                for(int c = 0; c < board.length; c++){
                    if(i == 2 && c == 0){
                        out += r + 1 + "  ";
                    } else if (c == 0) {
                        out += "   ";
                    }
                    
                    if(board[r][c] != null){
                        out += board[r][c].printLine(i) + " ";
                    } else {
                        out += printBlank(i) + " ";
                    }
                }
                out += "\n";
            }
           
        }
        return out;
    }
    
}
