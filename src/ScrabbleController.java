import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.*;

public class ScrabbleController implements Serializable {
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
            game.setGame(state);
            System.out.println("The move has been redone");
            board.updateBoardDisplay();
            board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles(game.getTurn()));
            board.displayRound(game.getRound());
        }
    }

    public void handleUndo(){
        if (game.round != 0){
            int index = game.round - 2;
            GameState state = game.getStates().get(index - 1);
            for(GameState str : game.getStates()){
                for (int i = 0; i < str.getBoard()[0].length; i++) {
                    for (int j = 0; j < str.getBoard()[0].length; j++) {
                        System.out.print(str.getBoard()[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }

            for (GameState st : game.getStates()) {
                System.out.println(st.getTurn() + " " + st.getRound());
            }
            board.setBoard(state.getBoard());
            game.setGame(state);
            System.out.println("The move has been undone");
//            board.updateBoardDisplay();
            board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles(game.getTurn()));
            board.displayRound(game.getRound());
        }

    }

    public boolean handleSave(){
        String gameName = JOptionPane.showInputDialog("Type in the name of the file that you want to save the game as");
        String boardName = JOptionPane.showInputDialog("Type in the name of the file that you want to save the board as");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(gameName + ".ser"))){
            out.writeObject(game.getGame());
        } catch (IOException e) {
            System.err.println("Error during game serialization: " + e.getMessage());
            return false;
        }
        try (ObjectOutputStream boardOut = new ObjectOutputStream(new FileOutputStream(boardName + ".ser"))){
            boardOut.writeObject(this.board);
        } catch (IOException e) {
            System.err.println("Error during board serialization: " + e.getMessage());
            return false;
        }
        return true;

    }
    public boolean handleLoad(){
        String gameName = JOptionPane.showInputDialog("Type in the name of the game file that you want to reload from");
        String boardName = JOptionPane.showInputDialog("Type in the name of the board file that you want to reload from");

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(gameName + ".ser"))) {
            System.out.println("Game about to be read");
            this.game = (Game)in.readObject();
            System.out.println("Game has been read");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during game deserialization: " + e.getMessage());
            return false;
        }

        System.out.println("This is the board before loading" + board);
        try (ObjectInputStream boardOut = new ObjectInputStream(new FileInputStream(boardName + ".ser"))) {
            Board b = (Board)boardOut.readObject();
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    this.board.getBoard()[i][j] = b.getBoard()[i][j];
                    System.out.println(board.getBoard()[i][j]);
                }
                System.out.println(" ");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during board deserialization: " + e.getMessage());
            return false;
        }

        char [][] b = this.board.getBoard();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(b[i][j]);
            }
            System.out.println(" ");
        }
//        game.setBoard(board);
        this.board.updateBoardDisplay();
        System.out.println("This is the board after loading" + b);
        board.updateScoreLabel(game.getCurrentPlayer().getPlayerScore(), game.getTurn());
        board.displayCurrentPlayerTiles(game.getCurrentPlayer().displayTiles(game.getTurn()));
        board.displayRound(game.getRound());
        return true;

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
