public class GameState {
    private Board board;
    private Game game;

    public GameState(Board board, Game game) {
        this.board = board;
        this.game = game;
    }
    public Board getBoard() {
        return board;
    }
    public Game getGame() {
        return game;
    }
}
