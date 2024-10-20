public class Tiles {
    private int letter;
    private int score;

    public Tiles(int letter, int score){
        this.letter = letter;
        this.score = score;
    }

    public int getLetter(){
        return this.letter;
    }

    public int getScore(){
        return this.score;
    }
}
