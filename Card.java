
/** 
 * A card is described as a tile that can be placed on a board.
 * Each card has a set of values (in the following order):
 * 
 * Power | Job | HP | MP | PhysicalDef | MagicalDef | Direction
 * 
 * Power: An int value from 0 to 9
 * Job: A String value with job name. 
 *      F = Fighter, B = Black Mage, W = White Mage, X = None
 * HP: An int health point value from 0 to 9
 * MP: An int mana point value from 0 to 9
 * PhysicalDef: A defense against power attacks int value from 0 to 9
 * MagicalDef: A defense against magic attacks int value from 0 to 9
 * Direction: A String value ranging from 0 to 8 digits. Each digit can have a value from 0 to 7 indicating direction.
 *            0 = E, 1 = SE, 2 = S, 3 = SW, 4 = W, 5 = NW, 6 = N, 7 = NE
 * 
*/

public class Card
{
    public static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BGBLACK = "\u001B[40m";
    private static final String ANSI_BGRED = "\u001B[41m";
    private static final String ANSI_BGGREEN = "\u001B[42m";
    private static final String ANSI_BGYELLOW = "\u001B[43m";
    private static final String ANSI_BGBLUE = "\u001B[44m";
    private static final String ANSI_BGPURcE = "\u001B[45m";
    private static final String ANSI_BGCYAN = "\u001B[46m";
    private static final String ANSI_BGWHITE = "\u001B[47m";
    private static final String STR_RESET = "\033[0;0m";  
    private static final String STR_BOLD = "\033[0;1m"; 
    
    private int pow, hp, mp, pDef, mDef;
    private String job, dir, owner, currColorText;
    private boolean plainText;

    
    /**
     * Constructor for objects of class Card
     */
    public Card()
    {
        // Creates a default wall card.
        pow = 0;
        hp = 0;
        mp = 0;
        pDef = 0;
        mDef = 0;
        job = "X";
        plainText = false;
        dir = "";
        owner = "none";
        currColorText = ANSI_BGWHITE + ANSI_WHITE;
    }
    public Card(boolean p)
    {
        // Creates a default wall card with plainText option
        pow = 0;
        hp = 0;
        mp = 0;
        pDef = 0;
        mDef = 0;
        job = "X";
        plainText = p;
        dir = "";
        owner = "none";
        currColorText = plainText==true ? "" : ANSI_BGWHITE + ANSI_WHITE;
    }

    public Card(int p, int h, int m, int pD, int mD, String j, boolean pt, String d, String o)
    {
        pow = p;
        hp = h;
        mp = m;
        pDef = pD;
        mDef = mD;
        job = j;
        plainText = pt;
        dir = d;
        owner = o;
        currColorText = o.toLowerCase().equals("red") ? ANSI_RED:ANSI_BLUE;
    }
    public Card(int p){
        pow = p;
        hp = (int)(Math.random() * 10);
        mp = (int)(Math.random() * 10);
        pDef = (int)(Math.random() * 10);
        mDef = (int)(Math.random() * 10);
        job = generateJob();
        plainText = false;
        currColorText = "";
        //TEMP
        if((int)(Math.random() * 2 + 1) % 2 == 0){
            currColorText = ANSI_RED;
            owner = "red";
        } else {
            currColorText = ANSI_BLUE;
            owner = "blue";
        }

        setDirections();
    }


    public Card(int p, String o){
        pow = p;
        hp = (int)(Math.random() * 10);
        mp = (int)(Math.random() * 10);
        pDef = (int)(Math.random() * 10);
        mDef = (int)(Math.random() * 10);
        job = generateJob();
        plainText = false;
        owner = o;
        currColorText = o.toLowerCase().equals("red") ? ANSI_RED:ANSI_BLUE;

        setDirections();
    }

    public Card(int p, String d, String o){
        pow = p;
        hp = (int)(Math.random() * 10);
        mp = (int)(Math.random() * 10);
        pDef = (int)(Math.random() * 10);
        mDef = (int)(Math.random() * 10);
        job = generateJob();
        plainText = false;
        dir = d;
        owner = o;
        currColorText = o.toLowerCase().equals("red") ? ANSI_RED:ANSI_BLUE;
    }

    public Card(String o){
        pow = (int)(Math.random() * 10);
        hp = (int)(Math.random() * 10);
        mp = (int)(Math.random() * 10);
        pDef = (int)(Math.random() * 10);
        mDef = (int)(Math.random() * 10);
        job = generateJob();
        plainText = false;
        owner = o;
        currColorText = o.toLowerCase().equals("red") ? ANSI_RED:ANSI_BLUE;

        setDirections();
    }

    //temp
    public Card(String o, boolean p){
        pow = (int)(Math.random() * 10);
        hp = (int)(Math.random() * 10);
        mp = (int)(Math.random() * 10);
        pDef = (int)(Math.random() * 10);
        mDef = (int)(Math.random() * 10);
        job = generateJob();
        plainText = p;
        owner = o;
        currColorText = plainText==true ? "" : o.toLowerCase().equals("red") ? ANSI_RED:ANSI_BLUE;

        setDirections();
    }
    

