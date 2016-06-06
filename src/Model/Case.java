/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package Model;

/**
 * Class case representing a case in the model Can be a trap or not, displayed
 * or not yet, or set as flag
 */
public final class Case {

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

    public Case() {
        this.setFlag(false);
        this.setTrap(false);
        this.setNbNeighbour(0);
        this.setVisible(false);
    }

    public void setFlag() {
        this.flag = !flag;
    }
}
