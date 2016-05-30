/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VueControlleur;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author p1508754
 */
public class GUI extends Application {

    @Override
    public void start(Stage primaryStage)
    {
        // gestion du placement (permet de palcer le champ Text affichage en haut, et GridPane gPane au centre)
        BorderPane border = new BorderPane();

        // permet de placer les diffrents boutons dans une grille
        GridPane gPane = new GridPane();

        int column = 0;
        int row = 0;

        // création des bouton et placement dans la grille
        for (int i = 0; i < 8; i++) {
            final Text t = new Text("s");
            t.setWrappingWidth(30);
            t.setFont(Font.font("Verdana", 20));
            t.setTextAlignment(TextAlignment.CENTER);

            gPane.add(t, column++, row);

            if (column > 8) {
                column = 0;
                row++;
            }

        }

        gPane.setGridLinesVisible(true);

        border.setCenter(gPane);

        Scene scene = new Scene(border, Color.LIGHTBLUE);

        primaryStage.setTitle("Calc FX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
