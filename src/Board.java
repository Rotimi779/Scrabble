import javax.swing.*;
import java.awt.*;


public class Board extends JFrame {
    //attributes
    private Game game;
    private JButton [][] buttons;
    private char[][] board;

    //constructor
    public Board(Game game){
        this.game = game;
        this.buttons = new JButton[15][15];
        setTitle("Scrabble");
        setLayout(new GridLayout(15, 15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeGrid();
        setVisible(true);
    }

    private void initializeGrid() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(40, 40));
                buttons[i][j].addActionListener(e -> handleButtonClick (row,col));
                add(buttons[i][j]);
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        String word = JOptionPane.showInputDialog("Enter a word:");
        if (word != null && !word.isEmpty()) {
            String direction = JOptionPane.showInputDialog("Enter direction: H (Horizontal) or V (Vertical)?");
            if (direction != null && (direction.equalsIgnoreCase("H") || direction.equalsIgnoreCase("V"))) {
                game.getCurrentPlayer().placeWord(word);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid direction.");
            }
        }
    }

    public void updateBoard(int row, int col, char letter) {
        buttons[row][col].setText(String.valueOf(letter));
    }

    public void updateButton(int row, int col, char letter){
        board [row][col] = letter;
        buttons[row][col].setText(String.valueOf(letter));
    }

    public char[][] getBoard() {
        return board;
    }

    public void display() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.printf(board[i][j] + " ");
            }
            System.out.println();
        }
    }

}
