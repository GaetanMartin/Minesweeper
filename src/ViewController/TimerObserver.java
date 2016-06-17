/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package ViewController;

import Model.Board;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.Label;
import Model.GameTimer;
import javafx.application.Platform;

/**
 * Class needed to refresh the timer in the view
 *
 * @author p1509413
 */
public class TimerObserver implements Observer {

    private final GameTimer modelTimer;
    private final Label label;
    private final Board model;

    public TimerObserver(Label timerLabel, Board model) {
        this.label = timerLabel;
        this.model = model;
        this.modelTimer = model.getTimer();
    }

    /**
     * Refresh the label at each tick of the timer
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
            String timerValue = this.modelTimer.getValue();
            if ("0".equals(timerValue)) {
                model.resetBoard();
                modelTimer.restart();
            } else {
                this.label.setText(this.modelTimer.getValue());
            }
        });
    }

}
