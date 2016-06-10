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
public class Board extends Observable {

    private Case[][] board;
    private int row;
    private int col;
    private final int nbBomb; //Number of bomb
    private boolean lost;
    private boolean win;
    private int nbFlag;

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

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getNbFlag() {
        return nbFlag;
    }

    public void setNbFlag(int nbFlag) {
        this.nbFlag = nbFlag;
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
        this.nbFlag = 0;
        this.setLost(false);
        this.setWin(false);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = new Case();
            }
        }
        generateBomb();
        addNeignbours();
    }
    
    public void resetBoard()
    {
       this.board = new Case[row][col];
        this.nbFlag = 0;
        this.setLost(false);
        this.setWin(false);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = new Case();
            }
        }
        generateBomb();
        addNeignbours();
        this.update();
    }

    public void addNeignbours() {
        for (int row = 0; row < this.getRow(); row++) {
            for (int col = 0; col < this.getCol(); col++) {
                if (row < this.getRow() - 1) {
                    board[row][col].addNeighbour(board[row + 1][col]);
                    if (col < this.getCol() - 1) {
                        board[row][col].addNeighbour(board[row + 1][col + 1]);
                    }
                    if (col > 0) {
                        board[row][col].addNeighbour(board[row + 1][col - 1]);
                    }
                }
                if (row > 0) {
                    board[row][col].addNeighbour(board[row - 1][col]);
                    if (col < this.getCol() - 1) {
                        board[row][col].addNeighbour(board[row - 1][col + 1]);
                    }
                    if (col > 0) {
                        board[row][col].addNeighbour(board[row - 1][col - 1]);
                    }
                }
                if (col < this.getCol() - 1) {
                    board[row][col].addNeighbour(board[row][col + 1]);
                }
                if (col > 0) {
                    board[row][col].addNeighbour(board[row][col - 1]);
                }
            }
        }
    }

    /**
     * Set a flag on the grid according to the coordinates entered in the
     * arguments
     *
     * @param row
     * @param col
     */
    public void rightClick(int row, int col) {

        if (gameFinished()) {
            return; // Do Nothing
        }
        Case c = this.getCase(row, col);

        switch (c.getState()) {
            case FLAGGED:
                // Remove flag
                // TODO Question Mark
                c.setFlag(false);
                this.nbFlag--;
                break;
            case UNDISCOVERED:
                c.setFlag(true);
                this.nbFlag++;
                break;
            case TRAPPED:
                c.setFlag(true);
                this.nbFlag++;
                break;
            default:
                return;
        }
        if (gameWon()) {
            this.setWin(true);
            this.manageWin();
        }
        System.out.println(nbFlag);
        this.update();
    }

    public void leftClick(int row, int col) {

        Case c = this.getCase(row, col);

        switch (c.getState()) {
            case DISCOVERED:
                return; // Do Nothing
            case EMPTY:
                return; // Do Nothing
            case TRAPPED:
                setLost(true);
                c.setState(CaseState.TRIGGERED);
                this.manageDefeat();
                break;
            case UNDISCOVERED:
                c.discover();
                if (!(c.computeNbBomb() > 0)) {
                    c.discoverNeighbours();
                }
                if (this.gameWon()) {
                    this.setWin(true);
                    manageWin();
                }
                break;
            default:
                break;
        }
        
        
        
        this.update();
    }


    /**
     * Game won if all bombs are flagged or if every case undiscovered remaining
     * are bombs
     *
     * @return true if the game is won, false if not yet
     */
    private boolean gameWon() {
        int nbUndiscovered = this.nbAllUndiscovered();
        return (nbUndiscovered == this.nbBomb
                || (nbBombsFlagged() == this.nbBomb && this.nbFlag == this.nbBomb)); // Maybe remove this
    }

    /**
     * Return the number of bombs flagged
     *
     * @return the number of bombs flagged
     */
    private int nbBombsFlagged() {
        int nb = 0;
        for (int row = 0; row < this.getRow(); row++) {
            for (int col = 0; col < this.getCol(); col++) {
                Case c = this.getCase(row, col);
                if (c.isTrap() && c.isFlag()) {
                    nb++;
                }
            }
        }
        return nb;
    }

    public boolean gameFinished() {
        return (this.isWin() || this.isLost());
    }

    private void manageDefeat() {
        if (!this.isLost() || this.isWin()) {
            return;
        }
        discoverAll();
    }

    private void manageWin() {
        if (!this.isWin() || this.isLost()) {
            return;
        }
        this.discoverAll();
    }

    public void discoverAll() {
        for (int row = 0; row < this.getRow(); row++) {
            for (int col = 0; col < this.getCol(); col++) {
                this.getCase(row, col).discover();
            }
        }
    }

    public int nbAllUndiscovered() {
        int counter = 0;
        for (int row = 0; row < this.getRow(); row++) {
            for (int col = 0; col < this.getCol(); col++) {
                if (!this.getCase(row, col).isVisible()) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Method to generate randomly a list of bombs and put it on the grid
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
