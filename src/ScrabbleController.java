import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrabbleController{
    private Game game;
    private Board board;

    public ScrabbleController(Game game, Board board) {
        this.game = game;
        this.board = board;

    }


    public void initializeGrid(JPanel grid,JButton [][] buttons) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(40, 40));
                buttons[i][j].addActionListener(e -> handleButtonClick (row,col));
                buttons[i][j].setText("-");
                grid.add(buttons[i][j]);
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        String word = JOptionPane.showInputDialog("Enter a word:");
        if (word != null && !word.isEmpty()) {
            String direction = JOptionPane.showInputDialog("Enter direction: H (Horizontal) or V (Vertical)?");
            if (direction != null && (direction.equalsIgnoreCase("H") || direction.equalsIgnoreCase("V"))) {
                char dirChar = direction.equalsIgnoreCase("H") ? 'H' : 'V';
                if (game.getCurrentPlayer().place(word, dirChar, row, col)) {

                    //New stuff
                    //game.getCurrentPlayer().playTurn(word);

                    board.updateBoardDisplay();
                } else {
                    JOptionPane.showMessageDialog(board, "Invalid placement.");
                }
            } else {
                JOptionPane.showMessageDialog(board, "Invalid direction.");
            }
        }
    }
}
