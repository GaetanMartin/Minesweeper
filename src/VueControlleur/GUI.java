/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VueControlleur;

import Model.Board;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author p1508754
 */
public class GUI extends Application implements Observer {

    final Board model = new Board();
    ImageView[][] tabImageView = new ImageView[8][8];

    @Override
    public void start(Stage primaryStage) {
        model.addObserver(this);
        //model = 
        // gestion du placement (permet de palcer le champ Text affichage en haut, et GridPane gPane au centre)
        BorderPane border = new BorderPane();

        // permet de placer les diffrents boutons dans une grille
        GridPane gPane = new GridPane();
        GridPane.setMargin(gPane, new Insets(0, 0, 0, 0));
        int column = 0;
        int row = 0;

        // création des bouton et placement dans la grille
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int cj = j;
                final int ci = i;
                Image image = new Image(getClass().getResource("/images/Square.png").toExternalForm());
                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(30);

                imageView.setFitHeight(30);
                        
                gPane.add(imageView, column++, row);

                if (column > 7) {
                    column = 0;
                    row++;
                }

                // on efface affichage lors du clic
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            model.rightClick(ci, cj);
                        }
                    }
                });

                tabImageView[i][j] = imageView;
            }
        }

        border.setCenter(gPane);
        gPane.setBorder(Border.EMPTY);
        gPane.setPadding(new Insets(2,2,2,2));
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

    public void refreshBoard() {

    }

    @Override
    public void update(Observable o, Object arg) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (model.getCase(i, j).isFlag()) {
                    Image image = new Image(getClass().getResource("/images/drapeau.png").toExternalForm());
                    ImageView imageView = new ImageView(image);

                    imageView.setFitWidth(30);

                    imageView.setFitHeight(30);
                    imageView.setSmooth(true);

                    this.tabImageView[i][j].setImage(image);
                }
            }
        }
    }

}
