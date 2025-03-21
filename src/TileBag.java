import java.util.*;

public class TileBag {
    private static List<Tiles> bag;
    private static Map<Character, Integer> scoreMap;
    private Random random;

    // Constructor to initialize all tiles
    public TileBag() {
        bag = new ArrayList<>();
        scoreMap = new HashMap<>();
        random = new Random();
        initializeBag();
    }
    public void initializeBag() {
        addTiles('A', 1, 9);
        addTiles('B', 3, 2);
        addTiles('C', 3, 2);
        addTiles('D', 2, 4);
        addTiles('E', 1, 12);
        addTiles('F', 4, 2);
        addTiles('G', 2, 3);
        addTiles('H', 4, 2);
        addTiles('I', 1, 9);
        addTiles('J', 8, 1);
        addTiles('K', 5, 1);
        addTiles('L', 1, 4);
        addTiles('M', 3, 2);
        addTiles('N', 1, 6);
        addTiles('O', 1, 8);
        addTiles('P', 3, 2);
        addTiles('Q', 10, 1);
        addTiles('R', 1, 6);
        addTiles('S', 1, 4);
        addTiles('T', 1, 6);
        addTiles('U', 1, 4);
        addTiles('V', 4, 2);
        addTiles('W', 4, 2);
        addTiles('X', 8, 1);
        addTiles('Y', 4, 2);
        addTiles('Z', 10, 1);
        addTiles('_', 0, 2); // Blank tiles
    }

    private void addTiles(char letter, int score, int count) {
        for (int i = 0; i < count; i++) {
            bag.add(new Tiles(letter, score));
        }
        scoreMap.put(letter, score);
    }

    public int getScore(char letter) {
        return scoreMap.getOrDefault(letter, 0);  // Get score from map, defaulting to 0
    }

    public void addTile(char letter, int score) {
        bag.add(new Tiles(letter, score));
    }

    // Draw a random tile from the bag
    public Tiles drawTile() {
        if (bag.isEmpty()) {
            throw new NoSuchElementException("The bag is empty!");
        }
        int index = random.nextInt(bag.size());
        Tiles tile = bag.get(index);
        bag.remove(index);
        return tile;
    }
    public static List<Tiles> getBag() {
        return bag;
    }

    public static int getScoreForTile(char letter){
        return scoreMap.getOrDefault(letter, 0);  // Get score from map, defaulting to 0
    }


}
