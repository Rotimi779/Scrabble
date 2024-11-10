import javax.swing.*;

public class Button {
    private JButton button;
    private int row;
    private int column;

    public Button(int row, int column){
        button = new JButton();
        this.row = row;
        this.column = column;
    }

    public JButton getButton(){
        return this.button;
    }

    public int getRow(){
        return this.row;
    }

    public int getColumn(){
        return this.column;
    }
}
