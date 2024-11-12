import java.util.ArrayList;
import java.util.Iterator;

public class Game {
    public static TileBag tilebag;
    public static WordDictionary wordDictionary;
    public static Board board;
    public static int turn;
    public ArrayList<Player> players;
    private static Player currentPlayer;
    public static int round = 1;
    public static int firstOrSecond = 1;

    public Game(Board board) {
        // initializations
        tilebag = new TileBag();
        wordDictionary = new WordDictionary("https://www.mit.edu/~ecprice/wordlist.10000");
        this.players = new ArrayList<>();
        Player player1 = new Player(board);
        Player player2 = new Player(board);
        currentPlayer = player1;
        this.board = board;

        // Give the players their tiles
        for (int i = 0; i < 7; i ++){
            player1.addTile(tilebag);
            player2.addTile(tilebag);
        }

        players.add(player1);
        players.add(player2);

        // Initialize the board and start the game
        turn = 1;
        System.out.println("Welcome to the Game of Scrabble");
    }

    public void play(String word, char direction, int row, int col) {
        getCurrentPlayer().playTurn(word, direction, row, col);
        switchTurn();
    }

    public void switchTurn(){
        round++;
        if (turn == 1){
            turn = 2;
            currentPlayer = players.get(1);
            firstOrSecond = 2;
        } else if (turn == 2) {
            turn = 1;
            currentPlayer = players.get(0);
            firstOrSecond = 1;
        }
    }

    public static int getRound(){
        return round;
    }

    public static boolean isValidPlacement(Board board, String word, char direction, int row, int column) {
        // Check for out-of-bounds conditions
        int round = getRound();
        if (direction == 'H' && (column + word.length()) > 15) {
            return false;
        } else if (direction == 'V' && (row + word.length()) > 15) {
            return false;
        }

        boolean hasAdjacent = false;

        for (int i = 0; i < word.length(); i++) {
            char characterOnBoard;

            // Set the character on the board depending on direction
            if (direction == 'H') {
                characterOnBoard = board.getBoard()[row][column + i];
            } else if (direction == 'V') {
                characterOnBoard = board.getBoard()[row + i][column];
            } else {
                return false; // Invalid direction
            }

            // Check if the tile on the board is either empty ('-') or matches the letter in the word
            if (characterOnBoard != '-') {
                if (characterOnBoard != word.charAt(i)) {
                    return false; // The board letter doesn't match the word letter
                }
            }

            // Check for adjacent tiles if `round > 1`
            if (round> 1) {
                if (direction == 'H') {
                    // Check above and below the current tile for adjacency
                    if ((row > 0 && board.getBoard()[row - 1][column + i] != '-') ||
                            (row < 14 && board.getBoard()[row + 1][column + i] != '-')) {
                        hasAdjacent = true;
                    }
                } else if (direction == 'V') {
                    // Check left and right of the current tile for adjacency
                    if ((column > 0 && board.getBoard()[row + i][column - 1] != '-') ||
                            (column < 14 && board.getBoard()[row + i][column + 1] != '-')) {
                        hasAdjacent = true;
                    }
                }
            }

            // Validate perpendicular word creation
            String perpendicularWord = newPerpendicularWord( row + (direction == 'V' ? i : 0), column + (direction == 'H' ? i : 0), direction);
            if (perpendicularWord.length() > 1 && !isValidWord(perpendicularWord)) {
                return false;
            }
        }

        // For rounds > 1, ensure there's at least one adjacent tile
        return round == 1 || hasAdjacent;
    }



    // checking for new word created, only for perpendicular will add parallel later
    public static String newPerpendicularWord(int row, int column, char direction){
        StringBuilder newWord = new StringBuilder();
        int startRow = row, startCol = column;

        if (direction == 'H') {
            while (startRow > 0 && board.getBoard()[startRow - 1][column] != '-') {
                startRow--;

            }
        }else {
            while (startCol > 0 && board.getBoard()[row][startCol - 1] != '-') {
                startCol--;
            }
        }

        // forming the word
        if (direction == 'H') {
            while (startRow <= 14 && board.getBoard()[startRow][column] != '-') {
                newWord.append(board.getBoard()[startRow][column]);
                startRow++;
            }
        } else {
            while (startCol <= 14 && board.getBoard()[row][startCol] != '-') {
                newWord.append(board.getBoard()[row][startCol]);
                startCol++;
                startCol++;
            }
        }
        return newWord.toString();
    }

    private static boolean isValidWord(String word){
        return wordDictionary.containsWord(word);
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static int calculateScore(String word, int row, int column, char direction) {

        int score = 0;
        System.out.println(word);
        for (char c: word.toCharArray()){
            Iterator<Tiles> iter = currentPlayer.getTiles().iterator();
            while(iter.hasNext()){
                Tiles tile = iter.next();
                //System.out.println("The letter is ");
                if(tile.getLetter() == Character.toUpperCase(c)){

                    System.out.println(tile.getScore());

                    score += tile.getScore();
                    break;
                }
            }
        }
        return score;
    }

    public static int calculatePerpendicularWordScore(String word, int row, int column, char direction) {

        int perpendicularScore = 0;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);

            // Only calculate perpendicular words for newly placed tiles
            if ((direction == 'H' && Game.board.getBoard()[row][column + i] == '-') ||
                    (direction == 'V' && Game.board.getBoard()[row + i][column] == '-')) {

                String perpendicularWord = newPerpendicularWord(row + (direction == 'H' ? 0 : i),
                        column + (direction == 'H' ? i : 0),
                        direction);

                // Check if a new perpendicular word was formed
                if (perpendicularWord.length() > 1 && wordDictionary.containsWord(perpendicularWord)) {
                    for (char perpendicularLetter : perpendicularWord.toCharArray()) {
                        perpendicularScore += getTileScore(perpendicularLetter);
                    }
                }
            }
        }

        return perpendicularScore;
    }

    public static int getTileScore(char letter) {
        for (Tiles tile : TileBag.getBag()) {
            if (tile.getLetter() == letter) {
                return tile.getScore();
            }
        }
        return 0; // default if tile not found
    }
}
