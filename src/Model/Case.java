/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.List;
import java.util.LinkedList;

/**
 *
 * @author p1508754
 */
public class Case
{

    private boolean flag;
    private boolean trap;
    private boolean visible;
    private LinkedList<Case> neighbours;
    private int nbBomb; //Number of bonbs neighbour to current case

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public boolean isTrap()
    {
        return trap;
    }

    public void setTrap(boolean trap)
    {
        this.trap = trap;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public LinkedList getNeighbours()
    {
        return neighbours;
    }

    public void setNeighbours(LinkedList neighbours)
    {
        this.neighbours = neighbours;
    }

    public void addNeighbour(Case c)
    {
        neighbours.add(c);
    }

    public int getNbBomb()
    {
        return nbBomb;
    }

    public Case()
    {
        this.setFlag(false);
        this.setTrap(false);
        this.neighbours = new LinkedList<Case>();
        this.setVisible(false);
    }

    public void setFlag()
    {
        if (this.flag) {
            flag = false;
        } else {
            flag = true;
        }
    }

    public void discover()
    {
        if (!isVisible()) 
        {
            this.setVisible(true);
            this.computeNbBomb();
        }
    }

    public void computeNbBomb()
    {
        nbBomb = 0;
        for (Case c : this.neighbours) {
            if (c.isTrap()) {
                nbBomb++;
            }
        }
    }
}
