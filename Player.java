import java.util.*;

public class Player{
    private String color;
    private int score;
    private ArrayList<Card> deck;
    boolean plainText;
    public static final String ANSI_RESET = "\u001B[0m";

    public Player(){
        if((int)(Math.random() * 2 + 1) % 2 == 0){
            color = "red";
        } else {
            color = "blue";
        }

        score = 0;
        deck = generateDeck();
        plainText = false;
    }

    public Player(String c){
        color = c;
        score = 0;
        deck = generateDeck();
        plainText = false;
    }

    public Player(String c, boolean p){
        color = c;
        score = 0;
        deck = generateDeck(p);
        plainText = p;
    }

    public Player(Card[] d){
        if((int)(Math.random() * 2 + 1) % 2 == 0){
            color = "red";
        } else {
            color = "blue";
        }

        score = 0;
        plainText = false;
    }

    public void setScore(int s){
        score = s;
    }

    public void win(){
        score++;
    }

    public void lose(){
        score--;
    }

    public int getScore(){
        return score;
    }

    public String getColor(){
        return color;
    }

    public Card getCard(int p){
        return deck.get(p - 1);
    }

    public void removeCard(int p){
        deck.set(p - 1, null);
    }

    public boolean isEmpty(int cNum){
        return deck.get(cNum - 1) == null;
    }

    public void newHand(){
        deck = generateDeck();
    }
    
    public ArrayList<Card> getDeck(){
        return deck;
    }

    public ArrayList<Card> generateDeck(){
        deck = new ArrayList<Card>();

        for(int i = 0; i < 5; i++){
            deck.add(new Card(color));
        }

        return deck;
    }

    public ArrayList<Card> generateDeck(boolean p){
        deck = new ArrayList<Card>();

        for(int i = 0; i < 5; i++){
            deck.add(new Card(color, p));
        }

        return deck;
    }

    public int findHighestLength(){
        int max = -1;
        
        
        for(Card c : deck){
            if(c != null){
                if(c.numTotalCharacters() > max){
                    max = c.numTotalCharacters();
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

    public String toString(){
        String out = "";
        int spacing = findHighestLength();
        int offset = 0;

        if(spacing % 2 != 0){
            offset = 1;
        }

        for(int i = 0; i < deck.size(); i++){
            for(Card c : deck){
                if(c != null){
                    out += c.printLine(i) + " ";
                } else {
                    out += printBlank(i) + " ";
                }
            }
            out += "\n";
        }

        for(int i = 0; i < deck.size(); i++){
            for(int j = 0; j < spacing * 2; j++){
                if(j == spacing + offset){
                    out += i + 1;
                }

                out += " ";
            }
        }

        return out + "\n";
    }

}