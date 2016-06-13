/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package ViewController;

import Model.Board;
import Model.Board2D;
import Model.BoardPyramid;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class GUI used as a ViewController to display the GUI & manage the events
 */
public class GUI extends Application implements Observer {

    private Board model;
    private static List<List<Node>> caseNodes;

    public static List<List<Node>> getCaseNodes() {
        return caseNodes;
    }
    private Button smiley;
    private static final double SQUARESIZE = 30;
    private static ImageRefresher imageRefresher;

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
                    -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {

        BorderPane borderPane = new BorderPane();
        HBox hbox = this.buildTopBar();

        model = new BoardPyramid(10, 10, 15);
        model.addObserver(this);
        
        caseNodes = new ArrayList<>();
        for (int i = 0; i < model.getBoard().size(); i++) {
            caseNodes.add(new ArrayList<>());
        }
        
        imageRefresher = new ImageRefresher(caseNodes, model, smiley);
        
        Pane p;
        if (model instanceof BoardPyramid)
            p = PaneBuilder.createBorderPane(model, executor, SQUARESIZE);
        else
            p = PaneBuilder.createGridPane(model, executor);
        
        p.setMinSize(model.getBoard().size() * SQUARESIZE, model.getBoard().size() * SQUARESIZE);
        borderPane.setCenter(p);
        borderPane.setTop(hbox);
        Scene scene = new Scene(borderPane, Color.WHITE);

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(imageRefresher);
    }

    public HBox buildTopBar() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(2);
        hbox.setStyle("-fx-background-color: #336699;");
        
        Image image = new Image(getClass().getResource("/images/Sleep.png").toExternalForm());
        smiley = this.createSmileyButton(image);
        StackPane stack = PaneBuilder.createStackPane(smiley, image, model, executor);
        
        hbox.getChildren().add(stack);            // Add to HBox from Example 1-2
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
        return hbox;
    }

    public static ImageView createImageView() {
        Image image = imageRefresher.buildImage("/images/Square.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(SQUARESIZE);
        imageView.setFitHeight(SQUARESIZE);
        imageView.setSmooth(true);
        return imageView;
    }
    
    /**
     * Create and set up a button
     * @param image
     * @return 
     */
    private Button createSmileyButton(Image image) {
        Button button = new Button();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        button.setGraphic(imageView);

        button.setOnMouseClicked((MouseEvent event)
                -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        executor.execute(() -> {
                            model.resetBoard();
                        });
                    }
                });
        return button;
    }
}
