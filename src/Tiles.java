import java.io.Serializable;

public class Tiles implements Serializable {
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

    public void setLetter(char letter){ this.letter = letter;}

    public void setScore(char letter){ this.score = TileBag.getScoreForTile(letter);}
}