    /**
     * An example of a method - recace this comment with your own
     *
     * @param  y  a samce parameter for a method
     * @return    the sum of x and y
     */
    public int someMethod(int y)
    {
        // put your code here
        return 0;
    }

    public void setDirections(){
        /** Generate a set of random directions, going in clockwise order
         * 0 = E, 1 = SE, 2 = S, 3 = SW, 4 = W, 5 = NW, 6 = N, 7 = NE
         * There can be up to 8 maximum directions set in one card, minimum 1.
         * Each direction is randomly generated from the choices above.
         */
        dir = "";

        for(int i = 0; i < (int)(Math.random() * 9) + 1; i++){
            int generator = (int)(Math.random() * 8);

            if(!dir.contains(Integer.toString(generator))){
                dir+=generator;
            }
        }
    }

    public String getDirections(){
        return dir;
    }
    
    public String generateJob(){
        switch((int)(Math.random() * 2)){
            case 0:
                return "F";
            case 1:
                return "B";
            default:
                return "X";
        }
    }
    public void setPlainText(){
        plainText = true;
    }
    
    public int numTotalCharacters(){
        int pOut = String.valueOf(pow).length();
        int hpOut = String.valueOf(hp).length();
        int mpOut = String.valueOf(mp).length();
        int pDefOut = String.valueOf(pDef).length();
        int mDefOut = String.valueOf(mDef).length();
       
        return pOut + hpOut + mpOut + pDefOut + mDefOut + 1;
    }

    public String printLine(int lineNum){
        int spacing = numTotalCharacters();
        String out = "";
        int offset = 0;

        if(spacing % 2 != 0){
            offset = 1;
        }

        if(lineNum == 0 || lineNum == 4){ //Prints out the top/bottom of the card
            out+= currColorText;

            for(int c = 0; c < spacing * 2 + offset; c++){
                if(c == (spacing * 2 + offset) / 3){
                    if(owner.toLowerCase().equals("red")){
                        out += "RED";
                        c += 2;
                    }
                    else if (owner.toLowerCase().equals("blue")){
                        out += "BLU";
                        c += 2;
                    } else {
                        out += "-";
                    }
                } else {
                    out += "-";
                }
            }

        } else if (lineNum == 1 || lineNum  == 3){ //Prints out the second/fourth line of the card
            out += currColorText + "|";
            for(int c = 0; c < spacing * 2 - 2 + offset; c++){ 
                if(lineNum == 1){
                    if(c == 0 && dir.contains("5")){ //NW arrow
                        out += "\\";
                    } else if (c == spacing - 2 + offset && dir.contains("6")){ //N Arrow
                        out += "^";
                    } else if (c == spacing * 2 - 3 + offset && dir.contains("7")){ // NE Arrow
                        out += "/";
                    } else {
                        out += " ";
                    }
                } else {
                    if(c == 0 && dir.contains("3")){ //SW Arrow
                        out += "/";
                    } else if (c == spacing - 2 + offset && dir.contains("2")){ //S Arrow
                        out += "v";
                    } else if (c == spacing * 2 - 3 + offset && dir.contains("1")){ //SE Arrow
                        out += "\\";
                    } else {
                        out += " ";
                    }
                }
            }
            out += "|";
        } else { //Draw the side borders, respective to the middle text
            out += currColorText + "|";
            for(int c = 0; c < (int)Math.ceil(spacing / 2.0) - 1; c++){ 
                if(c == 0 && dir.contains("4")){ //W Arrow
                    out+= "<";
                } else {
                    out += " ";
                }
            }

            if(owner.equals("none")){
                for(int i = 0; i < spacing; i++){
                    out+= ".";
                }
            } else if (!plainText){
                out += "" + ANSI_BGWHITE + ANSI_BLACK + pow 
                          + ANSI_BGBLACK + ANSI_WHITE + job 
                          + ANSI_RESET
                          + ANSI_RED + hp 
                          + ANSI_BLUE + mp 
                          + ANSI_BGRED + ANSI_WHITE + pDef 
                          + ANSI_BGBLUE + mDef
                          + ANSI_RESET;
            } else {
                out += pow + job + hp + mp + pDef + mDef;
            }

            out+= currColorText;

            for(int c = 0; c < (int)Math.ceil(spacing / 2.0) - 1; c++){ 
                if (c == (int)Math.ceil(spacing / 2.0) - 2 && dir.contains("0")){ //E Arrow
                    out+= ">";
                } else {
                    out += " ";
                }
            }

            out += "|";

        }       
        return out + (plainText==true ? "" : ANSI_RESET);
    }

    public void setOwner(String o){
        owner = o;
        currColorText = plainText==true ? "" : o.toLowerCase().equals("red") ? ANSI_RED:ANSI_BLUE;

        System.out.println("The owner of this card is set to player " + owner);
    }
    
    public String getOwner(){
        return owner;
    }

    public String toString(){
        return printLine(0) + "\n" + printLine(1) + "\n" + printLine(2) + "\n" + printLine(3) + "\n" + printLine(4) + "\n";
    }
    
    public String debug(){
        return "Pow: " + pow + " Job: " + job + " hp: " + hp + " mp: " + mp + " pDef: " + pDef + " mDef: " + mDef + "\nDirections: " + dir;
    }
}
