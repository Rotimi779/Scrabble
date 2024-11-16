import java.util.*;

public class AIPlayer {
    Board board;
    private int score;
    private List<Tiles> tiles;
    public AIPlayer(Board board) {
        this.board = board;
        this.score = 0;
        this.tiles = new ArrayList<Tiles>();
    }

    public void playMove() {
        // finding the best placement on board
        char[][] board1 = board.getBoard();
        int row = 0;
        int col = 0;
        char maxTile = board1[0][0]; // the maximum tile is the first tile in the board
        for ( int i = 0; i < board1.length; i++ ) { // iterate over the tiles
            for ( int j = 0; j < board1[0].length; j++ ) {
                if (board1[i][j] != '-') { // check if a tile is there
                    int score = Game.tilebag.getScore(board1[i][j]); // get score of tile
                    if (score > Game.tilebag.getScore(maxTile)) { // swap for the bigger tile
                        if (board1[i+1][j] == '-' || board1[i][j+1] == '-' || board1[i-1][j] == '-' || board1[i][j-1] == '-') {
                            row = i;
                            col = j;
                            maxTile = board1[i][j]; // basically checking if it has a space for a new word to be formed with it
                        }

                    }

                }
            }
        }

        // case 1: if placing a whole word
        // checking for best word from valid words in dictionary
        for (String str : Game.wordDictionary.getWords()) {
            if (canFormWordFromTiles(str, board, row, col, 'H')) { // checks if it can form the word horizontally
                if (place(str, 'H', row, col)){
                    updatePlayerScore(str, 'H', row, col);
                    pickTile();
                    return;
                }
            } else if (canFormWordFromTiles(str, board, row, col, 'V')) { // checks if it can form the word vertically
                if (place(str, 'V', row, col)){
                    updatePlayerScore(str, 'V', row, col);
                    pickTile();
                    return;
                }
            }
        }

        // case 2: player does not have enough tiles to play a valid word
        // it should first look if it can add a suffix or prefix to an existing word, if not then it should pass the turn
        // we might need to keep track of the played words in a static array in game class or something
        // we could use a hash map to map from the played word to its location and direction as a string (format: "rcd")
        // r - row, c - column, d - direction
    }

    public void updatePlayerScore(String word, char direction, int row, int column) {
        //for debugging checking to see if called
        System.out.println("Updating score for word: " + word);  // Debugging line

        int score = Game.calculateScore(word, row, column, direction); // Main word score

        // Check and add scores for new perpendicular words formed
        for (int i = 0; i < word.length(); i++) {
            int perpendicularScore = 0;

            if (direction == 'H') {
                // Check vertically if a new word is created
                perpendicularScore = Game.calculatePerpendicularWordScore(word, row, column + i, 'V');
            } else if (direction == 'V') {
                // Check horizontally if a new word is created
                perpendicularScore = Game.calculatePerpendicularWordScore(word, row + i, column, 'H');
            }

            score += perpendicularScore;
            System.out.println("Perpendicular score added :" + perpendicularScore);

        }

        // Update the player's total score
        this.score += score;
        System.out.println("Total score for word " + word + ": " + score);

        //Remove the tiles if they have been used
        for (char s: word.toCharArray()){
            Iterator<Tiles> iter = tiles.iterator();
            while(iter.hasNext()){
                Tiles tile = iter.next();
                if(tile.getLetter() == Character.toUpperCase(s)){
                    iter.remove();
                    break;
                }
            }

        }
    }

//    private BestMove calculateBestMove() {
//
//    }

    public boolean place(String word, char direction, int row, int column) {
        for (int i = 0; i < word.length(); i++) {
            if (direction == 'H') {
                if (Game.isValidPlacement(board, word, direction, row, column)) {
                    board.getBoard()[row][column + i] = word.charAt(i);
                } else {
                    System.out.println("Invalid placement");
                    return false;
                }
            } else if (direction == 'V') {
                if (Game.isValidPlacement(board, word, direction, row, column)) {
                    board.getBoard()[row + i][column] = word.charAt(i);
                } else {
                    System.out.println("Invalid placement");
                    return false;
                }
            }
        }
        return true;
    }

    public String displayTiles() {
        StringBuilder str = new StringBuilder("AI Player");
        str.append(Game.turn).append("'s tiles:");
        for (Tiles tile : tiles) {
            str.append("\t").append("Tile: ").append(tile.getLetter()).append(" Score: ").append(tile.getScore());
        }
        return str.toString();
    }

    public void addTile(TileBag tileBag) {
        tiles.add(tileBag.drawTile());
    }

    public void pickTile() {
        //For refilling players tiles
        while (this.getNumberOfTiles() < 7) {
            if (Game.tilebag.getBag().isEmpty()) {
                System.out.println("The tile bag is empty");
                break;
            }
            this.addTile(Game.tilebag);
        }
    }

    public int getNumberOfTiles() {
        return tiles.size();
    }

    public List<Tiles> getTiles() {
        return tiles;
    }

    public int getPlayerScore() {
        return this.score;
    }

    public boolean canFormWordFromTiles(String word, Board board, int row, int col, char direction) {
        // Step 1: Count the available tiles
        Map<Character, Integer> tileCount = new HashMap<>();
        for (Tiles tile : tiles) {
            char character = Character.toLowerCase(tile.getLetter());
            tileCount.put(character, tileCount.getOrDefault(character, 0) + 1);
        }


        // Step 2: Check each letter in the word and check intersections
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            boolean isLetterOnBoard = false;

            // Check if the letter is already on the board (part of an intersection)
            if (direction == 'H') {
                if (board.getBoard()[row][col + i] != '-' && board.getBoard()[row][col + i] == letter) {
                    isLetterOnBoard = true;
                }
            } else if (direction == 'V') {
                if (board.getBoard()[row + i][col] != '-' && board.getBoard()[row + i][col] == letter) {
                    isLetterOnBoard = true;
                }
            }

            // If the letter is on the board, no need to use a tile from the player's hand
            if (isLetterOnBoard) {
                continue; // Skip this letter since it's already on the board
            }

            // If the letter is not on the board, player has in their tiles
            if (tileCount.containsKey(letter) && tileCount.get(letter) > 0) {
                tileCount.put(letter, tileCount.get(letter) - 1); // Decrease count for the letter
            } else {
                return false;
            }
        }

        // Step 3: The word can be formed
        return true;
    }
}
