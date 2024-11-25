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
        assertEquals(Game.calculateScore("sit", 0, 0, 'H'), 3);
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
    }

    @org.junit.jupiter.api.Test
    void coloredSquareTest() {
        board = new Board();
        game = new Game(board);
        assertTrue(Game.isValidPlacement(board,"eat",'H',7,7));
        assertTrue(Game.calculateScore("eat",7,7,'H') == 6);
    }

//    @org.junit.jupiter.api.Test
//    void emptyTileTest() {
//        Board board1 = new Board();
//        Game game = new Game(board1);
//        Player player = new Player(board1);
//        for(int i = 0; i < 3; i ++){
//            player.addTile(Game.tilebag);
//        }
//        player.getTiles().add(new Tiles('_',0));
//        player.getTiles().add(new Tiles('E',1));
//        player.getTiles().add(new Tiles('T',1));
//        player.playTurn("ate",'H',7,7);
//    }

}