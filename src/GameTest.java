import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Board board;

    @org.junit.jupiter.api.Test
    void isValidPlacement() {
        // commit
        board = new Board();
        game = new Game(board);

        assertFalse(game.isValidPlacement(board, "food", 'H', 14, 14));
        assertFalse(game.isValidPlacement(board, "food", 'V', 14, 5));

        // normal/central case
        assertTrue(game.isValidPlacement(board, "food", 'H', 7, 7));
        assertTrue(game.isValidPlacement(board, "fake", 'V', 7, 7));

    }

    @org.junit.jupiter.api.Test
    void calculateScore() {
        board = new Board(); 
        game = new Game(board);
        assertTrue(game.calculateScore("sit", 0, 0, 'H') == 3);
    }

    @org.junit.jupiter.api.Test
    void calculatePerpendicularWordScore() {
//        board = new Board();
//        game = new Game(board);
//        assertEquals(Game.calculateScore("abs",0,0,'H'), 5);
//        assertEquals(Game.calculatePerpendicularWordScore("abs",1,0,'H'),);

    }

    @org.junit.jupiter.api.Test
    void calculatePerpendicularScore() {
        String word = "bat";
        int score = game.calculatePerpendicularScore(word);

        // Expected score: B = 3, A = 1, T = 1 -> Total = 5
        assertEquals(5, score, "Score for 'bat' should be 5 points.");

        word = "quiz";
        score = game.calculatePerpendicularScore(word);

        // Expected score: Q = 10, U = 1, I = 1, Z = 10 -> Total = 22
        assertEquals(22, score, "Score for 'quiz' should be 22 points.");
    }

    @org.junit.jupiter.api.Test
    void coloredSquareTest() {
        board = new Board();
        game = new Game(board);
        assertTrue(game.isValidPlacement(board,"eat",'H',7,7));
        assertTrue(game.calculateScore("eat",7,7,'H') == 6);
    }

    @org.junit.jupiter.api.Test
    void emptyTileTest() {
        Board board1 = new Board();
        Game game = new Game(board1);
        Player player = new Player(game);
        for(int i = 0; i < 3; i ++){
            player.addTile(Game.tilebag);
        }
        player.getTiles().add(new Tiles('_',0));
        player.getTiles().add(new Tiles('A',1));
        player.getTiles().add(new Tiles('T',1));
        //Type in the letter b or c
        player.playTurn("_at",'H',7,7);
    }

    @Test
    void undoTest(){
        board = new Board();
        game = new Game(board);
        ScrabbleController controller = new ScrabbleController(game, board);
        board.setController(controller);
        game.getStates().add(new GameState(board, game));
        game.getCurrentPlayer().place("sit", 'H', 7, 7);
        game.getCurrentPlayer().updatePlayerScore("sit", 'H', 7, 7);
        game.switchTurn();

        controller.handleUndo();
        int count = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(board.getBoard()[i][j]);
                if (board.getBoard()[i][j] != '-'){
                    count++;
                }
            }
            System.out.println();
        }

        assertEquals(game.players.get(0).score, 0);
        assertEquals(count, 0);

    }

    @Test
    void redoTest(){
        board = new Board();
        game = new Game(board);
        ScrabbleController controller = new ScrabbleController(game, board);
        board.setController(controller);
        game.getStates().add(new GameState(board, game));
        game.getCurrentPlayer().place("sit", 'H', 7, 7);
        game.getCurrentPlayer().updatePlayerScore("sit", 'H', 7, 7);
        game.switchTurn();
        controller.handleUndo();
        controller.handleRedo();
        int count = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(board.getBoard()[i][j]);
                if (board.getBoard()[i][j] != '-'){
                    count++;
                }
            }
            System.out.println();
        }

        assertEquals(game.players.get(0).score, 2);
        assertEquals(count, 3);

    }

    @Test
    void saveTest(){
        board = new Board();
        game = new Game(board);
        ScrabbleController controller = new ScrabbleController(game, board);
        board.setController(controller);
        game.getStates().add(new GameState(board, game));
        game.getCurrentPlayer().place("sit", 'H', 7, 7);
        game.getCurrentPlayer().updatePlayerScore("sit", 'H', 7, 7);
        game.switchTurn();

        assertEquals(controller.handleSave(),true);
    }

    @Test
    void loadTest(){
        board = new Board();
        game = new Game(board);
        ScrabbleController controller = new ScrabbleController(game, board);
        board.setController(controller);
        game.getStates().add(new GameState(board, game));
        game.getCurrentPlayer().place("sit", 'H', 7, 7);
        game.getCurrentPlayer().updatePlayerScore("sit", 'H', 7, 7);
        game.switchTurn();

        //Save the file as 'file' and board as 'board'
        assertEquals(controller.handleSave(),true);

        //Load 'file' and 'board'
        assertEquals(controller.handleLoad(),true);
    }

    @Test
    void alternateSpecialTilePlacement(){
        board = new Board();
        game = new Game(board);
        ScrabbleController controller = new ScrabbleController(game, board);
        board.setController(controller);
        game.getStates().add(new GameState(board, game));
        assertEquals(controller.handleLayout("clustered"),true);

    }
}