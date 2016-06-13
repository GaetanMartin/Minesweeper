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
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
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
    private static final int SQUARESIZE = 20;
    private static ImageRefresher imageRefresher;
    private Stage primaryStage;
    Pane p;
    BorderPane borderPane;

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
        this.primaryStage = primaryStage;
        Scene scene = initGame(9, 9);
        this.primaryStage.sizeToScene();
        this.primaryStage.setTitle("Minesweeper");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

    }

    private Scene initGame(int row, int col) {
        borderPane = new BorderPane();

        BorderPane b = buildTopMenuPane();

        model = new Board2D(row, col, 15);
        model.addObserver(this);

        caseNodes = new ArrayList<>();
        for (int i = 0; i < model.getBoard().size(); i++) {
            caseNodes.add(new ArrayList<>());
        }

        imageRefresher = new ImageRefresher(caseNodes, model, smiley);

        if (model instanceof BoardPyramid) {
            p = PaneBuilder.createBorderPane(model, executor);
        } else {
            p = PaneBuilder.createGridPane(model, executor);
        }
        borderPane.setTop(b);
        borderPane.setCenter(p);
        Scene scene = new Scene(borderPane, Color.WHITE);
        return scene;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("update");
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
     *
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

    /**
     * Methode to build the top bqt including the menu bar and the smilet bar
     *
     * @return
     */
    private BorderPane buildTopMenuPane() {
        BorderPane bp = new BorderPane();
        MenuBar menuBar = new MenuBar();

        Menu menu = new Menu("Difficulté");

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        final Stage ps = this.primaryStage;
        slider.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                p.getChildren().remove(0, (model.getNbCase()));
                int level = (int) slider.getValue();
                switch (level) {
                    case 0:
                        model.changeLevel(9, 9, 10);
                        break;
                    case 50:
                        model.changeLevel(16, 16, 34);
                        break;
                    case 100:
                        model.changeLevel(16, 30, 100);
                        break;
                }

                caseNodes = new ArrayList<>();
                for (int i = 0; i < model.getBoard().size(); i++) {
                    caseNodes.add(new ArrayList<>());
                }

                imageRefresher = new ImageRefresher(caseNodes, model, smiley);

                if (model instanceof BoardPyramid) {
                    p = PaneBuilder.createBorderPane(model, executor);
                } else {
                    p = PaneBuilder.createGridPane(model, executor);
                }
                
                borderPane.setCenter(p);
                ps.sizeToScene();

            }

        });

        CustomMenuItem customMenuItem = new CustomMenuItem(slider);
        customMenuItem.setHideOnClick(false);

        menu.getItems().add(customMenuItem);

        menuBar.getMenus().add(menu);

        HBox hbox = this.buildTopBar();
        bp.setTop(hbox);

        bp.setTop(menuBar);
        bp.setCenter(hbox);
        return bp;
    }

}
