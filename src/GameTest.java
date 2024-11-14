import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Board board;

    @org.junit.jupiter.api.Test
    void isValidPlacement() {
        board = new Board();
        game = new Game(board);

        // edge cases
        assertFalse(Game.isValidPlacement(board, "food", 'H', 14, 14));
        assertFalse(Game.isValidPlacement(board, "food", 'V', 14, 5));
        assertTrue(Game.isValidPlacement(board, "food", 'H', 14, 5));
        assertTrue(Game.isValidPlacement(board, "food", 'V', 11, 14));
        assertTrue(Game.isValidPlacement(board, "food", 'H', 0, 0));

        // normal/central case
        assertTrue(Game.isValidPlacement(board, "food", 'H', 5, 5));
        assertTrue(Game.isValidPlacement(board, "food", 'V', 0, 5));

        // overlapping words
        assertTrue(game.getCurrentPlayer().place("food", 'H', 0, 0));
        assertFalse(Game.isValidPlacement(board, "jacket", 'H', 0, 0));

    }

    @org.junit.jupiter.api.Test
    void calculateScore() {
        board = new Board();
        game = new Game(board);
        assertEquals(Game.calculateScore("sit", 0, 0, 'H'), 3);
    }

    @org.junit.jupiter.api.Test
    void calculatePerpendicularWordScore() {
    }

    @org.junit.jupiter.api.Test
    void calculatePerpendicularScore() {
    }

    @org.junit.jupiter.api.Test
    void getTileScore() {
    }
}