import java.util.*;


public class Player {
    private int score =0;
    private List<Tiles> tiles;
    private Board board;

    public Player(Board board) {
        tiles = new ArrayList<>();
        this.score = 0;
        this.board = board;
    }

    public void addTile(TileBag tileBag) {
        tiles.add(tileBag.drawTile());
    }

    public String displayTiles() {
        StringBuilder str = new StringBuilder("Player");
        str.append(Game.turn).append("'s tiles:");
        for (Tiles tile : tiles) {
            str.append("\t").append("Tile: ").append(tile.getLetter()).append("(").append(tile.getScore()).append(") ");
        }
        return str.toString();
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

    public void playTurn(String word, char direction, int row, int col) {
        if (!Game.wordDictionary.containsWord(word)){
            board.displayInvalidWord();
        }

        if (!canFormWordFromTiles(word, board, row, col, direction)) {
            board.displayInvalidPlacement();
        }

        if (place(word, direction, row, col)){
            updatePlayerScore(word, direction, row, col);
            pickTile();
        }
    }

    //methods
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