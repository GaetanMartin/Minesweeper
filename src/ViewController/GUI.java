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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class GUI used as a ViewController to display the GUI & manage the events
 */
public class GUI extends Application implements Observer
{

    private Board model;
    private ImageView[][] tabImageView;
    private Button smiley;
    private final int SQUARESIZE = 30;
    private ImageRefresher imageRefresher;

    /**
     * Max number of threads for the ExecutorService Used to process the model
     */
    private final int NB_THREAD_MAX = 5;

    /**
     * Executor : Provides @NB_THREAD_MAX threads Stores in queue the tasks if
     * more than @NB_THREAD_MAX threads are needed
     */
    private final ExecutorService executor
            = Executors.newFixedThreadPool(NB_THREAD_MAX, (Runnable r)
                    -> 
                    {
                        Thread thread = new Thread(r);
                        thread.setDaemon(true);
                        return thread;
            });

    @Override

    public void start(Stage primaryStage)
    {
        model = new Board(5, 5, 2);
        tabImageView = new ImageView[model.getRow()][model.getCol()];
        model.addObserver(this);

        // gestion du placement (permet de palcer le champ Text affichage en haut, et GridPane gPane au centre)
        BorderPane border = new BorderPane();

        HBox hbox = this.buildTopBar();

        imageRefresher = new ImageRefresher(tabImageView, smiley, model);
        // permet de placer les diffrents boutons dans une grille
        GridPane gPane = new GridPane();
        this.buildGrid(gPane);

        border.setCenter(gPane);
        border.setTop(hbox);
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
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        Platform.runLater(imageRefresher);
    }

    public HBox buildTopBar()
    {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(2);
        hbox.setStyle("-fx-background-color: #336699;");

        addStackPane(hbox);
        return hbox;
    }

    public void addStackPane(HBox hb)
    {
        StackPane stack = new StackPane();
        smiley = new Button();

        Image image = new Image(getClass().getResource("/images/Sleep.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        smiley.setGraphic(imageView);

        smiley.setOnMouseClicked((MouseEvent event)
                -> 
                {
                    if (event.getButton() == MouseButton.PRIMARY)
                    {
                        executor.execute(()
                                -> 
                                {
                                    model.resetBoard();
                        });
                    }
        });

        stack.getChildren().addAll(smiley);
        stack.setAlignment(Pos.CENTER);     // Right-justify nodes in stack

        hb.getChildren().add(stack);            // Add to HBox from Example 1-2
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }
    /**
     * Method to build the playing grid
     *
     * @return GridPane
     */
    public void buildGrid(GridPane gPane)
    {
        int column = 0;
        int row = 0;

        for (int i = 0; i < this.model.getRow(); i++)
        {
            for (int j = 0; j < this.model.getCol(); j++)
            {
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
                if (column > this.model.getCol() - 1)
                {
                    column = 0;
                    row++;
                }

                imageView.setOnMouseClicked((MouseEvent event)
                        -> 
                        {
                            if (model.gameFinished())
                            {
                                System.out.println("Game Finished ! ");
                                return;
                            }
                            if (event.getButton() == MouseButton.SECONDARY)
                            {
                                executor.execute(()
                                        -> 
                                        {
                                            model.rightClick(ci, cj);
                                });
                            } else if (event.getButton() == MouseButton.PRIMARY)
                            {
                                executor.execute(()
                                        -> 
                                        {
                                            model.leftClick(ci, cj);
                                });
                            }
                });
            }
        }
    }
}
