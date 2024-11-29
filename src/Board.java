import javax.swing.*;
import java.awt.*;


public class Board extends JFrame {
    //attributes
    private ScrabbleController controller;
    private JButton[][] buttons;
    private char[][] board;
    private JPanel grid;
    private JLabel tiles;
    private JLabel roundLabel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JLabel scoreLabel;
    private Menu fileMenu;
    private Menu gameMenu;
    private MenuBar menuBar;
    private MenuItem quitItem;
    private MenuItem helpItem;
    private MenuItem redoItem;
    private MenuItem undoItem;
    private MenuItem saveItem;
    private MenuItem loadItem;


    //constructor
    public Board(){
        this.buttons = new JButton[15][15];
        setTitle("Scrabble");
        setSize(800,600);
        setLayout(new BorderLayout());

        // initialize the menus
        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        gameMenu = new Menu("Game");
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        quitItem = new MenuItem("Quit");
        helpItem = new MenuItem("Help");
        redoItem = new MenuItem("Redo");
        undoItem = new MenuItem("Undo");
        saveItem = new MenuItem("Save");
        loadItem = new MenuItem("Load");

        // add the menu to menu items to their respective menus
        // game menu
        gameMenu.add(redoItem);
        gameMenu.add(undoItem);
        gameMenu.add(quitItem);
        gameMenu.add(helpItem);

        // file menu
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        this.setMenuBar(menuBar);





        grid = new JPanel(new GridLayout(15,15));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        topPanel = new JPanel(new GridLayout(2,1));
        roundLabel = new JLabel();
        topPanel.add(roundLabel);
        scoreLabel = new JLabel("Score: ");
        topPanel.add(scoreLabel);

        add(topPanel, BorderLayout.NORTH);


        board = new char[15][15];
        for (int i = 0; i< 15;i++){
            for (int j = 0; j < 15; j++){
                board[i][j] = '-';
            }

        }
        initializeGrid(grid,buttons);
        add(grid, BorderLayout.CENTER);

        bottomPanel = new JPanel(new FlowLayout());
        tiles = new JLabel();
        bottomPanel.add(tiles);
        add(bottomPanel, BorderLayout.SOUTH);
        setMenuFunctions();


        setVisible(true);
    }

    private void setMenuFunctions(){
        redoItem.addActionListener(e -> {controller.handleRedo();});
        undoItem.addActionListener(e -> {controller.handleUndo();});
        saveItem.addActionListener(e -> {controller.handleSave();});
        loadItem.addActionListener(e -> {controller.handleLoad();});
        quitItem.addActionListener(e -> {controller.handleExit();});
        helpItem.addActionListener(e -> {controller.handleHelp();});
    }

    public void displayRound(int round){
        roundLabel.setText("Round " + Integer.toString(round));
    }

