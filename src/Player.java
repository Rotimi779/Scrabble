import java.util.*;


public class Player {
    private int score;
    private List<Tiles> tiles;
    private static Scanner input;

    public Player() {
        tiles = new ArrayList<>();
        this.score = 0;
        input = new Scanner(System.in);

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

    public static void close() {
        input.close();
    }

    public void displayTiles() {
        System.out.println("Player's Tiles:");
        for (Tiles tile : tiles) {
            System.out.println("Tile: " + tile.getLetter() + " Score: " + tile.getScore());
        }
    }

    public void updatePlayerScore(String word) {
        Iterator<Tiles> iterator = tiles.iterator();
        for (char i : word.toCharArray()) {
            while (iterator.hasNext()) {
                Tiles tile = iterator.next();
                if (Character.toLowerCase(tile.getLetter()) == i) {
                    score += tile.getScore();
                    iterator.remove();  // Use iterator to remove the tile
                    System.out.println("New Score: " + this.score);
                    break;
                }
            }
        }
    }

    public void playTurn(){
        System.out.println("Round " + Game.round);
        System.out.println("Player " + Game.turn  +"'s turn");
        this.displayTiles();
        System.out.print("Type a word or enter P to pass turn: ");
        String userInput = input.nextLine().trim().toLowerCase();
        while((!Game.wordDictionary.containsWord(userInput)) ||  (!this.canFormWordFromTiles(userInput))){
            if (userInput.equals("p")){
                break;
            }else if(!this.canFormWordFromTiles(userInput)){
                System.out.println("The word cannot be formed from the available tiles");
                System.out.println("Type a new word or enter P to pass turn: ");
                userInput = input.nextLine().trim().toLowerCase();
            }
            else if(!Game.wordDictionary.containsWord(userInput)){
                System.out.println("The word is not in the dictionary");
                System.out.println("Type a new word or enter P to pass turn: ");
                userInput = input.nextLine().trim().toLowerCase();
            }
        }
        if (!userInput.equals("p")){
            placeWord(userInput);
        }

        this.updatePlayerScore(userInput);
        this.displayTiles();
        Game.board.display();

        if (!userInput.equals("p")){
            pickTile();
        }
    }

    public void placeWord(String word) {
        boolean placed = false;
        while(!placed){
            System.out.println("Where do you want to place the word? ");
            System.out.println("Row: ");
            int row = input.nextInt();
            input.nextLine();
            System.out.println("Column: ");
            int column = input.nextInt();
            input.nextLine();
            System.out.println("H (Horizontal) or V (Vertical)? ");
            String direction = input.nextLine().trim().toLowerCase();

            if (direction.equals("horizontal") || direction.equals("h")){
                placed = Game.board.place(word,'H',row,column);

            }else if(direction.equals("vertical") || direction.equals("v")){
                placed = Game.board.place(word,'V',row,column);
            }else{
                System.out.println("Invalid direction");
            }
        }
    }

    public void pickTile() {
        //For picking a tile
        System.out.print("Do you want to pick a tile? (yes/no): ");
        String tileInput = input.nextLine().trim().toLowerCase();
        while(true){
            if (tileInput.equals("yes") || tileInput.equals("y") && this.getNumberOfTiles() < 7) {
                System.out.println("Pick a tile");
                boolean validPick1 = false;
                while(!(validPick1)){
                    this.addTile(Game.tilebag);
                    validPick1 = true;
                }
                break;
            } else if (tileInput.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please respond with 'yes' or 'no'.");
            }

        }
    }

    public int getNumberOfTiles() {
        return tiles.size();
    }

    public int getPlayerScore(){
        return this.score;
    }

    public  boolean canFormWordFromTiles(String word) {
        // Step 1: Count the available tiles
        Map<Character, Integer> tileCount = new HashMap<>();
        for (Tiles tile : tiles) {
            char character = Character.toLowerCase(tile.getLetter());
            tileCount.put(character, tileCount.getOrDefault(character, 0) + 1);
        }



        // Step 2: Check each letter in the word
        for (char letter : word.toCharArray()) {
            if (tileCount.containsKey(letter) && tileCount.get(letter) > 0) {
                // Decrease count for the letter
                tileCount.put(letter, tileCount.get(letter) - 1);
            } else {
                // Letter not available or used up
                return false;
            }
        }

        // Step 3: The word can be formed
        return true;
    }
}
