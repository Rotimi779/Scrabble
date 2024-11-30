import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ScrabbleController {
    private Game game;
    private Board board;

    public ScrabbleController(Game game, Board board) {
        this.game = game;
        this.board = board;
        board.setController(this);
        game.getStates().add(new GameState(board, game));
        board.displayRound(game.getRound());
        board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles(game.getTurn()));
    }

    public void handleButtonClick(int row, int col) {
        PlayEvent playEvent = board.displayInputFields();
        String direction = playEvent.getDirection();

        if (playEvent.getWord() != null && !playEvent.getWord().isEmpty() && direction != null && (direction.equalsIgnoreCase("H") || direction.equalsIgnoreCase("V"))) {
            char dirChar = direction.equalsIgnoreCase("H") ? 'H' : 'V';

            if (!game.getCurrentPlayer().canFormWordFromTiles(playEvent.getWord(), board, row, col, dirChar)) {
                board.displayInvalidWord();
            } else if (!game.isValidPlacement(board, playEvent.getWord(), dirChar, row, col)) {
                board.displayInvalidPlacement();
            }else {
                if (!(game.getCurrentPlayer() instanceof AIPlayer)){
                    game.play(playEvent.getWord(), dirChar, row, col);
                    game.getStates().add(new GameState(board, game));
                    board.updateBoardDisplay();

                    // Update player score
                    //game.getCurrentPlayer().updatePlayerScore(playEvent.getWord(), dirChar, row, col);


                    // Update score display on GUI
                    int playerScore = game.getCurrentPlayer().getPlayerScore();
                    board.updateScoreLabel(playerScore, game.getTurn());
                    //System.out.println("Player's current score: " + playerScore);
                }
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
        board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles(game.getTurn()));
    }

    public void handleRedo(){
        if (game.round != game.getStates().size()){
            int index = game.round - 1;
            GameState state = game.getStates().get(index + 1);
            board.setBoard(state.getBoard());
            game.setGame(state.getGame());
            System.out.println("The move has been redone");
            board.updateBoardDisplay();
            board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles(game.getTurn()));
            board.displayRound(game.getRound());
        }
    }

    public void handleUndo(){
        if (game.round != 0){
            int index = game.round - 1;
            GameState state = game.getStates().get(index - 1);
            for (GameState st : game.getStates()) {
                System.out.println(st.getGame().getTurn() + " " + st.getGame().getRound());
            }
            board.setBoard(state.getBoard());
            game.setGame(state.getGame());
            System.out.println("The move has been undone");
            board.updateBoardDisplay();
            board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles(game.getTurn()));
            board.displayRound(game.getRound());
        }

    }

    public void handleSave(){
        String filename = JOptionPane.showInputDialog("Type in the name of the file that you want to save the file as");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename + ".ser"))){
            out.writeObject(game.getGame());
        } catch (IOException e) {
            System.err.println("Error during serialization: " + e.getMessage());
        }
    }
    public void handleLoad(){

    }

    public void handleExit(){
        if (board.displayDisplayQuitConfirmation()){
            System.exit(0);
        }

    }

    public void handlePlay(){

    }

    public void handleHelp(){

    }
}
