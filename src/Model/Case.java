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
    private int nbBomb; //Number of bonbs neighbour to current case
    private CaseState state;

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
        this.setState(flag ? CaseState.FLAGGED : CaseState.UNDISCOVERED);
    }

    public boolean isTrap()
    {
        return trap;
    }

    public void setTrap(boolean trap)
    {
        this.trap = trap;
        this.setState(CaseState.TRAPPED);
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
        this.setState();
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
        this.setState();
    }

    public void setFlag()
    {
        this.setState((this.flag = ! this.flag) ? CaseState.FLAGGED : CaseState.UNDISCOVERED);
        
    }
    
    /**
     * @return the state
     */
    public CaseState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(CaseState state) {
        this.state = state;
    }
    
    public void setState()
    {
        if (this.state == CaseState.TRIGGERED) return;
        
        if (flag)
            setState(CaseState.FLAGGED);
        else if (! isVisible())
            setState(CaseState.UNDISCOVERED);
        else if (isTrap() && isVisible())
            setState(CaseState.TRAPPED);
        else if (isVisible() && nbBomb == 0)
            setState(CaseState.EMPTY);
        else
            setState(CaseState.DISCOVERED);
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
            nbBomb = computeNbBomb();
        }
        this.setState();
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
