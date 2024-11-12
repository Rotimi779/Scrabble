import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        Game game = new Game(board);
        ScrabbleController controller = new ScrabbleController(game,board);
        board.setController(controller);

    }

}
