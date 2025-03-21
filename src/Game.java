import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import javax.sound.sampled.*;
import java.io.*;



public class Game implements Serializable {
    public static TileBag tilebag;
    public static WordDictionary wordDictionary;
    private Board board;
    private int turn;
    public ArrayList<Player> players;
    private Player currentPlayer;
    public int round = 1;
    //    public static int firstOrSecond = 1;
    private final Map<String, String> playedList;
    private Stack<GameState> gameStates;
    private Stack<GameState> redoStates;
    private Clip gameSound;
    private Clip winSound;


    public static Set<String> scoredWords = new HashSet<>();


    public Game(Board board) {
        // initializations
        tilebag = new TileBag();
        wordDictionary = new WordDictionary("https://www.mit.edu/~ecprice/wordlist.10000");
        this.players = new ArrayList<>();
        Player player1 = new Player(this);
        Player player2 = new Player(this);
        AIPlayer aiPlayer = new AIPlayer(this);
        currentPlayer = player1;
        playedList = new HashMap<>();
        this.board = board;
        gameStates = new Stack<GameState>();
        redoStates = new Stack<GameState>();

        // Give the players their tiles
        for (int i = 0; i < 7; i++) {
            player1.addTile(tilebag);
            player2.addTile(tilebag);
            aiPlayer.addTile(tilebag);
        }

        players.add(player1);
        players.add(player2);
        players.add(aiPlayer);

        // Initialize the board and start the game
        turn = 1;
        System.out.println("Welcome to the Game of Scrabble");
    }

    public Stack<GameState> getStates() {
        return gameStates;
    }

    public Stack<GameState> getRedoStates() {
        return redoStates;
    }

    public int getTurn() {
        return turn;
    }

    public Board getBoard() {
        return board;
    }

    public void setGame(GameState state) {
        int i = 0;
        for (Player player : players) {
            player.score = state.getScores()[i];
            int j = 0;
            for (char c : state.getTiles().get(i)) {
                if (player.tiles.get(j).getLetter() != c) {
                    player.tiles.remove(j);
                    player.tiles.add(j, new Tiles(c, tilebag.getScore(c))); // set the players tiles to that
                    tilebag.addTile(c, tilebag.getScore(c)); // add the tiles back to the bag
                }
            }
            i++;
        }
        this.turn = state.getTurn();
//        this.currentPlayer = this.players.get(turn % players.size());
        System.out.println("Turn: " + state.getTurn());
        if (state.getTurn() < 3) {
            this.currentPlayer = this.players.get(state.getTurn());
        } else {
            this.currentPlayer = this.players.get(state.getTurn() - 1);
        }
        this.round = state.getRound();
    }