    public void displayCurrentPlayerTiles(String playerTiles){
        tiles.setText(playerTiles);
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
                buttons[i][j] = new JButton("");
                buttons[i][j].setPreferredSize(new Dimension(40, 40));
                buttons[i][j].addActionListener(e -> controller.handleButtonClick(row, col));
                buttons[i][j].setBackground(Color.lightGray);
                grid.add(buttons[i][j]);
            }
        }
        updateButtonColors(buttons);

    }

    public char[][] getBoard() {
        return board;
    }

    public void displayInvalidPlacement(){
        JOptionPane.showMessageDialog(this, "Invalid placement.");
    }

    public void displayInvalidWord(){
        JOptionPane.showMessageDialog(this, "Invalid word.");
    }

    public void displayInvalidDirection(){
        JOptionPane.showMessageDialog(this, "Invalid direction.");
    }

    public void updateBoardDisplay() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText(String.valueOf(this.board[i][j]));
                if (buttons[i][j].getText().equals("-")){
                    if(buttons[i][j].getBackground().equals(Color.red)){
                        buttons[i][j].setText("Triple Word Score");
                    }
                    else if(buttons[i][j].getBackground().equals(Color.blue)){
                        buttons[i][j].setText("Triple Letter Score");
                    }
                    else if (buttons[i][j].getBackground().equals(Color.cyan)){
                        buttons[i][j].setText("Double Letter Score");
                    }
                    else if (buttons[i][j].getBackground().equals(Color.pink)){
                        buttons[i][j].setText("Double Word Score");
                    }
                    else{
                        buttons[i][j].setText("-");
                    }
                }
            }
        }
    }

    public void updateScoreLabel(int score) {
        scoreLabel.setText("Player " + Game.turn + "'s turn. " + "Player " + Game.turn  +"'s Score: " + score);
        scoreLabel.revalidate();
        scoreLabel.repaint();
    }

    //For setting background colours
    private static void updateButtonColors(JButton[][] buttons) {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (isTripleWord(row, col)) {
                    buttons[row][col].setBackground(Color.red);
                    buttons[row][col].setText("Triple Word Score");
                    buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 8));
                    buttons[row][col].setMargin(new Insets(10,10,10,10));
                } else if (isDoubleWord(row, col)) {
                    buttons[row][col].setBackground(Color.pink);
                    buttons[row][col].setText("Double Word Score");
                    buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 8));
                    buttons[row][col].setMargin(new Insets(10,10,10,10));
                } else if (isTripleLetter(row, col)) {
                    buttons[row][col].setBackground(Color.blue);
                    buttons[row][col].setText("Triple Letter Score");
                    buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 8));
                    buttons[row][col].setMargin(new Insets(10,10,10,10));
                } else if (isDoubleLetter(row, col)) {
                    buttons[row][col].setBackground(Color.cyan);
                    buttons[row][col].setText("Double Letter Score");
                    buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 8));
                    buttons[row][col].setMargin(new Insets(10,10,10,10));
                }
            }
        }
    }

        private static boolean isTripleWord(int row, int col) {
        return (row == col && (row == 0 || row == 14)) || // Diagonal corners
                (row == 0 || row == 14) && (col == 0 || col == 14) || // Outer corners
                (row == 7 && (col == 0 || col == 14)) || // Middle row edges
                (col == 7 && (row == 0 || row == 14)); // Middle column edges
    }

    private static boolean isDoubleWord(int row, int col) {
        // Double Word squares
        return (row == col && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13 || row == 7)) ||
                (row + col == 14 && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13 || row == 7));
    }

    private static boolean isTripleLetter(int row, int col) {
        // Triple Letter squares
        return (row == 1 && (col == 5 || col == 9)) ||
                (row == 5 && (col == 1 || col == 5 || col == 9 || col == 13)) ||
                (row == 9 && (col == 1 || col == 5 || col == 9 || col == 13)) ||
                (row == 13 && (col == 5 || col == 9));
    }

    private static boolean isDoubleLetter(int row, int col) {
        // Double Letter squares
        return (row == 3 && col == 0) || (row == 11 && col == 0) ||
                (row == 6 && col == 2) || (row == 8 && col == 2) ||
                (row == 0 && col == 3) || (row == 7 && col == 3) || (row == 14 && col == 3) ||
                (row == 2 && col == 6) || (row == 6 && col == 6) || (row == 8 && col == 6) ||
                (row == 12 && col == 6) || (row == 3 && col == 7) || (row == 11 && col == 7) ||
                (row == 2 && col == 8) || (row == 6 && col == 8) || (row == 8 && col == 8) ||
                (row == 12 && col == 8) || (row == 0 && col == 11) || (row == 7 && col == 11) ||
                (row == 14 && col == 11) || (row == 6 && col == 12) || (row == 8 && col == 12) ||
                (row == 3 && col == 14) || (row == 11 && col == 14);
    }

    public JButton[][] getButtons(){ return this.buttons; }

}
