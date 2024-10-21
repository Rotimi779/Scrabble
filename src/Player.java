import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private int score;
    private List<Tiles> tiles;

    public Player(){
        tiles = new ArrayList<>();
        this.score = 0;
    }

    public void addTile(TileBag tileBag){
        tiles.add(tileBag.drawTile());
    }

    public int getScore(char letter){
        for (Tiles tile : tiles) {
            if (tile.getLetter() == letter) {
                return tile.getScore();
            }
        }
        return 0;
    }

    public void removeTile(char letter, int score){
        Iterator<Tiles> iterator = tiles.iterator();
        while (iterator.hasNext()) {
            Tiles tile = iterator.next();
            if ((letter == tile.getLetter()) && (tile.getScore() == score)) {
                iterator.remove();
                System.out.println("Removed: " + tile);
            }
        }
        System.out.println("No matching tile found for " + letter + "(" + score + ")");
    }

    public void displayTiles() {
        System.out.println("Player's Tiles:");
        for (Tiles tile : tiles) {
            System.out.println("Tile: " + tile.getLetter() + " Score: " + tile.getScore());
        }
    }

    public void updatePlayerScore(char letter){
        int number = getScore(letter);
        score += number;
    }

    public int getPlayerScore(){
        return this.score;
    }
}
