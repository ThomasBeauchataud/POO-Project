package pieces;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class LayoutEntity extends Group implements LayoutEntityInterface {

    private ImageView imageView;

    @Override
    public ImageView getImageView() {
        return imageView;
    }

    @Override
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void resize(double width, double height) {
        ImageView imageView = this.getImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        this.setImageView(imageView);
    }

    @Override
    public void relocate(double x, double y) {
        ImageView imageView = this.getImageView();
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        this.setImageView(imageView);
        centerImage();
    }

    private void centerImage() {
        ImageView imageView = this.getImageView();
        Image img = imageView.getImage();
        if (img != null) {
            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();
            double reducedCoefficient = Math.min(ratioX, ratioY);
            double w = img.getWidth() * reducedCoefficient;
            double h = img.getHeight() * reducedCoefficient;
            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);
            this.setImageView(imageView);
        }
    }

}
