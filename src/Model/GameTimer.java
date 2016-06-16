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
public class GameTimer extends Observable
{

    private Timer t;
    private int value;

    public String getValue()
    {
        return Integer.toString(value);
    }
    
    public int getValueInt()
    {
        return value;
    }

    public GameTimer()
    {
        this.value = 300;
        t = new Timer();
    }

    public void start()
    {
        this.value = 300;
        this.t.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                value--;
                 setChanged();
                notifyObservers();
                if(value  == 0)
                    stop();
            }
        }, 0, 1000);
    }

    private void stop()
    {
        this.t.cancel();
    }

    public void restart()
    {
        stop();
        t = new Timer();
        start();
    }

}
