package managers;

import javafx.scene.image.Image;

public interface ImageManagerInterface {

    /**
     * Return an Image of a Pawn
     * @return Image
     */
    Image getPawnImage();

    /**
     * Return an Image of a Tower
     * @return Image
     */
    Image getTowerImage();

    /**
     * Return an Image of a Rider
     * @return Image
     */
    Image getRiderImage();

    /**
     * Return an Image of a Queen
     * @return Image
     */
    Image getQueenImage();

    /**
     * Return an Image of a King
     * @return Image
     */
    Image getKingImage();

    /**
     * Return an Image of a Crazy
     * @return Image
     */
    Image getCrazyImage();

}