    public void play(String word, char direction, int row, int col) {
        if (round == 1){
            String audio = JOptionPane.showInputDialog(null,"Enter first,second or third for the audio file you want");
            try
            {
                AudioInputStream gameStream;
                switch(audio){
                    case "first":
                        gameStream = AudioSystem.getAudioInputStream(new File("bleach_aizen_theme.wav"));
                        gameSound = AudioSystem.getClip();
                        gameSound.open(gameStream);
                        break;
                    case "second":
                        gameStream = AudioSystem.getAudioInputStream(new File("iron_man.wav"));
                        gameSound = AudioSystem.getClip();
                        gameSound.open(gameStream);
                        break;
                    case "third":
                        gameStream = AudioSystem.getAudioInputStream(new File("supa_strikas.wav"));
                        gameSound = AudioSystem.getClip();
                        gameSound.open(gameStream);
                        break;
                    default :
                        gameStream = AudioSystem.getAudioInputStream(new File("bleach_aizen_theme.wav"));
                        gameSound = AudioSystem.getClip();
                        gameSound.open(gameStream);
                        break;
                }


                AudioInputStream winStream = AudioSystem.getAudioInputStream(new File("adventure_time.wav"));
                winSound = AudioSystem.getClip();
                winSound.open(winStream);


                if (gameSound != null)
                {
                    gameSound.setFramePosition(0);
                    gameSound.start();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        if (currentPlayer.getTiles().size() == 0){
            int max = 0;
            int index = 1;
            for(Player p: players){
                if (p.getPlayerScore() > max){
                    max = p.getPlayerScore();
                    index = players.indexOf(currentPlayer);
                }
            }
            gameSound.stop();
            winSound.start();
            board.displayWinnerMessage(index + 1);
        }
        else{
            if (currentPlayer instanceof AIPlayer aiPlayer) {
                System.out.println("This is an AI player");
                if (aiPlayer.playTurn()) {
                    System.out.println("AIPlayer " + currentPlayer + " has played a tile.");
                    switchTurn();
                } else {
                    System.out.println("AIPlayer " + currentPlayer + " did not play a tile.");
                }
            } else {
                if (currentPlayer.playTurn(word, direction, row, col)) {
                    // store the words that have been played in the game
                    String value = row + col + String.valueOf(direction);
                    playedList.put(word, value);
                    switchTurn();
                }
            }
        }







//        System.out.println("1ST PLAY");
//        System.out.println("Round value is" + round);

    }

    public void switchTurn() {
//        gameStates.add(new GameState(board, this));
        round++;
        if ( round % players.size() == 0) {
            turn = players.size();
        } else {
            turn = (round % players.size());
        }

        currentPlayer = players.get((round - 1) % players.size());
        if (currentPlayer instanceof AIPlayer aiPlayer) {
            boolean played = false;
            System.out.println("I am AI and I rule!!!!");
            gameStates.add(new GameState(board, this));
            aiPlayer.playTurn();

            switchTurn();

        }

//        if (turn == 1){
//            turn = 2;
//            currentPlayer = players.get(1);
////            firstOrSecond = 2;
//        } else if (turn == 2) {
//            turn = 1;
//            currentPlayer = players.get(0);
////            firstOrSecond = 1;
//        }
    }

    public int getRound() {
        return round;
    }

    public boolean isValidPlacement(Board board, String word, char direction, int row, int column) {
        int round = getRound(); // Get the current round number

        // Check for out-of-bounds conditions
        if ((direction == 'H' && column + word.length() > 15) || (direction == 'V' && row + word.length() > 15)) {
            System.out.println("Out of bounds");
            return false;
        }

        boolean hasAdjacent = false;
        boolean allPerpendicularWordsValid = true;

        for (int i = 0; i < word.length(); i++) {
            int currentRow = row + (direction == 'V' ? i : 0);
            int currentCol = column + (direction == 'H' ? i : 0);
            char characterOnBoard = board.getBoard()[currentRow][currentCol];

            // Check for tile conflict
            if (characterOnBoard != '-' && characterOnBoard != word.charAt(i)) {
                System.out.println("Tile conflict at (" + currentRow + ", " + currentCol + ")");
                return false;
            }

            // Skip line word validation for the first round
            if (round > 1) {
                // Validate the full word in the row or column
                String fullLineWord = (direction == 'H')
                        ? getFullWordInRow(board, currentRow, column)  // Form the full horizontal word
                        : getFullWordInColumn(board, row, currentCol); // Form the full vertical word

                if (!fullLineWord.isEmpty() && !isValidWord(fullLineWord)) {
                    System.out.println("Invalid word formed: " + fullLineWord);
                    return false;
                }
            }

            // For rounds > 1, check for adjacent tiles
            if (round > 1) {
                if ((direction == 'H' && ((currentRow > 0 && board.getBoard()[currentRow - 1][currentCol] != '-') ||
                        (currentRow < 14 && board.getBoard()[currentRow + 1][currentCol] != '-'))) ||
                        (direction == 'V' && ((currentCol > 0 && board.getBoard()[currentRow][currentCol - 1] != '-') ||
                                (currentCol < 14 && board.getBoard()[currentRow][currentCol + 1] != '-')))) {
                    hasAdjacent = true;
                }

                // Validate perpendicular words
                String perpendicularWord = newPerpendicularWord(currentRow, currentCol, direction);
                if (perpendicularWord.length() > 1 && !isValidWord(perpendicularWord)) {
                    System.out.println("Invalid perpendicular word: " + perpendicularWord);
                    allPerpendicularWordsValid = false;
                }
            }
        }

        // Ensure the word itself is in the dictionary
        if (!isValidWord(word)) {
            System.out.println("The word " + word + " is not in the dictionary.");
            return false;
        }

        // Special logic for the first round
        if (round == 1) {
            boolean touchesMiddleTile = false;
            for (int i = 0; i < word.length(); i++) {
                int currentRow = row + (direction == 'V' ? i : 0);
                int currentCol = column + (direction == 'H' ? i : 0);
                if (currentRow == 7 && currentCol == 7) {
                    touchesMiddleTile = true;
                    break;
                }
            }
            if (!touchesMiddleTile) {
                System.out.println("The first word must touch the center tile (7,7).");
                return false;
            }
            // Skip adjacent checks for the first word
            return true;
        }

        // For rounds > 1, ensure the word is connected and valid
        return hasAdjacent && allPerpendicularWordsValid;
    }

    // Helper functions for forming full words
    private String getFullWordInRow(Board board, int row, int column) {
        StringBuilder fullWord = new StringBuilder();
        int startCol = column;
        while (startCol > 0 && board.getBoard()[row][startCol - 1] != '-') {
            startCol--;
        }
        for (int col = startCol; col < 15 && board.getBoard()[row][col] != '-'; col++) {
            fullWord.append(board.getBoard()[row][col]);
        }
        return fullWord.toString();
    }

    private String getFullWordInColumn(Board board, int row, int column) {
        StringBuilder fullWord = new StringBuilder();
        int startRow = row;
        while (startRow > 0 && board.getBoard()[startRow - 1][column] != '-') {
            startRow--;
        }
        for (int r = startRow; r < 15 && board.getBoard()[r][column] != '-'; r++) {
            fullWord.append(board.getBoard()[r][column]);
        }
        return fullWord.toString();
    }

    public String newPerpendicularWord(int row, int column, char direction) {
        StringBuilder newWord = new StringBuilder();

        // Determine traversal direction
        boolean isHorizontal = (direction == 'H');

        // Find the starting point for the perpendicular word
        int startRow = row, startCol = column;
        if (isHorizontal) {
            while (startRow > 0 && board.getBoard()[startRow - 1][column] != '-') {
                startRow--;
            }
        } else {
            while (startCol > 0 && board.getBoard()[row][startCol - 1] != '-') {
                startCol--;
            }
        }

        // Build the word by traversing forward
        if (isHorizontal) {
            while (startRow <= 14 && board.getBoard()[startRow][column] != '-') {
                newWord.append(board.getBoard()[startRow][column]);
                startRow++;
            }
        } else {
            while (startCol <= 14 && board.getBoard()[row][startCol] != '-') {
                newWord.append(board.getBoard()[row][startCol]);
                startCol++;
            }
        }

        String result = newWord.toString();
        if (result.isEmpty()) {
            System.out.println("No perpendicular word formed");
        }
        return result;
    }

    private static boolean isValidWord(String word) {
        return wordDictionary.containsWord(word);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int calculateScore(String word, int row, int column, char direction) {
        int score = 0;

        for (int i = 0; i < word.length(); i++) {
            // Calculate the position of the current tile
            int currentRow = direction == 'H' ? row : row + i;
            int currentCol = direction == 'H' ? column + i : column;

            JButton button = board.getButtons()[currentRow][currentCol];
            Color buttonColor = button.getBackground();

            // Get the tile score and apply letter multipliers
            int tileScore = getTileScore(this.currentPlayer, word.charAt(i));
            if (tileScore == -1) continue; // Skip if no matching tile is found

            tileScore = applyLetterMultiplier(tileScore, buttonColor);
            score += tileScore;


        }

        // Apply word multipliers
        for (int i = 0; i < word.length(); i++) {
            int currentRow = direction == 'H' ? row : row + i;
            int currentCol = direction == 'H' ? column + i : column;

            JButton button = board.getButtons()[currentRow][currentCol];
            score = applyWordMultiplier(score, button.getBackground());
        }

        return score;
    }

    // Helper method to get the tile score, including blank tile handling
    private static int getTileScore(Player currentPlayer, char letter) {
        Iterator<Tiles> iter = currentPlayer.getTiles().iterator();
        while (iter.hasNext()) {
            Tiles tile = iter.next();
            if (tile.getLetter() == Character.toUpperCase(letter)) {
                if (letter == '_') {
                    System.out.println("This is an empty tile");
                    String emptyTile = JOptionPane.showInputDialog("Enter a letter for the empty tile:");
                    tile.setLetter(emptyTile.charAt(0));
                    tile.setScore(emptyTile.charAt(0));
                }
                TileBag t = new TileBag();
                return t.getScoreForTile(Character.toUpperCase(letter));
            }
        }
        return 0;
    }

    // Helper method to apply letter multipliers
    private static int applyLetterMultiplier(int tileScore, Color color) {
        if (color.equals(Color.blue)) {
            return tileScore * 3; // Triple Letter Score
        } else if (color.equals(Color.cyan)) {
            return tileScore * 2; // Double Letter Score
        }
        return tileScore; // No multiplier
    }

    // Helper method to apply word multipliers
    private static int applyWordMultiplier(int currentScore, Color color) {
        if (color.equals(Color.red)) {
            return currentScore * 3; // Triple Word Score
        } else if (color.equals(Color.pink)) {
            return currentScore * 2; // Double Word Score
        }
        return currentScore; // No multiplier
    }
    //        for (char c: word.toCharArray()){
//            Iterator<Tiles> iter = currentPlayer.getTiles().iterator();
//            while(iter.hasNext()){
//                Tiles tile = iter.next();
//                //System.out.println("The letter is ");
//                if(tile.getLetter() == Character.toUpperCase(c)){
//                    //For handling blank tiles
//                    if (c == '_'){
//                        System.out.println("This is an empty tile");
//                        String emptyTile = JOptionPane.showInputDialog("Enter a letter for the letter you want to replace the empty tile");
//                        tile.setLetter(emptyTile.charAt(0));
//                        tile.setScore(emptyTile.charAt(0));
//                    }
//
//                    System.out.println(tile.getScore());
//
//                    score += tile.getScore();
//                    break;
//                }
//            }
//        }

    public int calculatePerpendicularWordScore(String word, int row, int column, char direction) {
        int totalPerpendicularScore = 0;

        // Only in round 1, mark the first word as placed in the set
        if (round == 1 && !scoredWords.contains(word)) {
            scoredWords.add(word);  // Mark the first word played as scored
            System.out.println("First word placed: " + word);
        }

        // Iterate through each character in the main word
        for (int i = 0; i < word.length(); i++) {
            int startRow = row;
            int startCol = column;

            // Adjust starting position based on direction for perpendicular word check
            if (direction == 'H') {
                startRow = row; // Fixed row for horizontal
                startCol = column + i; // Adjust column for each character in the word
            } else if (direction == 'V') {
                startCol = column; // Fixed column for vertical
                startRow = row + i; // Adjust row for each character in the word
            }

            // **Boundary Check**
            if (startRow < 0 || startRow >= board.getBoard().length ||
                    startCol < 0 || startCol >= board.getBoard()[0].length) {
                continue; // Skip processing if out of bounds
            }

            // **Form the new perpendicular word** (whether the spot is empty or occupied by a letter)
            if (board.getBoard()[startRow][startCol] == '-' || Character.isLetter(board.getBoard()[startRow][startCol])) {
                // If the spot is empty, form the perpendicular word
                String perpendicularWord = newPerpendicularWord(startRow, startCol, direction == 'H' ? 'V' : 'H');

                // Check if this is a valid perpendicular word
                if (!perpendicularWord.isEmpty() && perpendicularWord.length() > 1 && wordDictionary.containsWord(perpendicularWord)) {
                    // Skip the perpendicular word if it matches the first word placed during round 1
                    if (round == 1 && perpendicularWord.equalsIgnoreCase(word)) {
                        continue;  // Skip this as it's the first word placed
                    }

                    // Check if the perpendicular word has already been scored to avoid duplication
                    if (!scoredWords.contains(perpendicularWord)) {
                        System.out.println("New perpendicular word formed: " + perpendicularWord);

                        // Calculate the score for this perpendicular word
                        int perpendicularWordScore = calculatePerpendicularScore(perpendicularWord);
                        totalPerpendicularScore += perpendicularWordScore;

                        // Mark this word as scored
                        scoredWords.add(perpendicularWord);
                    }
                }
            }
        }

        return totalPerpendicularScore;
    }



    public int calculatePerpendicularScore(String perpendicularWord) {
        int score = 0;

        // Tile score mapping (letter -> score), typically based on Scrabble tile values.
        Map<Character, Integer> tileScores = new HashMap<>();
        tileScores.put('A', 1);
        tileScores.put('B', 3);
        tileScores.put('C', 3);
        tileScores.put('D', 2);
        tileScores.put('E', 1);
        tileScores.put('F', 4);
        tileScores.put('G', 2);
        tileScores.put('H', 4);
        tileScores.put('I', 1);
        tileScores.put('J', 8);
        tileScores.put('K', 5);
        tileScores.put('L', 1);
        tileScores.put('M', 3);
        tileScores.put('N', 1);
        tileScores.put('O', 1);
        tileScores.put('P', 3);
        tileScores.put('Q', 10);
        tileScores.put('R', 1);
        tileScores.put('S', 1);
        tileScores.put('T', 1);
        tileScores.put('U', 1);
        tileScores.put('V', 4);
        tileScores.put('W', 4);
        tileScores.put('X', 8);
        tileScores.put('Y', 4);
        tileScores.put('Z', 10);

        // Loop through each letter in the perpendicular word
        for (int i = 0; i < perpendicularWord.length(); i++) {
            char letter = perpendicularWord.charAt(i);

            // Add the letter score from the tile score map
            Integer letterScore = tileScores.get(Character.toUpperCase(letter));
            if (letterScore != null) {
                score += letterScore;
            }
        }

        return score;
    }

    //Get the game to serialize
    public Game getGame(){ return this;}

    //Set the board
    public void setBoard(Board b){this.board = b;}

}
