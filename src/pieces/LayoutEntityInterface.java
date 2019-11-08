package pieces;

import javafx.scene.image.ImageView;

/**
 * Manage the Image View of the Entity
 */
public interface LayoutEntityInterface {

    /**
     * Return the ImageView of the Entity
     * @return ImageView
     */
    ImageView getImageView();

    /**
     * Set the ImageView of the Entity
     * @param imageView ImageView
     */
    void setImageView(ImageView imageView);

    /**
     * Relocate the ImageView
     * @param x double
     * @param y double
     */
    void relocate(double x, double y);

    /**
     * Resize the ImageView
     * @param width double
     * @param height double
     */
    void resize(double width, double height);

}
