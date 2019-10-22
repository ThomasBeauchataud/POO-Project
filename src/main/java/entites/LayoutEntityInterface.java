package entites;

import javafx.scene.image.ImageView;

/**
 * A LayoutEntity is an Entity which is represented by an ImageView
 */
public interface LayoutEntityInterface {

    /**
     * Return the ImageView of the entity
     * @return ImageView
     */
    ImageView getImageView();

    /**
     * Set the ImageView of the entity
     * @param imageView ImageView
     */
    void setImageView(ImageView imageView);

}
