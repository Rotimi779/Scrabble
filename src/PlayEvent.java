import javax.swing.*;
import java.util.EventObject;

public class PlayEvent extends EventObject {
    private String word;
    private String direction;

    public PlayEvent(Board board, String word, String direction) {
        super(board);
        this.word = word;
        this.direction = direction;
    }

    public String getWord(){
        return word;
    }


    public String getDirection(){
        return direction;
    }
}
