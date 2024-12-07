import java.util.*;

public class AIPlayer extends Player {

    public AIPlayer(Game game) {
        super(game);
    }

    public boolean playTurn() {
        // finding the best placement on board
        char[][] board1 = game.getBoard().getBoard();
        HashMap<Integer, Integer> avoidTiles = new HashMap<>();
        StringBuilder tileString = new StringBuilder();
        for (Tiles tile : tiles) {
            tileString.append(tile.getLetter());
        }
        System.out.println("The AI has " + tileString);
        List<LegalMove> legalMoves = new ArrayList<LegalMove>();

        // get a subset of legal moves
        for (String str : Game.wordDictionary.getWords()) {
            for (int i = 0; i < 14; i++) {
//                System.out.println("This is the row: "+ i);
                for (int j = 0; j < 14; j++) {
//                    System.out.println("This is the column: "+ j);
                    if (canFormWordFromTiles(str, game.getBoard(), i, j, 'H') && game.isValidPlacement(game.getBoard(), str, 'H', i, j)) {
                        legalMoves.add(new LegalMove(i, j, 'H', str));
                    }
                    // Check vertical placement
                    if (canFormWordFromTiles(str, game.getBoard(), i, j, 'V') && game.isValidPlacement(game.getBoard(), str, 'V', i, j)) {
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
            int currentScore = game.calculateScore(move.getWord(), move.getRow(), move.getCol(), move.getDirection());

            // Validate that the placement doesn't create invalid words
            if (isValidPlacementWithIntersections(move, game.getBoard()) &&
                    isValidPlacementWithLine(move.getRow(), move.getCol(), move.getDirection(), move.getWord(), game.getBoard()) &&
                    game.isValidPlacement(game.getBoard(), move.getWord(), move.getDirection(), move.getRow(), move.getCol())) {
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
                String perpendicularWord = game.newPerpendicularWord(row, col + i, 'V');
                if (!perpendicularWord.isEmpty() && !Game.wordDictionary.containsWord(perpendicularWord)) {
                    return false;
                }
            } else if (direction == 'V') {
                // Check if placing this letter creates a valid horizontal word
                String perpendicularWord = game.newPerpendicularWord(row + i, col, 'H');
                if (!perpendicularWord.isEmpty() && !Game.wordDictionary.containsWord(perpendicularWord)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int row, int col, char dir, Board board, String word){
        int i = 1;


        // checks if the it
        if (dir == 'V'){
            // checks if the tile behind the start is empty
            while (board.getBoard()[row][col + i] == '-' && board.getBoard()[row - 1][col] == '-' && game.isValidPlacement(board, word, 'H', i, dir)) {
                i++;
            }
        }else if (dir == 'H'){
            // checks if the tile behind the start is empty
            while (board.getBoard()[row + i][col] == '-' && board.getBoard()[row][col - 1] == '-' && game.isValidPlacement(board, word, 'H', i, dir)) {
                i++;
            }
        }

        // if its
        if (Game.wordDictionary.containsWord(word) && i >= word.length() - 1) {
            return true;
        }
        return false;
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
        super.updatePlayerScore(word, direction, row, column);
    }

    public boolean place(String word, char direction, int row, int column) {
        return super.place(word, direction, row, column);
    }

    public String displayTiles(int turn) {
        return super.displayTiles(turn);
    }

    public void addTile(TileBag tileBag) {
        super.addTile(tileBag);
    }

    public void pickTile() {
        super.pickTile();
    }

    public int getNumberOfTiles() {
        return super.getNumberOfTiles();
    }

    public List<Tiles> getTiles() {
        return super.getTiles();
    }

    public int getPlayerScore() {
        return super.getPlayerScore();
    }

    public boolean canFormWordFromTiles(String word, Board board, int row, int col, char direction) {
        return super.canFormWordFromTiles(word, board, row, col, direction);
    }

}
