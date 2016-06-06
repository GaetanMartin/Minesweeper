/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package ViewController;

import Model.Board;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class ImageRefresher used to refresh the GUI
 * @author Gaetan
 */
public class ImageRefresher implements Runnable {

    private final ImageView[][] images;

    private final Board model;

    public ImageRefresher(ImageView[][] images, Board model) {
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
        for (int i = 0; i < model.getRow(); i++)
        {
            for (int j = 0; j < model.getCol(); j++)
            {
                
                ImageView caseImage =  this.images[i][j];
                if (model.getCase(i, j).isFlag())
                {
                    caseImage.setImage(this.buildImage("/images/Flag.png"));
                }
                else if(model.getCase(i, j).isVisible() && model.getCase(i, j).isLost())
                {
                    caseImage.setImage(this.buildImage("/images/Mine.png"));
                }
                else if(model.getCase(i, j).isVisible() && model.getCase(i, j).isTrap())
                {
                    caseImage.setImage(this.buildImage("/images/Bomb.png"));
                }
                else if(model.getCase(i, j).isVisible() && model.getCase(i, j).getNbBomb() != 0)
                {
                    int nbBombs = model.getCase(i, j).getNbBomb();
                    caseImage.setImage(this.buildImage("/images/Square" + nbBombs + ".png"));
                }
                else if(model.getCase(i, j).isVisible())
                {
                    caseImage.setImage(this.buildImage("/images/EmptySquare.png"));
                }
                else
                {
                    caseImage.setImage(this.buildImage("/images/Square.png"));
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
