import javax.swing.*;
import java.io.Serializable;
import java.util.*;


public class Player implements Serializable {
    protected int score = 0;
    protected List<Tiles> tiles;
    protected Game game;
    private int blank_space = 0;

    public Player(Game game) {
        tiles = new ArrayList<>();
        this.score = 0;
        this.game = game;
    }

    public void addTile(TileBag tileBag) {
        tiles.add(tileBag.drawTile());
    }

    public String displayTiles(int turn) {
        StringBuilder str = new StringBuilder("Player");
        str.append(turn).append("'s tiles:");
        for (Tiles tile : tiles) {
            str.append("\t").append("Tile: ").append(tile.getLetter()).append("(").append(tile.getScore()).append(") ");
        }
        return str.toString();
    }

    public void updatePlayerScore(String word, char direction, int row, int column) {
        //for debugging checking to see if called
        System.out.println("Updating score for word: " + word);  // Debugging line

        int score = game.calculateScore(word, row, column, direction); // Main word score

        // Check and add scores for new perpendicular words formed
        for (int i = 0; i < word.length(); i++) {
            int perpendicularScore = 0;

            if (direction == 'H') {
                // Check vertically if a new word is created
                perpendicularScore = game.calculatePerpendicularWordScore(word, row, column + i, 'V');
            } else if (direction == 'V') {
                // Check horizontally if a new word is created
                perpendicularScore = game.calculatePerpendicularWordScore(word, row + i, column, 'H');
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

    public boolean playTurn(String word, char direction, int row, int col) {
        if (word.contains(String.valueOf('_'))) {
            while(word.contains(String.valueOf('_'))){
                String emptyTile = JOptionPane.showInputDialog("Enter a letter for the empty tile:");
                StringBuilder sb = new StringBuilder(word);
                int index = word.indexOf('_');
                if (index != -1) {
                    sb.setCharAt(index, emptyTile.charAt(0));
                }
                word = sb.toString();
                blank_space += 1;
            }

            if (place(word, direction, row, col)){
                updatePlayerScore(word, direction, row, col);
                pickTile();
                return true;
            }
        }


        if (Game.wordDictionary.containsWord(word) && canFormWordFromTiles(word, game.getBoard(), row, col, direction)) {
            if (place(word, direction, row, col)){
                updatePlayerScore(word, direction, row, col);
                pickTile();
                return true;
            }
        }

        if (!Game.wordDictionary.containsWord(word)){
            game.getBoard().displayInvalidWord();
        }

        if (!canFormWordFromTiles(word, game.getBoard(), row, col, direction)) {
            game.getBoard().displayInvalidPlacement();
        }

        return false;
    }

    //methods
    public boolean place(String word, char direction, int row, int column) {
        for (int i = 0; i < word.length(); i++) {
            if (direction == 'H') {
                if (game.isValidPlacement(game.getBoard(), word, direction, row, column)) {

                    game.getBoard().getBoard()[row][column + i] = word.charAt(i);
                } else {
                    System.out.println("Invalid placement");
                    return false;
                }
            } else if (direction == 'V') {
                if (game.isValidPlacement(game.getBoard(), word, direction, row, column)) {
                    game.getBoard().getBoard()[row + i][column] = word.charAt(i);
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
        //return false;
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

        // Step 0: Check bounds to prevent ArrayIndexOutOfBoundsException
        if ((direction == 'H' && col + word.length() > board.getBoard()[0].length) ||
                (direction == 'V' && row + word.length() > board.getBoard().length)) {
            return false; // Word placement exceeds board boundaries
        }

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