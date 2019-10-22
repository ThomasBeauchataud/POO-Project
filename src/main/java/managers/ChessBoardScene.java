package managers;

import entites.Position;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ChessBoardScene extends Scene {

    private boolean clicked;
    private Position selected;

    public ChessBoardScene(Parent root, double width, double height) {
        super(root, width, height);
    }

    public boolean isClicked() {
        return selected != null;
    }

    public Position getSelected() {
        return selected;
    }

    public void setSelected(Position selected) {
        this.selected = selected;
    }
}
