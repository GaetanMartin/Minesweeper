/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package ViewController;

import Model.Board;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class GUI used as ViewController for our application Display the GUI & manage
 * the actions of the user processing the model
 */
public class GUI extends Application implements Observer {

    private Board model;
    private ImageView[][] tabImageView;
    private final int SQUARESIZE = 20;
    private ImageRefresher imageRefresher;

    /**
     * Max number of threads for the ExecutorService Used to process the model
     */
    private final int NB_THREAD_MAX = 5;

    /**
     * Executor : Provides @NB_THREAD_MAX threads Stores in queue the tasks if
     * more than @NB_THREAD_MAX threads are needed
     */
    private ExecutorService executor = Executors.newFixedThreadPool(NB_THREAD_MAX);

    @Override
    public void start(Stage primaryStage) {
        model = new Board(5, 5, 5);
        tabImageView = new ImageView[model.getRow()][model.getCol()];
        model.addObserver(this);
        imageRefresher = new ImageRefresher(tabImageView, model);

        // gestion du placement (permet de palcer le champ Text affichage en haut, et GridPane gPane au centre)
        BorderPane border = new BorderPane();

        // permet de placer les diffrents boutons dans une grille
        GridPane gPane = this.buildGrid();

        border.setCenter(gPane);
        gPane.setBorder(Border.EMPTY);
        gPane.setPadding(new Insets(2, 2, 2, 2));
        Scene scene = new Scene(border, Color.WHITE);

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new ImageRefresher(this.tabImageView, this.model));
    }

    /**
     * Method to build the playing grid
     *
     * @return GridPane
     */
    public GridPane buildGrid() {
        GridPane gPane = new GridPane();
        int column = 0;
        int row = 0;

        // Creating & setting the imageviews on the grid
        for (int i = 0; i < this.model.getRow(); i++) {
            for (int j = 0; j < this.model.getCol(); j++) {
                final int cj = j;
                final int ci = i;
                Image image = imageRefresher.buildImage("/images/Square.png");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(SQUARESIZE);
                imageView.setFitHeight(SQUARESIZE);
                imageView.setSmooth(true);
                gPane.add(imageView, column++, row);
                tabImageView[i][j] = imageView;

                //When reaching end of a row
                if (column > this.model.getCol() - 1) {
                    column = 0;
                    row++;
                }

                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            executor.execute(() -> {
                                model.rightClick(ci, cj);
                            });
                        } else if (event.getButton() == MouseButton.PRIMARY) {
                            executor.execute(() -> {
                                model.leftClick(ci, cj);
                            });
                        }
                    }
                });
            }
        }
        return gPane;
    }
}
