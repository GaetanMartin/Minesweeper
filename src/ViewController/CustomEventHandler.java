/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package ViewController;

import Model.Board;
import java.util.concurrent.ExecutorService;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Custom Event Handler of Mouse clicks events
 *
 * @author Gaetan
 */
public class CustomEventHandler implements EventHandler {

    private final Board model;
    private final int i;
    private final int j;
    private final ExecutorService executor;

    public CustomEventHandler(Board model, int i, int j, ExecutorService executor) {
        this.model = model;
        this.i = i;
        this.j = j;
        this.executor = executor;
    }

    public void handle(MouseEvent event) {
        if (model.gameFinished()) {
            System.out.println("Game Finished ! ");
            return;
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            executor.execute(() -> {
                model.rightClick(i, j);
            });
        } else if (event.getButton() == MouseButton.PRIMARY) {
            executor.execute(() -> {
                model.leftClick(i, j);
            });
        }
    }

    @Override
    public void handle(Event event) {
        if (event instanceof MouseEvent) {
            this.handle((MouseEvent) event);
        } else {
            System.err.println("Events except MouseEvents not managed");
        }
    }

}
