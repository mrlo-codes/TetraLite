import java.util.*;

public class Main{
    public static void main(String[] args){
        Card c = new Card(1);
        Card d = new Card(10);
        Card e = new Card(101);
        
        /*
        System.out.println(c.numTotalCharacters());
        System.out.println(c.debug());
        c.setPlainText();
        System.out.println(c);
        
        System.out.println(d.numTotalCharacters());
        System.out.println(d.debug());
        System.out.println(d);
        
        System.out.println(e.numTotalCharacters());
        System.out.println(e.debug());
        System.out.println(e);
        
        */

        Game g = new Game();
        System.out.println(g);
        g.play();

    }
    
    
}
