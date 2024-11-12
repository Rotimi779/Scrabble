import javax.swing.*;
import java.awt.*;


public class Board extends JFrame {
    //attributes
    private ScrabbleController controller;
    private JButton[][] buttons;
    private char[][] board;
    private JPanel grid;

    //constructor
    public Board(){
        this.buttons = new JButton[15][15];
        setTitle("Scrabble");
        setSize(800,600);
        setLayout(new BorderLayout());
        grid = new JPanel(new GridLayout(15,15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new char[15][15];
        for (int i = 0; i< 15;i++){
            for (int j = 0; j < 15; j++){
                board[i][j] = '-';
            }

        }
        initializeGrid(grid,buttons);
        add(grid, BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("LEN");
        panel.add(label);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JButton[][] getButtons(){
        return this.buttons;
    }

    public void setController(ScrabbleController controller){
        this.controller = controller;
    }

    public PlayEvent displayInputFields(){
        String word = JOptionPane.showInputDialog("Enter a word:");
        String direction = JOptionPane.showInputDialog("Enter direction: H (Horizontal) or V (Vertical)?");
        return new PlayEvent(this, word, direction);
    }

    public void initializeGrid(JPanel grid,JButton [][] buttons) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int row = i;
                int col = j;
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(40, 40));
                buttons[i][j].addActionListener(e -> controller.handleButtonClick(row, col));
                buttons[i][j].setText("-");
                grid.add(buttons[i][j]);
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

    public void displayInvalidPlacement(){
        JOptionPane.showMessageDialog(this, "Invalid placement.");
    }

    public void displayInvalidDirection(){
        JOptionPane.showMessageDialog(this, "Invalid direction.");
    }

    public void updateBoardDisplay() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText(String.valueOf(this.board[i][j]));
            }
        }
    }

}
