public class Tiles {
    private char letter;
    private int score;

    public Tiles(char letter, int score){
        this.letter = letter;
        this.score = score;
    }

    public char getLetter(){
        return this.letter;
    }

    public int getScore(){
        return this.score;
    }
}
