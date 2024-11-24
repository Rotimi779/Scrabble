import java.util.*;

public class AIPlayer extends Player {
    Board board;
    private int score;
    private List<Tiles> tiles;
    public AIPlayer(Board board) {
        super(board);
        this.board = board;
        this.score = 0;
        this.tiles = new ArrayList<Tiles>();
    }

    public boolean playTurn() {
        // finding the best placement on board
        char[][] board1 = board.getBoard();
        HashMap<Integer, Integer> avoidTiles = new HashMap<>();
        StringBuilder tileString = new StringBuilder();
        for (Tiles tile : tiles) {
            tileString.append(tile.getLetter());
        }
        List<LegalMove> legalMoves= new ArrayList<LegalMove>();

        // get a subset of legal moves
        for (String str : Game.wordDictionary.getWords()) {
            for (int i = 0; i < 14; i++){
//                System.out.println("This is the row: "+ i);
                for (int j = 0; j < 14; j++){
//                    System.out.println("This is the column: "+ j);
                    if (canFormWordFromTiles(str, board, i, j, 'H') && Game.isValidPlacement(board, str, 'H', i, j)) {
                        legalMoves.add(new LegalMove(i, j, 'H', str));
                    }
                    // Check vertical placement
                    if (canFormWordFromTiles(str, board, i, j, 'V') && Game.isValidPlacement(board, str, 'V', i, j)) {
                        legalMoves.add(new LegalMove(i, j, 'V', str));
                    }

                }
            }
        }
//        System.out.println(legalMoves.size());
//        for (LegalMove move : legalMoves) {
//            System.out.println("Word: " + move.getWord());
//            System.out.println("Row: " + move.getRow());
//            System.out.println("Col: " + move.getCol());
//            System.out.println("Direction: " + move.getDirection());
//        }

        // compute the highest scoring legal word
        int max_score = 0;
        int position = -1;
        LegalMove bestMove = null;

        for (LegalMove move : legalMoves) {
            int currentScore = Game.calculateScore(move.getWord(), move.getRow(), move.getCol(), move.getDirection());

            // Validate that the placement doesn't create invalid words
            if (isValidPlacementWithIntersections(move, board) &&
                    isValidPlacementWithLine(move.getRow(), move.getCol(), move.getDirection(), move.getWord(), board) &&
                    Game.isValidPlacement(board, move.getWord(), move.getDirection(), move.getRow(), move.getCol())) {
                if (currentScore > max_score) {
                    max_score = currentScore;
                    bestMove = move;
                }
            }
        }
        System.out.println("Max score: " + max_score);

        if (bestMove != null) {
            place(bestMove.getWord(), bestMove.getDirection(), bestMove.getRow(), bestMove.getCol());
            updatePlayerScore(bestMove.getWord(), bestMove.getDirection(), bestMove.getRow(), bestMove.getCol());
            pickTile();
            System.out.println("AI played: " + bestMove.getWord());
            return true;
        }

        System.out.println("AI has no valid moves and skips the turn.");
        return false;
    }

    private boolean isValidPlacementWithIntersections(LegalMove move, Board board) {
        int row = move.getRow();
        int col = move.getCol();
        String word = move.getWord();
        char direction = move.getDirection();

        for (int i = 0; i < word.length(); i++) {
            char currentLetter = word.charAt(i);

            if (direction == 'H') {
                // Check if placing this letter creates a valid vertical word
                String perpendicularWord = Game.newPerpendicularWord(row, col + i, 'V');
                if (!perpendicularWord.isEmpty() && !Game.wordDictionary.containsWord(perpendicularWord)) {
                    return false;
                }
            } else if (direction == 'V') {
                // Check if placing this letter creates a valid horizontal word
                String perpendicularWord = Game.newPerpendicularWord(row + i, col, 'H');
                if (!perpendicularWord.isEmpty() && !Game.wordDictionary.containsWord(perpendicularWord)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidPlacementWithLine(int row, int col, char direction, String word, Board board) {
        StringBuilder fullLine = new StringBuilder();

        if (direction == 'H') {
            // Form the entire row
            int startCol = col;
            while (startCol > 0 && board.getBoard()[row][startCol - 1] != '-') {
                startCol--;
            }
            for (int i = startCol; i < board.getBoard()[row].length && board.getBoard()[row][i] != '-'; i++) {
                fullLine.append(board.getBoard()[row][i]);
            }
        } else if (direction == 'V') {
            // Form the entire column
            int startRow = row;
            while (startRow > 0 && board.getBoard()[startRow - 1][col] != '-') {
                startRow--;
            }
            for (int i = startRow; i < board.getBoard().length && board.getBoard()[i][col] != '-'; i++) {
                fullLine.append(board.getBoard()[i][col]);
            }
        }

        // Check if the entire line is valid
        return Game.wordDictionary.containsWord(fullLine.toString());
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
            if (row < board.getBoard().length - 1 && col < board.getBoard().length - 1) {
                if (direction == 'H') {
                    if (board.getBoard()[row][col + i] != '-' && board.getBoard()[row][col + i] == letter) {
                        isLetterOnBoard = true;
                    }
                } else if (direction == 'V') {
                    if (board.getBoard()[row + i][col] != '-' && board.getBoard()[row + i][col] == letter) {
                        isLetterOnBoard = true;
                    }
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
