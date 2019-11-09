package view;

import pieces.Piece;
import javafx.scene.image.Image;

import java.io.File;

public class ImageManager {

    /**
     * Return an image associated to the piece
     * @param piece Piece
     * @return Image
     */
    public static Image loadImage(Piece piece) {
        String filePath = System.getProperty("user.dir") + "/resources/" + piece.getTeamColor().name() + "_"
                + piece.getClass().getSimpleName( )+ ".png";
        return new Image(new File(filePath).toURI().toString());
    }

}
