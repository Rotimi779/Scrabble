import java.io.Serializable;
import java.util.Arrays;

import static java.util.Arrays.copyOf;

public class GameState implements Serializable {
    private char[][] board;
    private Game game;
    private int turn;
    private int round;
    private int[] scores;


    public GameState(Board board, Game game) {
        scores = new int[game.players.size()];
        this.turn = game.getTurn();
        this.round = game.getRound();
//        this.board = new char[15][];
//        for (int i = 0; i < 15; i++){
//            this.board[i] = Arrays.copyOf(board.getBoard()[i], board.getBoard()[i].length);
//            System.out.println(Arrays.toString(this.board[i]));
//        }
        this.board = new char[15][15];
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                char c = board.getBoard()[i][j];
                this.board[i][j] = c;
            }
            System.out.println(Arrays.toString(this.board[i]));
        }

        this.board = board.getBoard();
        int i = 0;
        for(Player player : game.players){
            scores[i] = player.score;
        }
    }

    public int[] getScores() {
        return scores;
    }

    public int getTurn() {
        return turn;
    }

    public int getRound() {
        return round;
    }

    public int turn(){
        return turn;
    }
    public char[][] getBoard() {
        return board;
    }
}
