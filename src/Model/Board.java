/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Observable;

/**
 *
 * @author p1508754
 */
public class Board extends Observable {

    private Case[][] board;

    public Case[][] getBoard() {
        return board;
    }

    public void setBoard(Case[][] board) {
        this.board = board;
    }

    public Board(Case[][] board) {
        this.board = board;
    }

    public Board() {
        this.board = new Case[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Case(false);
            }
        }

    }

    public void rightClick(int i, int j) 
    {
        this.getCase(i, j).setFlag();
        this.update();
    }

    public Case getCase(int i, int j) {
        return this.board[i][j];
    }

    public void update() {
        // notification de la vue, suite à la mise à jour du champ lastValue
        setChanged();
        notifyObservers();
    }

}
