/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package Model;

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
     * Set the neighbours for every cases
     */
    @Override
    public void setUpNeighbours() {
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
}
