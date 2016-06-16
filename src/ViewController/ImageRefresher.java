/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package ViewController;

import Model.Board;
import Model.GameState;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * Class ImageRefresher used to refresh the GUI
 *
 * @author Gaetan
 */
public class ImageRefresher implements Runnable
{

    private final List<List<Node>> images;

    private final Button smiley;

    private final Board model;

    public ImageRefresher(List<List<Node>> images, Board model, Button smiley)
    {
        this.images = images;
        this.model = model;
        this.smiley = smiley;
    }

    @Override
    public void run()
    {
        Image image;
        setSmiley(smiley, model.getState());
        for (int i = 0; i < images.size(); i++)
        {
            for (int j = 0; j < images.get(i).size(); j++)
            {
                Node caseImage = this.images.get(i).get(j);
                switch (model.getCase(i, j).getState())
                {
                    case UNDISCOVERED:
                        image = this.buildImage("/images/Square.png");
                        // caseImage.setImage(this.buildImage("/images/Square.png"));
                        break;
                    case FLAGGED:
                        image = this.buildImage("/images/Flag.png");
                        // caseImage.setImage(this.buildImage("/images/Flag.png"));
                        break;
                    case DISCOVERED:
                        int nbBombs = model.getCase(i, j).getNbBomb();
                        image = this.buildImage("/images/Square" + nbBombs + ".png");
                        // caseImage.setImage(this.buildImage("/images/Square" + nbBombs + ".png"));
                        break;
                    case EMPTY:
                        image = this.buildImage("/images/EmptySquare.png");
                        // caseImage.setImage(this.buildImage("/images/EmptySquare.png"));
                        break;
                    case TRIGGERED:
                        image = this.buildImage("/images/Mine.png");
                        // caseImage.setImage(this.buildImage("/images/Mine.png"));
                        break;
                    case TRAPPED:
                        if (model.gameFinished()) // Display only if the game is finished
                        {
                            // caseImage.setImage(this.buildImage("/images/Bomb.png"));
                            image = this.buildImage("/images/Bomb.png");
                        } else
                        {
                            image = this.buildImage("/images/Square.png");
                        }
                        break;
                    default:
                        // caseImage.setImage(this.buildImage("/images/Square.png"));
                        image = this.buildImage("/images/Square.png");
                        break;
                }
                this.putImage(caseImage, image);
            }
        }
    }

    /**
     * Set an image on the node depending if it's a form (polygon) or an
     * imageview
     *
     * @param n
     * @param i
     */
    private void putImage(Node n, Image i)
    {
        if (n instanceof Polygon)
        {
            Polygon p = (Polygon) n;
            p.setFill(new ImagePattern(i));
        } else
        {
            ImageView iv = (ImageView) n;
            iv.setImage(i);
        }
    }

    /**
     * Get an image form its path
     *
     * @param imagePath
     * @return Image, the image desired
     */
    public Image buildImage(String imagePath)
    {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        return image;
    }

    /**
     * Set the button given accordingly
     *
     * @param b
     * @param state
     */
    private void setSmiley(Button b, GameState state)
    {
        Image image;
        switch (state)
        {
            case LOST:
                image = new Image(getClass().getResource("/images/Cry.png").toExternalForm());
                break;
            case WON:
                image = new Image(getClass().getResource("/images/Win.png").toExternalForm());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Gagner");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez gagné et vostre score est de " + this.model.getScore());
                alert.showAndWait();
                break;
            default:
                image = new Image(getClass().getResource("/images/Smile.png").toExternalForm());
                break;

        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        b.setGraphic(imageView);
    }
}
