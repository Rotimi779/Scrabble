# Scrabble
This is a Scrabble game implemented in Java, using an MVC design pattern. The project allows two players to play Scrabble, where words are placed on a 15x15 board, tiles are drawn from a bag, and a dictionary is used to determine if the words placed down are valid.
The game keeps track of both players' score, which is calculated by adding the points of each letter in the word or words they create.The game then ends when there are no more tiles to draw from the bag and one player uses up the tiles in their collection.

## How to play
- **Starting the game:** When the game starts, the first player is initialized with seven tiles. Each tile will display its value.

- **First turn:** Player can choose to either play their turn or pass their turn 

- **Placing a word:** If a player chooses to play his turn. For round 1 the word they form their deck of tiles can be placed anywhere within the bounds of the board. The program will give a choice of placing either horizontally or vertically. The row and column from which the word will start must also be input.

- **Score Update:** The score will update automatically based on the letters used.

- **End the turn:** The board is displayed, the player’s number of tiles goes back to 7 by taking needed tiles from the bag  and the next player takes their turn.

- **Second turn:** From the second turn, player 2 is initialized with 7 tiles and has to create a word that connects to the word placed on the board by player 1. If the connection create 2 new words, the player get points from both words

- **End the game:** The game ends when no more tiles in the tile bag and one player uses up the remaining tiles they are holding

## Issues
### Intersecting tiles implementation 
At the moment to check if a placement is valid the program:
- Doesnt allow word placements that go off the board
- Checks each letter in the players word for any adjacent tiles, ensuring the word connects to a previous word past the first turn
- Checks to see if the character in a certain position in the player's word is the same as the word that is placed on the board

The third check is supposed to allow interseciton of words however a player will not allow the player to play that word because they do not have the letter that they want to overlap

## Future implementation
### Adding of points when 2 words are created
When placing a word that creates 2 new words, the points from both words should be added to the player score
### Allowing for more than 2 players
### Allowing players to quit the game
### GUI based version of the game
### Multiple level undo/redo
### Save and load features
### Custom boards with alternate placement of premium squares
## Developers
Rotimi Ajayi

Subomi Akingbade

Iheanyi Njoku Obi


