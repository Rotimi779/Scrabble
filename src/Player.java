import java.util.*;


public class Player {
    private int score;
    private List<Tiles> tiles;
    private static Scanner input;
    private Board board;

    public Player(Board board) {
        tiles = new ArrayList<>();
        this.score = 0;
        this.board = board;
    }

    public void addTile(TileBag tileBag) {
        tiles.add(tileBag.drawTile());
    }

    public int getScore(char letter) {
        for (Tiles tile : tiles) {
            if (tile.getLetter() == letter) {
                return tile.getScore();
            }
        }
        return 0;
    }

    public void removeTile(char letter, int score) {
        Iterator<Tiles> iterator = tiles.iterator();
        while (iterator.hasNext()) {
            Tiles tile = iterator.next();
            if ((letter == tile.getLetter()) && (tile.getScore() == score)) {
                iterator.remove();
                System.out.println("Removed: " + tile);
            }
        }
        System.out.println("No matching tile found for " + letter + "(" + score + ")");
    }

    public String displayTiles() {
        StringBuilder str = new StringBuilder("Player");
        str.append(Game.turn).append("'s tiles:");
        for (Tiles tile : tiles) {
            str.append("\t").append("Tile: ").append(tile.getLetter()).append(" Score: ").append(tile.getScore());
        }
        return str.toString();
    }

    public void updatePlayerScore(String word) {
        Iterator<Tiles> iterator = tiles.iterator();
        for (char i : word.toCharArray()) {
            score += getScore(i);
            while (iterator.hasNext()) {
                Tiles tile = iterator.next();
                if (Character.toLowerCase(tile.getLetter()) == i) {
                    score += tile.getScore();
                    iterator.remove();  // Use iterator to remove the tile
                    break;
                }
            }
        }
        System.out.println("New Score: " + score);
    }

    public void playTurn(String word, char direction, int row, int col) {
        if ((!Game.wordDictionary.containsWord(word)) || (!canFormWordFromTiles(word))) {
            System.out.println(Game.wordDictionary.containsWord(word) + " " + canFormWordFromTiles(word));
            if (place(word, direction, row, col)){
                updatePlayerScore(word);
                pickTile();
            }else{
                System.out.println("No matching tile found for " + word + "(" + score + ")");
            }
        }
    }

    //methods
    public boolean place(String word, char direction, int row, int column) {
        System.out.println("I am here");
        for (int i = 0; i < word.length(); i++) {
            if (direction == 'H') {
                if (Game.isValidPlacement(board, word, direction, row, column)) {
                    board.getBoard()[row][column + i] = word.charAt(i);
                } else {
                    System.out.println("Invalid placement");
                    return false;
                }
            } else if (direction == 'V') {
                System.out.println("I am NOW VERTICAL");
                if (Game.isValidPlacement(board, word, direction, row, column)) {
                    board.getBoard()[row + i][column] = word.charAt(i);
                } else {
                    System.out.println("Invalid placement");
                    return false;
                }
            }
        }
        //updatePlayerScore(word);
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

    public boolean canFormWordFromTiles(String word) {
        // Step 1: Count the available tiles
        Map<Character, Integer> tileCount = new HashMap<>();
        for (Tiles tile : tiles) {
            char character = Character.toLowerCase(tile.getLetter());
            tileCount.put(character, tileCount.getOrDefault(character, 0) + 1);
        }


        // Step 2: Check each letter in the word
        for (char letter : word.toCharArray()) {
            if (tileCount.containsKey(letter) && tileCount.get(letter) > 0) {
                // Decrease count for the letter
                tileCount.put(letter, tileCount.get(letter) - 1);
            } else {
                // Letter not available or used up
                return false;
            }
        }

        // Step 3: The word can be formed
        return true;
    }
}