/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package ViewController;

import Model.Board;
import Model.Case;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class ImageRefresher used to refresh the GUI
 * @author Gaetan
 */
public class ImageRefresher implements Runnable {

    private final List<List<ImageView>> images;

    private final Board model;

    public ImageRefresher(List<List<ImageView>> images, Board model) {
        this.images = images;
        this.model = model;
    }

    @Override
    public void run() {        
        if(model.isWin())
        {
            System.out.println("WIIIIIIIIIIINNNNNNNNNNN");
        } else if(model.isLost())
        {
            System.out.println("LOOOOOOOOSSSSSSSSSSSTTTTT");
        }
        for (int i = 0; i < model.getBoard().size(); i ++)
        {
            for (int j = 0; j < model.getBoard().get(i).size(); j ++)
            {
                ImageView caseImage = this.images.get(i).get(j);                
                switch(model.getCase(i, j).getState())
                {
                    case FLAGGED :
                        caseImage.setImage(this.buildImage("/images/Flag.png")); 
                        break;
                    case DISCOVERED :
                        int nbBombs = model.getCase(i, j).getNbBomb();
                        caseImage.setImage(this.buildImage("/images/Square" + nbBombs + ".png"));
                        break;
                    case EMPTY : 
                        caseImage.setImage(this.buildImage("/images/EmptySquare.png"));
                        break;
                    case TRIGGERED : 
                        caseImage.setImage(this.buildImage("/images/Mine.png"));
                        break;
                    case TRAPPED : 
                        if (model.gameFinished()) // Display only if the game is finished
                            caseImage.setImage(this.buildImage("/images/Bomb.png"));
                        break;
                    default :
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
}
