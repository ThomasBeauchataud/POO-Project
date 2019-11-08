package pieces;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

public abstract class LayoutEntity extends Group {

    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

}
