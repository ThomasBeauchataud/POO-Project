package entites;

import javafx.scene.image.ImageView;

abstract class LayoutEntity {

    private ImageView imageView;

    public LayoutEntity(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

}
