/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 *
 * @author p1508754
 */
public class Case {
    private boolean flag;
    private int nbNeighbour;
    private boolean trap;
    private boolean visible;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getNbNeighbour() {
        return nbNeighbour;
    }

    public void setNbNeighbour(int nbNeighbour) {
        this.nbNeighbour = nbNeighbour;
    }

    public boolean isTrap() {
        return trap;
    }

    public void setTrap(boolean trap) {
        this.trap = trap;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    
    
    public Case(boolean flag)
    {
        this.flag = flag;
    }
    
    public void setFlag()
    {
        if(this.flag)
            flag = false;
        else
            flag = true;
    }
}
