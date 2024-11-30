import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Board board;

    @org.junit.jupiter.api.Test
    void isValidPlacement() {
        board = new Board();
        game = new Game(board);

        assertFalse(Game.isValidPlacement(board, "food", 'H', 14, 14));
        assertFalse(Game.isValidPlacement(board, "food", 'V', 14, 5));

        // normal/central case
        assertTrue(Game.isValidPlacement(board, "food", 'H', 7, 7));
        assertTrue(Game.isValidPlacement(board, "fake", 'V', 7, 7));

    }

    @org.junit.jupiter.api.Test
    void calculateScore() {
        board = new Board(); 
        game = new Game(board);
        assertTrue(Game.calculateScore("sit", 0, 0, 'H') == 3);
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
        int score = Game.calculatePerpendicularScore(word);

        // Expected score: B = 3, A = 1, T = 1 -> Total = 5
        assertEquals(5, score, "Score for 'bat' should be 5 points.");

        word = "quiz";
        score = Game.calculatePerpendicularScore(word);

        // Expected score: Q = 10, U = 1, I = 1, Z = 10 -> Total = 22
        assertEquals(22, score, "Score for 'quiz' should be 22 points.");
    }

    @org.junit.jupiter.api.Test
    void coloredSquareTest() {
        board = new Board();
        game = new Game(board);
        assertTrue(Game.isValidPlacement(board,"eat",'H',7,7));
        assertTrue(Game.calculateScore("eat",7,7,'H') == 6);
    }

    @org.junit.jupiter.api.Test
    void emptyTileTest() {
        Board board1 = new Board();
        Game game = new Game(board1);
        Player player = new Player(board1);
        for(int i = 0; i < 3; i ++){
            player.addTile(Game.tilebag);
        }
        player.getTiles().add(new Tiles('_',0));
        player.getTiles().add(new Tiles('A',1));
        player.getTiles().add(new Tiles('T',1));
        //Type in the letter b or c
        player.playTurn("_at",'H',7,7);
    }

}