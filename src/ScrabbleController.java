import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class ScrabbleController{
    private Game game;
    private Board board;

    public ScrabbleController(Game game, Board board) {
        this.game = game;
        this.board = board;
    }

    public void handleButtonClick(int row, int col) {
        PlayEvent playEvent = board.displayInputFields();
        if (playEvent.getWord() != null && !playEvent.getWord().isEmpty()) {
            if (playEvent.getDirection() != null && (playEvent.getDirection().equalsIgnoreCase("H") || playEvent.getDirection().equalsIgnoreCase("V"))) {
                char dirChar = playEvent.getDirection().equalsIgnoreCase("H") ? 'H' : 'V';
//                if (game.getCurrentPlayer().place(playEvent.getWord(), dirChar, row, col)) {
                if (Game.isValidPlacement(board, playEvent.getWord(), dirChar, row, col)){
                    game.play(playEvent.getWord(), dirChar, row, col);
                    board.updateBoardDisplay();
                } else {
                    board.displayInvalidPlacement();
                }
            } else {
                board.displayInvalidDirection();
            }
        }
    }
}
