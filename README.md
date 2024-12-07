# Scrabble
This is a Scrabble game implemented in Java, using an MVC design pattern. The project allows two players - or a player and AI - to play Scrabble, where words are placed on a 15x15 board made up of buttons, tiles are drawn from a bag, and a dictionary is used to determine if the words placed down are valid.
The game keeps track of every player's score, which is calculated by adding the points of each letter in the word or words they create.The game then ends when there are no more tiles to draw from the bag and one player uses up the tiles in their collection.

## How to play
- **Starting the game:** In the current implementation of the game, the game runs with two players and one AI. When the game starts, the first player is initialized with seven tiles. Each tile will display its value.

- **First turn:** Player can choose to either play their turn or pass their turn 

- **Placing a word:** If a player chooses to play his turn. For round 1 the word they form their deck of tiles can be placed anywhere within the bounds of the board. The player will tap on the button they want the word to start from. The program will give a choice of placing either horizontally or vertically. The row and column from which the word will start must also be input.

- **Score Update:** The score will update automatically based on the letters used.

- **End the turn:** The board is displayed, the player’s number of tiles goes back to 7 by taking needed tiles from the bag  and the next player takes their turn.

- **Second turn:** From the second turn, player 2 is initialized with 7 tiles and has to create a word that connects to the word placed on the board by player 1. To do this the player taps on the button where they want their word to start from. If the connection create 2 new words, the player get points from both words.
  
- **AI player's turn:** The AI player decides what the best word is to play by checking it's tiles and the tiles that have been placed on the board then places it on the board.

- **Undo and Redo:** If you want to undo the last move you can click on undo under the "File" dropdown menu. If you want to then redo the move click on redo.
  
- **Saving and Loading:** If you want to save you game you can click on save under the "File" dropdown menu this will save your game and if you want to return to it later you can click on load.
  
- **End the game:** The game ends when no more tiles in the tile bag and one player uses up the remaining tiles they are holding
  
## Issues
### AI word placement 
- When deciding where to place sometimes the AI will place a valid word but with the words around it the word should be invalid

## Developers
Rotimi Ajayi

Subomi Akingbade

Iheanyi Njoku Obi


