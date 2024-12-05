import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.*;

import static java.util.Arrays.copyOf;

public class GameState {
    private String[] board;
    private Game game;
    private int turn;
    private int round;
    private int[] scores;
    private HashMap<Integer, List<Character>> tiles;
//    public static int counter = 0;


    public GameState(Board board, Game game) {
//        counter++;
        scores = new int[game.players.size()];
        int j = 0;
        for (Player player : game.players) {
            scores[j++] = player.score;
        }
        System.out.println("These are the scores for this save");
        for (int i = 0; i < game.players.size(); i++) {

            System.out.println(scores[i]);
        }



//        this.turn = (game.getTurn() - 1) % (game.players.size() + 1);

//        if (game.getRound() > 1){
//            this.round = game.getRound() - 1;
//        }else{
//            System.out.println("This is a round 0 occurrence");
//            this.round = game.getRound();
//        }
        this.turn = game.getTurn();
        this.round = game.getRound();
        System.out.println("Turn in this state: " + turn);
        System.out.println("Round in this state: " + round);
//        this.board = new char[15][];
//        for (int i = 0; i < 15; i++){
//            this.board[i] = Arrays.copyOf(board.getBoard()[i], board.getBoard()[i].length);
//            System.out.println(Arrays.toString(this.board[i]));
//        }
        this.tiles = new HashMap<>();
        int k = 0;
        for (Player player : game.players) {
            ArrayList<Character> tilesList = new ArrayList<>();
            for (Tiles tile : player.tiles) {
                tilesList.add(tile.getLetter());
            }
            tiles.put(k, tilesList);
            k++;
        }

        // testing
        int n = 1;
        for (List<Character> tiles1 : tiles.values()){
            System.out.println("This is for player " + n );
            for (char tiles2 : tiles1) {
                System.out.println(tiles2 + " " + TileBag.getScoreForTile(tiles2));
            }
            n++;
        }

        this.board = new String[15];
        StringBuilder row = new StringBuilder();
        System.out.println("This is the save for round " + round);
        for (int i = 0; i < 15; i++){
            row.append(board.getBoard()[i]);
            this.board[i] = String.valueOf(row);
            System.out.println(row);
            row.delete(0, row.length());
        }
        System.out.println();
        row.delete(0, row.length());
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

    public HashMap<Integer, List<Character>> getTiles() {
        return tiles;
    }

    public int turn(){
        return turn;
    }
    public String[] getBoard() {
        return board;
    }
}
