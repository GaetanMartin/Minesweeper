/*
 * Polytech Lyon - 2016
 * Jensen JOYMANGUL & Gaetan MARTIN
 * Projet Informatique 3A - Creation d'un demineur MVC
 */
package ViewController;

import Model.Board;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * Class used to build Panes according to the model
 *
 * @author Gaetan
 */
public class PaneBuilder {

    /**
     * Create gridPane (adapted for Board2D)
     *
     * @param model Model
     * @param executor Service launching the imageRefresher
     * @return
     */
    public static GridPane createGridPane(Board model, ExecutorService executor) {
        return buildGridPane(model, executor);
    }

    /**
     * Method to build the playing grid
     *
     * @param model
     * @param executor
     * @return
     */
    private static GridPane buildGridPane(Board model, ExecutorService executor) {
        int column = 0;
        int row = 0;
        GridPane gPane = new GridPane();

        for (int i = 0; i < model.getBoard().size(); i++) {
            for (int j = 0; j < model.getBoard().get(i).size(); j++) {
                Node n = GUI.createImageView();
                gPane.add(n, column++, row);
                GUI.getCaseNodes().get(i).add(n);

                //When reaching end of a row
                if (column > model.getBoard().get(row).size() - 1) {
                    column = 0;
                    row++;
                }
                n.setOnMouseClicked(new CustomEventHandler(model, i, j, executor));
            }
        }
        gPane.setBorder(Border.EMPTY);
        gPane.setPadding(new Insets(2, 2, 2, 2));
        return gPane;
    }

    /**
     * Create stack pane (header)
     * @param smiley
     * @param image
     * @param model
     * @param executor
     * @return 
     */
    public static StackPane createStackPane(Button smiley, Image image, Board model, ExecutorService executor) {

        StackPane stack = new StackPane();
        stack.getChildren().addAll(smiley);
        stack.setAlignment(Pos.CENTER);     // Right-justify nodes in stack
        return stack;
    }

    /**
     * Create a BorderPane (used with BoardPyramid)
     *
     * @param model
     * @param executor
     * @return
     */
    public static BorderPane createBorderPane(Board model, ExecutorService executor, Double sizeSideTriangle) {
        return buildBorderPane(model, executor, sizeSideTriangle);
    }

    /**
     * Build the border pane according to the model (Board) given
     *
     * @param model
     * @return the border pane built
     */
    private static BorderPane buildBorderPane(Board model, ExecutorService executor, Double sizeSideTriangle) {
        BorderPane borderPane = new BorderPane();

        List<List<Polygon>> pyramid = createPyramid(model.getBoard().size(), sizeSideTriangle);
        for (int i = 0; i < pyramid.size(); i++) {
            for (int j = 0; j < pyramid.get(i).size(); j++) {
                Polygon p = pyramid.get(i).get(j);
                p.setOnMouseClicked(new CustomEventHandler(model, i, j, executor));
                borderPane.getChildren().add(p);
                p.setFill(new ImagePattern(GUI.createImageView().getImage())); // Todo maybe change here
                GUI.getCaseNodes().get(i).add(p);
            }
        }
        return borderPane;
    }

    private static List<List<Polygon>> createPyramid(int nbShapesBase, Double sizeSideTriangle) {
        Double y, x = y = 0.0;
        Double sideX = sizeSideTriangle;
        Double sideY = sideX;// Math.cos(Math.PI / 6) * sideX;
        List<List<Polygon>> list = new ArrayList<>();
        for (int i = nbShapesBase; i > 0; i--) {
            list.add(createRowPolygons(x, y, sideX, sideY, i));
            x += sideX / 2;
            y += sideY;
        }
        return list;
    }

    private static ArrayList<Polygon> createRowPolygons(Double x, Double y, Double sideX, Double sideY, int nbShapes) {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Double ax = x, ay = y;
        Double bx, by;
        Double cx = x, cy = y;
        for (int i = 0; i < nbShapes; i++) {
            ax = cx;
            ay = cy;
            bx = ax + sideX / 2;
            by = y + sideY;
            cx += sideX;
            cy = y;
            Polygon poly = new Polygon();
            Polygon poly2 = new Polygon();
            poly.getPoints().addAll(new Double[]{
                ax, ay,
                bx, by,
                cx, cy,});
            polygons.add(poly);
            if (i + 1 != nbShapes) {
                poly2.getPoints().addAll(new Double[]{
                    bx + sideX, by,
                    bx, by,
                    cx, cy,});
                polygons.add(poly2);
            }
        }

        return polygons;
    }

}
