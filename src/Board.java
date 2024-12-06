import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.*;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;


public class Board extends JFrame implements Serializable {
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
    private Menu layoutMenu;
    private MenuItem normalLayout;
    private MenuItem clusteredLayout;
    private MenuItem circularLayout;



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
        layoutMenu = new Menu("Layout");
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(layoutMenu);
        quitItem = new MenuItem("Quit");
        helpItem = new MenuItem("Help");
        redoItem = new MenuItem("Redo");
        undoItem = new MenuItem("Undo");
        saveItem = new MenuItem("Save");
        loadItem = new MenuItem("Load");
        normalLayout = new MenuItem("Normal");
        clusteredLayout = new MenuItem("Clustered");
        circularLayout = new MenuItem("Circular");



        // add the menu to menu items to their respective menus
        // game menu
        gameMenu.add(redoItem);
        gameMenu.add(undoItem);

        gameMenu.add(helpItem);

        // file menu
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(quitItem);

        layoutMenu.add(normalLayout);
        layoutMenu.add(clusteredLayout);
        layoutMenu.add(circularLayout);

        // set the menu bar
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
        for (int i = 0; i< 15;i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = '-';
            }

        }
        initializeGrid(grid,buttons,"normal.xml");
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
        normalLayout.addActionListener(e -> {controller.handleLayout("normal");});
        clusteredLayout.addActionListener(e -> {controller.handleLayout("clustered");});
        circularLayout.addActionListener(e -> {controller.handleLayout("circular");});

    }

    public void setBoard(String[] board){
        for(int i = 0; i < 15; i++){
            this.board[i] = board[i].toCharArray();
        }
        updateBoardDisplay();
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

    public void initializeGrid(JPanel grid,JButton [][] buttons,String xmlFile) {
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
        File xml = new File(xmlFile); // Path to your XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc = null;
        try {
            doc = builder.parse(xml);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        NodeList buttonNodes = doc.getElementsByTagName("Button");
        switch(xmlFile){
            case("normal.xml"):
                // Process the buttons based on XML data
                for (int i = 0; i < buttonNodes.getLength(); i++) {
                    Element buttonElement = (Element) buttonNodes.item(i);

                    int row = Integer.parseInt(buttonElement.getAttribute("row"));
                    int col = Integer.parseInt(buttonElement.getAttribute("col"));

                    JButton button = buttons[row][col];
                    updateButtonColors(buttons, row, col,"normal.xml");
                }
                break;
            case("clustered.xml"):
                // Process the buttons based on XML data
                for (int i = 0; i < buttonNodes.getLength(); i++) {
                    Element buttonElement = (Element) buttonNodes.item(i);

                    int row = Integer.parseInt(buttonElement.getAttribute("row"));
                    int col = Integer.parseInt(buttonElement.getAttribute("col"));

                    JButton button = buttons[row][col];
                    updateButtonColors(buttons, row, col,"clustered.xml");
                }
                break;
            case("circular.xml"):
                // Process the buttons based on XML data
                for (int i = 0; i < buttonNodes.getLength(); i++) {
                    Element buttonElement = (Element) buttonNodes.item(i);

                    int row = Integer.parseInt(buttonElement.getAttribute("row"));
                    int col = Integer.parseInt(buttonElement.getAttribute("col"));

                    JButton button = buttons[row][col];
                    updateButtonColors(buttons, row, col,"circular.xml");
                }
                break;
        }

//        add(grid, BorderLayout.CENTER);
        //updateButtonColors(buttons);

    }

    public void setLayout(JButton [][]buttons, String xmlFile){
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int row = i;
                int col = j;
                buttons[i][j].setBackground(Color.lightGray);
            }
        }
        File xml = new File(xmlFile); // Path to your XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc = null;
        try {
            doc = builder.parse(xml);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        NodeList buttonNodes = doc.getElementsByTagName("Button");
        switch(xmlFile){
            case("normal.xml"):
                // Process the buttons based on XML data
                for (int i = 0; i < buttonNodes.getLength(); i++) {
                    Element buttonElement = (Element) buttonNodes.item(i);

                    int row = Integer.parseInt(buttonElement.getAttribute("row"));
                    int col = Integer.parseInt(buttonElement.getAttribute("col"));

                    JButton button = buttons[row][col];
                    updateButtonColors(buttons, row, col,"normal.xml");
                }
                break;
            case("clustered.xml"):
                // Process the buttons based on XML data
                for (int i = 0; i < buttonNodes.getLength(); i++) {
                    Element buttonElement = (Element) buttonNodes.item(i);

                    int row = Integer.parseInt(buttonElement.getAttribute("row"));
                    int col = Integer.parseInt(buttonElement.getAttribute("col"));

                    JButton button = buttons[row][col];
                    updateButtonColors(buttons, row, col,"clustered.xml");
                }
                break;
            case("circular.xml"):
                // Process the buttons based on XML data
                for (int i = 0; i < buttonNodes.getLength(); i++) {
                    Element buttonElement = (Element) buttonNodes.item(i);

                    int row = Integer.parseInt(buttonElement.getAttribute("row"));
                    int col = Integer.parseInt(buttonElement.getAttribute("col"));

                    JButton button = buttons[row][col];
                    updateButtonColors(buttons, row, col,"circular.xml");
                }
                break;
        }
        updateBoardDisplay();
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

    public boolean displayDisplayQuitConfirmation(){ // we could change this to an option one instead
        String answer = JOptionPane.showInputDialog(this, "Are you sure you want to quit?");
        return answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y");
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

    public void updateScoreLabel(int score, int turn) {
        scoreLabel.setText("Player " + turn + "'s turn. " + "Player " + turn  +"'s Score: " + score);
        scoreLabel.revalidate();
        scoreLabel.repaint();
    }

    //For setting background colours
    private static void updateButtonColors(JButton[][] buttons,int row, int col, String xmlFile) {
        if (isTripleWord(row, col,xmlFile)) {
            buttons[row][col].setBackground(Color.red);
            buttons[row][col].setText("Triple Word Score");
            buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 8));
            buttons[row][col].setMargin(new Insets(10,10,10,10));
        } else if (isDoubleWord(row, col,xmlFile)) {
            buttons[row][col].setBackground(Color.pink);
            buttons[row][col].setText("Double Word Score");
            buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 8));
            buttons[row][col].setMargin(new Insets(10,10,10,10));
        } else if (isTripleLetter(row, col,xmlFile)) {
            buttons[row][col].setBackground(Color.blue);
            buttons[row][col].setText("Triple Letter Score");
            buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 8));
            buttons[row][col].setMargin(new Insets(10,10,10,10));
        } else if (isDoubleLetter(row, col,xmlFile)) {
            buttons[row][col].setBackground(Color.cyan);
            buttons[row][col].setText("Double Letter Score");
            buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 8));
            buttons[row][col].setMargin(new Insets(10,10,10,10));
        }
    }

        private static boolean isTripleWord(int row, int col,String xmlFile) {
        switch(xmlFile){
            case("normal.xml"):
                return (row == col && (row == 0 || row == 14)) || // Diagonal corners
                        (row == 0 || row == 14) && (col == 0 || col == 14) || // Outer corners
                        (row == 7 && (col == 0 || col == 14)) || // Middle row edges
                        (col == 7 && (row == 0 || row == 14)); // Middle column edges
            case("clustered.xml"):
                // Triple Word Score positions (6 tiles clustered near the center)
                return (row == 0 && col == 0) ||
                        (row == 0 && col == 14) ||
                        (row == 14 && col == 0) ||
                        (row == 14 && col == 14) ||
                        (row == 7 && col == 0) ||
                        (row == 7 && col == 7) ||
                        (row == 7 && col == 14);
            case("circular.xml"):
                // Triple Word squares at specific coordinates
                return (row == 6 && col == 6) ||
                        (row == 6 && col == 8) ||
                        (row == 8 && col == 8) ||
                        (row == 8 && col == 6) ||
                        (row == 4 && col == 4) ||
                        (row == 10 && col == 4) ||
                        (row == 4 && col == 10) ||
                        (row == 10 && col == 10);
        }
        return true;
    }

    private static boolean isDoubleWord(int row, int col,String xmlFile) {
        switch(xmlFile){
            case("normal.xml"):
                // Double Word squares
                return (row == col && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13 || row == 7)) ||
                        (row + col == 14 && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13 || row == 7));
            case("clustered.xml"):
                // Double Word squares placement based on the "Clustered High-Impact Layout"
                return (row == 3 && col == 7) || (row == 11 && col == 7) ||
                        (row == 7 && col == 3) || (row == 7 && col == 11) ||
                        (row == 11 && col == 3) || (row == 3 && col == 11) ||
                        (row == 6 && col == 6) || (row == 6 && col == 8) || (row == 8 && col == 6) || (row == 8 && col == 8) ||
                        (row == 3 && col == 3) || (row == 11 && col == 11);
            case("circular.xml"):
                // Double Word squares at the specified positions
                return (row == 3 && col == 3) ||
                        (row == 11 && col == 3) ||
                        (row == 3 && col == 11) ||
                        (row == 11 && col == 11) ||
                        (row == 5 && col == 5) ||
                        (row == 5 && col == 9) ||
                        (row == 9 && col == 5) ||
                        (row == 9 && col == 9) ||
                        (row == 5 && col == 7) ||
                        (row == 9 && col == 7) ||
                        (row == 0 && col == 7) ||
                        (row == 14 && col == 7);
        }
        return true;
    }

    private static boolean isTripleLetter(int row, int col,String xmlFile) {
        switch(xmlFile){
            case("normal.xml"):
                // Triple Letter squares
                return (row == 1 && (col == 5 || col == 9)) ||
                        (row == 5 && (col == 1 || col == 5 || col == 9 || col == 13)) ||
                        (row == 9 && (col == 1 || col == 5 || col == 9 || col == 13)) ||
                        (row == 13 && (col == 5 || col == 9));
            case("clustered.xml"):
                // Triple Letter Score squares (specified positions)
                return (row == 2 && col == 2) ||
                        (row == 6 && col == 2) ||
                        (row == 8 && col == 2) ||
                        (row == 12 && col == 2) ||
                        (row == 5 && col == 5) ||
                        (row == 9 && col == 5) ||
                        (row == 5 && col == 9) ||
                        (row == 9 && col == 9) ||
                        (row == 2 && col == 12) ||
                        (row == 6 && col == 12) ||
                        (row == 8 && col == 12) ||
                        (row == 12 && col == 12);
            case("circular.xml"):
                // Triple Letter squares based on the provided coordinates
                return (row == 2 && col == 2) ||
                        (row == 12 && col == 2) ||
                        (row == 2 && col == 12) ||
                        (row == 12 && col == 12) ||
                        (row == 2 && col == 7) ||
                        (row == 12 && col == 7) ||
                        (row == 11 && col == 5) ||
                        (row == 11 && col == 9) ||
                        (row == 3 && col == 5) ||
                        (row == 3 && col == 9) ||
                        (row == 7 && col == 5) ||
                        (row == 7 && col == 9);
        }
        return true;
    }

    private static boolean isDoubleLetter(int row, int col,String xmlFile) {
        switch(xmlFile){
            case("normal.xml"):
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
            case("clustered.xml"):
                // Double Letter Score squares (on the outer edges)
                return (row == 0 && (col == 1 || col == 3 || col == 5 || col == 9 || col == 11 || col == 13)) || // Top edge
                        (row == 14 && (col == 1 || col == 3 || col == 5 || col == 9 || col == 11 || col == 13)) || // Bottom edge
                        (col == 0 && (row == 1 || row == 3 || row == 5 || row == 9 || row == 11 || row == 13)) || // Left edge
                        (col == 7 && (row == 5 || row == 9 || row == 1 || row == 13)) ||
                        (col == 14 && (row == 1 || row == 3 || row == 5 || row == 9 || row == 11 || row == 13));   // Right edge
            case("circular.xml"):
                // Double Letter squares at the given positions
                return (row == 0 && col == 0) || (row == 0 && col == 14) ||
                        (row == 14 && col == 0) || (row == 14 && col == 14) ||
                        (row == 1 && col == 1) || (row == 1 && col == 13) ||
                        (row == 13 && col == 1) || (row == 13 && col == 13) ||
                        (row == 1 && col == 6) || (row == 1 && col == 8) ||
                        (row == 13 && col == 6) || (row == 13 && col == 8) ||
                        (row == 14 && col == 5) || (row == 14 && col == 9) ||
                        (row == 0 && col == 5) || (row == 0 && col == 9) ||
                        (row == 7 && col == 0) || (row == 7 && col == 14) ||
                        (row == 7 && col == 7);
        }
        return true;

    }

    public JButton[][] getButtons(){ return this.buttons; }

    public JPanel getGrid(){ return this.grid;}
}
