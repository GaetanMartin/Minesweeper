/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package Model;

import static Model.CaseState.DISCOVERED;
import static Model.CaseState.EMPTY;
import static Model.CaseState.FLAGGED;
import static Model.CaseState.TRAPPED;
import static Model.CaseState.UNDISCOVERED;
import java.util.List;

/**
 * Class Board2D representing the model side of the game
 */
public final class Board2D extends Board {
    
    /**
     * Constructor
     *
     * @param row Number of rows in the playing grid
     * @param col Number of columns in the playing grid
     * @param bomb Number of bomb to be generated on the grid
     */
    public Board2D(int row, int col, int bomb) {
        super(row, col, bomb);
    }
    
    /**
     * Reset the board to the initial state (empty, ready to play)
     */
    @Override
    public void resetBoard()
    {
        for (List<Case> list : board) {
            for (Case c : list) {
                c.reset();
            }
        }
        this.nbFlag = 0;
        this.state = GameState.RUNNING;
        generateBomb();
        addNeighbours();
        this.update();
    }

    /**
     * Set the neighbours for every cases
     */
    @Override
    public void addNeighbours() {
        for (int row = 0; row < this.getBoard().size(); row++) {
            for (int col = 0; col < this.getBoard().get(row).size(); col++) {
                if (row < this.getBoard().size() - 1) {
                    board.get(row).get(col).addNeighbour(board.get(row + 1).get(col));
                    if (col < this.getBoard().get(row).size() - 1) {
                        board.get(row).get(col).addNeighbour(board.get(row + 1).get(col + 1));
                    }
                    if (col > 0) {
                        board.get(row).get(col).addNeighbour(board.get(row + 1).get(col - 1));
                    }
                }
                if (row > 0) {
                    board.get(row).get(col).addNeighbour(board.get(row - 1).get(col));
                    if (col < this.getBoard().get(row).size() - 1) {
                        board.get(row).get(col).addNeighbour(board.get(row - 1).get(col + 1));
                    }
                    if (col > 0) {
                        board.get(row).get(col).addNeighbour(board.get(row - 1).get(col - 1));
                    }
                }
                if (col < this.getBoard().get(row).size() - 1) {
                    board.get(row).get(col).addNeighbour(board.get(row).get(col + 1));
                }
                if (col > 0) {
                    board.get(row).get(col).addNeighbour(board.get(row).get(col - 1));
                }
            }
        }
    }

    /**
     * Set a flag on the grid according to the coordinates entered in the
     * arguments
     *
     * @param row the row id of the case clicked
     * @param col the col id of the case clicked
     */
    @Override
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
            this.state = GameState.WON;
            this.manageWin();
        }
        System.out.println(nbFlag);
        this.update();
    }

    /**
     * Manage the left click on a case
     * @param row : the row id of the case clicked
     * @param col : the col id of the case clicked
     */
    @Override
    public void leftClick(int row, int col) {

        Case c = this.getCase(row, col);

        switch (c.getState()) {
            case DISCOVERED:
                return; // Do Nothing
            case EMPTY:
                return; // Do Nothing
            case TRAPPED:
                this.state = GameState.LOST;
                c.setState(CaseState.TRIGGERED);
                this.manageDefeat();
                break;
            case UNDISCOVERED:
                c.discover();
                if (!(c.computeNbBomb() > 0)) {
                    c.discoverNeighbours();
                }
                if (this.gameWon()) {
                    this.state = GameState.WON;
                    manageWin();
                }
                break;
            default:
                break;
        }
        this.update();
    }

}
