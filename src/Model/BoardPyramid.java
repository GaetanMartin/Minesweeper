/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a pyramidal board
 * @author Gaetan
 */
public class BoardPyramid extends Board {

    /**
     * Size of the base of the pyramid
     */
    private int baseSize;

    public BoardPyramid(int baseSize, int bomb) {
        super(baseSize, 0, bomb);
        this.baseSize = baseSize;
    }

    /**
     * Create the board
     *
     * @param row
     * @param col
     * @return
     */
    @Override
    protected List<List<Case>> createBoard(int row, int col) {
        this.baseSize = row;
        return this.createPyramid();
    }

    /**
     * Create a pyramid with baseSize
     *
     * @return
     */
    private List<List<Case>> createPyramid() {
        return this.createPyramid(baseSize);
    }

    /**
     * Return the list of case as a Pyramidial shape
     *
     * @return
     */
    private List<List<Case>> createPyramid(int baseSize) {
        List<List<Case>> grid = new ArrayList<>();
        for (int i = baseSize; i > 0; i--) {
            grid.add(createRowTriangularCases(i));
        }
        return grid;
    }

    /**
     * Create a row of cases
     *
     * @param size
     * @return
     */
    private List<Case> createRowTriangularCases(int size) {
        List<Case> row = new ArrayList<>();
        for (int i = 0; i < 2 * size - 1; i++) {
            row.add(new Case());
        }
        return row;
    }

    /**
     * Set the neighbours for every cases
     */
    @Override
    public void setUpNeighbours() {
        boolean down = true;
        for (int row = 0; row < this.getBoard().size(); row++) {
            for (int col = 0; col < this.getBoard().get(row).size(); col++) {
                if (getLeft(row, col) != null) {
                    board.get(row).get(col).addNeighbour(getLeft(row, col));
                }
                if (getRight(row, col) != null) {
                    board.get(row).get(col).addNeighbour(getRight(row, col));
                }
                if (down && getUp(row, col) != null) {
                    board.get(row).get(col).addNeighbour(getUp(row, col));
                } else if (getDown(row, col) != null) { // Up
                    board.get(row).get(col).addNeighbour(getDown(row, col));
                }
                down = !down;
            }
            down = true; // Each line starts with a down triangle
        }
    }

    /**
     * Try to get left neighbour
     *
     * @param row
     * @param col
     * @return the case corresponding if found, null else
     */
    private Case getLeft(int row, int col) {
        if (col == 0 || getBoard().get(row).isEmpty()) {
            return null;
        }
        return getBoard().get(row).get(col - 1);
    }

    /**
     * Try to get Right neighbour
     *
     * @param row
     * @param col
     * @return the case corresponding if found, null else
     */
    private Case getRight(int row, int col) {
        if (col >= getBoard().get(row).size() - 1 || getBoard().get(row).isEmpty()) {
            return null;
        }
        return getBoard().get(row).get(col + 1);
    }

    /**
     * Try to get up neighbour
     *
     * @param row
     * @param col
     * @return the case corresponding if found, null else
     */
    private Case getUp(int row, int col) {
        if (getBoard().isEmpty() || row == 0 || col >= getBoard().get(row).size() - 1) {
            return null;
        }
        return getBoard().get(row - 1).get(col + 1);
    }

    /**
     * Try to get down neighbour
     *
     * @param row
     * @param col
     * @return the case corresponding if found, null else
     */
    private Case getDown(int row, int col) {
        if (row >= getBoard().size() - 1 || getLeft(row, col) == null || getRight(row, col) == null) {
            return null;
        }
        List<Case> list = getBoard().get(row + 1);
        return list.get(col - 1);
    }

}
