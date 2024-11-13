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

    }

    @org.junit.jupiter.api.Test
    void calculateScore() {
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