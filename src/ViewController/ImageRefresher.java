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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class ImageRefresher used to refresh the GUI
 *
 * @author Gaetan
 */
public class ImageRefresher implements Runnable {

    private final List<List<Node>> images;

    private final Button smiley;

    private final Board model;

    public ImageRefresher(List<List<Node>> images, Board model, Button smiley) {
        this.images = images;
        this.model = model;
        this.smiley = smiley;
    }

    @Override
    public void run() {
        
        setSmiley(smiley, model.getState());
        for (int i = 0; i < images.size(); i++) {
            for (int j = 0; j < images.get(i).size(); j++) {
                ImageView caseImage = (ImageView) this.images.get(i).get(j);
                switch (model.getCase(i, j).getState()) {
                    case UNDISCOVERED:
                        caseImage.setImage(this.buildImage("/images/Square.png"));
                        break;
                    case FLAGGED:
                        caseImage.setImage(this.buildImage("/images/Flag.png"));
                        break;
                    case DISCOVERED:
                        int nbBombs = model.getCase(i, j).getNbBomb();
                        caseImage.setImage(this.buildImage("/images/Square" + nbBombs + ".png"));
                        break;
                    case EMPTY:
                        caseImage.setImage(this.buildImage("/images/EmptySquare.png"));
                        break;
                    case TRIGGERED:
                        caseImage.setImage(this.buildImage("/images/Mine.png"));
                        break;
                    case TRAPPED:
                        if (model.gameFinished()) // Display only if the game is finished
                        {
                            caseImage.setImage(this.buildImage("/images/Bomb.png"));
                        }
                        break;
                    default:
                        caseImage.setImage(this.buildImage("/images/Square.png"));
                        break;
                }
            }
        }
    }

    /**
     * Get an image form its path
     *
     * @param imagePath
     * @return Image, the image desired
     */
    public Image buildImage(String imagePath) {
        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        return image;
    }

    /**
     * Set the button given accordingly
     * @param b
     * @param state 
     */
    private void setSmiley(Button b, GameState state) {
        Image image;
        switch (state) {
            case LOST:
                image = new Image(getClass().getResource("/images/Cry.png").toExternalForm());
                break;
            case WON:
                image = new Image(getClass().getResource("/images/Win.png").toExternalForm());
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
