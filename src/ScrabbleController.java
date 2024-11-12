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
        board.setController(this);
        board.displayRound(game.getRound());
        board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles());
    }

    public void handleButtonClick(int row, int col) {
        PlayEvent playEvent = board.displayInputFields();
        System.out.println(playEvent.getWord() + " " + playEvent.getDirection() + " " + row + " " + col);
        if (playEvent.getWord() != null && !playEvent.getWord().isEmpty() && game.getCurrentPlayer().canFormWordFromTiles(playEvent.getWord())) {
            if (playEvent.getDirection() != null && (playEvent.getDirection().equalsIgnoreCase("H") || playEvent.getDirection().equalsIgnoreCase("V"))) {
                char dirChar = playEvent.getDirection().equalsIgnoreCase("H") ? 'H' : 'V';
                System.out.println(dirChar);
                if (Game.isValidPlacement(board, playEvent.getWord(), dirChar, row, col)){
                    game.play(playEvent.getWord(), dirChar, row, col);
                    board.updateBoardDisplay();
                } else {
                    board.displayInvalidPlacement();
                }
            } else {
                board.displayInvalidDirection();
            }
        }else{
            board.displayInvalidWord();
        }

        board.displayRound(game.getRound());
        board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles());
    }
}
