/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package Model;

import java.util.Observable;
import java.util.Random;

/**
 * Class Board representing the model side of the game
 */
public final class Board extends Observable {

    private Case[][] board;
    private int row;
    private int col;
    private final int nbBomb; //Number of bomb

    public Case[][] getBoard() {
        return board;
    }

    public void setBoard(Case[][] board) {
        this.board = board;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int lig) {
        this.row = lig;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Constructor
     *
     * @param row Number of rows in the playing grid
     * @param col Number of columns in the playing grid
     * @param bomb Number of bomb to be generated on the grid
     */
    public Board(int row, int col, int bomb) {
        this.board = new Case[row][col];
        this.setCol(col);
        this.setRow(row);
        this.nbBomb = bomb;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = new Case();
            }
        }
        generateBomb();
    }

    /**
     * Set a flag on the grid according to the coordinates entered in the
     * arguments
     *
     * @param row
     * @param col
     */
    public void rightClick(int row, int col) {
        if (!this.getCase(row, col).isVisible()) {
            this.getCase(row, col).setFlag();
            this.update();
        }
    }

    /**
     * Process the left click on a case : Game lost if a bomb is under this case
     * Game won if it is the last case undiscovered & not trapped Propagation on
     * the neighbours
     *
     * @param row
     * @param col
     */
    public void leftClick(int row, int col) {
        this.getCase(row, col).setVisible(true);
        this.update();
    }

    /**
     * Fill the grids with @nbBomb bombs
     */
    public void generateBomb() {
        Random r = new Random();

        int i_random;
        int j_random;
        for (int i = 0; i < nbBomb; i++) {
            do {
                i_random = r.nextInt(this.getRow());
                j_random = r.nextInt(this.getCol());
            } while (board[i_random][j_random].isTrap());
            System.out.println(i_random + "   " + j_random);
            board[i_random][j_random].setTrap(true);
        }
    }

    /**
     * Get the case specified
     *
     * @param i
     * @param j
     * @return The case according to the coordinates
     */
    public Case getCase(int i, int j) {
        return this.board[i][j];
    }

    /**
     * Ask the view to update the GUI content
     */
    public void update() {
        // Notify the view to update
        setChanged();
        notifyObservers();
    }

}
