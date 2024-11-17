import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class ScrabbleController {
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
        String direction = playEvent.getDirection();

        if (playEvent.getWord() != null && !playEvent.getWord().isEmpty() && direction != null && (direction.equalsIgnoreCase("H") || direction.equalsIgnoreCase("V"))) {
            char dirChar = direction.equalsIgnoreCase("H") ? 'H' : 'V';

            if (!Game.wordDictionary.containsWord(playEvent.getWord())) {
                // Display invalid word pop-up
                board.displayInvalidWord();
                return;
            }

            if (Game.isValidPlacement(board, playEvent.getWord(), dirChar, row, col)) {
                game.play(playEvent.getWord(), dirChar, row, col);
                board.updateBoardDisplay();

                // Update player score
                //game.getCurrentPlayer().updatePlayerScore(playEvent.getWord(), dirChar, row, col);


                // Update score display on GUI
                int playerScore = game.getCurrentPlayer().getPlayerScore();
                board.updateScoreLabel(playerScore);
                //System.out.println("Player's current score: " + playerScore);

            } else {
                board.displayInvalidPlacement();
            }
        } else {
            // Handle invalid word or direction
            if (direction == null) {
                board.displayInvalidDirection(); // You could also show a message indicating the direction is missing
            } else {
                board.displayInvalidWord();
            }
        }

        board.displayRound(game.getRound());
        board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles());
    }
}
