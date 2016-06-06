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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author p1508754
 */

//POur aller plus loin exécuter le model dqns les thread à l'aide de "ExecutionService"
public class GUI extends Application implements Observer
{

    private Board model;
    private ImageView[][] tabImageView;
    private final int SQUARESIZE = 25;

    @Override
    public void start(Stage primaryStage)
    {
        model = new Board(5, 5, 5);
        tabImageView = new ImageView[model.getRow()][model.getCol()];
        model.addObserver(this);

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
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        for (int i = 0; i < model.getRow(); i++)
        {
            for (int j = 0; j < model.getCol(); j++)
            {
                ImageView caseImage =  this.tabImageView[i][j];
                if (model.getCase(i, j).isFlag())
                {
                    caseImage.setImage(this.buildImage("/images/Flag.png"));
                } 
                else if(model.getCase(i, j).isVisible() && model.getCase(i, j).isTrap())
                {
                    caseImage.setImage(this.buildImage("/images/Bomb.png"));
                }
                else if(model.getCase(i, j).isVisible() && model.getCase(i, j).getNbBomb() != 0)
                {
                    int nbBonbs = model.getCase(i, j).getNbBomb();
                    caseImage.setImage(this.buildImage("/images/Square" + nbBonbs + ".png"));
                           
                }
                else
                {
                    this.tabImageView[i][j].setImage(this.buildImage("/images/Square.png"));
                }
            }
        }
    }

    /**
     * Method to build the playing grid
     * @return GridPane
     */
    public GridPane buildGrid()
    {
        GridPane gPane = new GridPane();
        int column = 0;
        int row = 0;

        // création des bouton et placement dans la grille
        for (int i = 0; i < this.model.getRow(); i++)
        {
            for (int j = 0; j < this.model.getCol(); j++)
            {
                final int cj = j;
                final int ci = i;
                Image image = this.buildImage("/images/Square.png");
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

                // on efface affichage lors du clic
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>()
                {

                    @Override
                    public void handle(MouseEvent event)
                    {
                        if (event.getButton() == MouseButton.SECONDARY)
                        {
                            model.rightClick(ci, cj);
                        } 
                        else if (event.getButton() == MouseButton.PRIMARY)
                        {
                             model.leftClick(ci, cj);
                        }
                    }
                });
            }
        }
        return gPane;
    }

    private Image buildImage(String imagePath)
    {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        return image;
    }

}
