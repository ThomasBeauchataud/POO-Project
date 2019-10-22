package managers;

import javafx.scene.image.Image;

import java.io.File;

//todo implements all this class
public class ImageManager implements ImageManagerInterface {

    private Image pawnImage;
    private Image riderImage;
    private Image towerImage;
    private Image queenImage;
    private Image kingImage;
    private Image crazyImage;

    public ImageManager() {
        loadImage();
    }

    @Override
    public Image getPawnImage() {
        return null;
    }

    @Override
    public Image getTowerImage() {
        return null;
    }

    @Override
    public Image getRiderImage() {
        return null;
    }

    @Override
    public Image getQueenImage() {
        return null;
    }

    @Override
    public Image getKingImage() {
        return null;
    }

    @Override
    public Image getCrazyImage() {
        return null;
    }

    private void loadImage() {
        //todo implement this method
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/pigeon.png");
        //pigeonImage = new Image(file.toURI().toString());
        file = new File(System.getProperty("user.dir") + "/src/main/resources/food.png");
        //foodImage = new Image(file.toURI().toString());
        file = new File(System.getProperty("user.dir") + "/src/main/resources/fear.png");
        //fearImage = new Image(file.toURI().toString());
    }

}
