/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package Model;


import java.util.LinkedList;

/**
 * Class case representing a case in the model Can be a trap or not, displayed
 * or not yet, or set as flag
 */
public class Case
{

    private boolean flag;
    private boolean trap;
    private boolean visible;
    private LinkedList<Case> neighbours;
    private boolean lost; //Case that causes the player to lose
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

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lostCase) {
        this.lost = lostCase;
    }

    public Case()
    {
        this.setFlag(false);
        this.setTrap(false);
        this.neighbours = new LinkedList<Case>();
        this.setVisible(false);
        this.setLost(false);
    }

    public void setFlag()
    {
        if (this.flag) {
            flag = false;
        } else {
            flag = true;
        }
    }

    public void discoverNeighbours()
    {
        if (!isTrap()) 
        {
            for (Case c : this.neighbours) 
            {
                if (!c.isVisible()&& !c.flag)
                {
                    c.discover();
                    if (c.nbBomb == 0) 
                    {
                        c.discoverNeighbours();
                    }
                }
            }
        }
    }

    public void discover()
    {
        if (!isVisible()) {
            this.setVisible(true);
            this.computeNbBomb();
        }
    }

    public int computeNbBomb()
    {
        nbBomb = 0;
        for (Case c : this.neighbours) {
            if (c.isTrap()) {
                nbBomb++;
            }
        }
        return nbBomb;
    }
}
