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
public class Board extends Observable
{

    private Case[][] board;
    private int row;
    private int col;
    private final int nbBomb; //Number of bomb
    private boolean lost;
    private boolean win;
    private int nbFlag;
    
    public Case[][] getBoard()
    {
        return board;
    }

    public void setBoard(Case[][] board)
    {
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
    public Board(int row, int col, int bomb)
    {
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

    public void addNeignbours()
    {
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
        if (!this.getCase(row, col).isVisible()) {
            this.getCase(row, col).setFlag();
            this.nbFlag ++;
            if (this.nbAllUnDiscovered() == this.nbBomb)
            {
                    this.setWin(true);
            }
            
            this.update();
        }
    }

    public void leftClick(int row, int col)
    {
        Case c = this.getCase(row, col);
        if (!c.isVisible()) {
            if (c.isTrap()) 
            {
                c.discover();
                this.setLost(true);
                discoverAll();
                c.setLost(true);
            } else {
                if (c.computeNbBomb() > 0) {
                    c.discover();
                } else {
                    c.discover();
                    c.discoverNeighbours();
                }
                if (this.nbAllUnDiscovered() == this.nbBomb)
                {
                        this.setWin(true);
                }
            }
        }
        this.update();
    }
    
    public void discoverAll()
    {
        for (int row = 0; row < this.getRow(); row++) {
            for (int col = 0; col < this.getCol(); col++) {
                this.getCase(row, col).discover();
            }
        }
    }
    
    
    public int nbAllUnDiscovered()
    {
        int counter = 0;
        for (int row = 0; row < this.getRow(); row++) {
            for (int col = 0; col < this.getCol(); col++) {
                if(this.getCase(row, col).isFlag())
                {
                    counter ++;
                }
            }
        }
        return counter;
    }

    /**
     * Method to generate randomly a list of bombs and put it on the grid
     */
    public void generateBomb()
    {
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
    public Case getCase(int i, int j)
    {
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
