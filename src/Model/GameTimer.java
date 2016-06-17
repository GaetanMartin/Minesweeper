/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package Model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author seljo
 */
public class GameTimer extends Observable {

    private Timer t;
    private int value; //Value of counter

    /**
     *
     * @return the value of counter in string
     */
    public String getValue() {
        return Integer.toString(value);
    }

    /**
     *
     * @return the value of counter in integer
     */
    public int getValueInt() {
        return value;
    }

    public GameTimer() {
        this.value = 300;
        t = new Timer();
    }

    /**
     * Method to start the counter
     */
    public void start() {
        this.value = 300;
        this.t.schedule(new TimerTask() {
            @Override
            public void run() {
                value--;
                // notify the view
                setChanged();
                notifyObservers();
                if (value == 0) // if counter arrives zero
                {
                    stop();
                }
            }
        }, 0, 1000);
    }

    /*
     * Method to stop the counter
     */
    public void stop() {
        this.t.cancel();
        this.t.purge();
    }

    /**
     * REstart the counter
     */
    public void restart() {
        stop();
        t = new Timer();
        start();
    }

}
