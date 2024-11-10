import javax.swing.*;
import java.awt.*;


public class Board extends JFrame {
    //attributes
    private Game game;
    private ScrabbleController controller;
    private JButton [][] buttons;
    private char[][] board;
    private JPanel grid;

    //constructor
    public Board(Game game){
        this.game = game;
        controller = new ScrabbleController(game,this);
        this.buttons = new JButton[15][15];
        setTitle("Scrabble");
        setSize(400,400);
        setLayout(new BorderLayout());
        grid = new JPanel(new GridLayout(15,15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new char[15][15];
        for (int i = 0; i< 15;i++){
            for (int j = 0; j < 15; j++){
                board[i][j] = '-';
            }

        }
        controller.initializeGrid(grid,buttons);
        add(grid, BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout());
        JTextField field = new JTextField(5);
        panel.add(field);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
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

    public void updateBoardDisplay() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText(String.valueOf(Game.board.getBoard()[i][j]));
            }
        }
    }

}
