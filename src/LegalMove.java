public class LegalMove {
    private int row;
    private int col;
    private char direction;
    private String word;
    public LegalMove(int row, int col, char direction, String word) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.word = word;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public char getDirection() {
        return direction;
    }
    public String getWord() {
        return word;
    }
}
