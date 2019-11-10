package game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.Window;
import view.WindowInterface;

import static game.ChessBoard.boardSize;

@SuppressWarnings("WeakerAccess")
public abstract class ChessBoardView extends Pane implements ChessBoardGameInterface, ChessBoardViewInterface {

    private double cellWidth;
    private double cellHeight;
    private Rectangle background;
    private WindowInterface[][] windows;

    public ChessBoardView() {
        windows = new Window[boardSize][boardSize];
        background = new Rectangle();
        background.setFill(Color.WHITE);
        getChildren().add(background);
    }

    protected double getCellWidth() {
        return cellWidth;
    }

    protected double getCellHeight() {
        return cellHeight;
    }

    protected WindowInterface getWindow(int x, int y) {
        return windows[x][y];
    }

    protected void setWindow(int i, int j, WindowInterface window) {
        windows[i][j] = window;
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        background.setWidth(width);
        background.setHeight(height);
        cellWidth = width / 8.0;
        cellHeight = height / 8.0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getPiece(i,j) != null) {
                    getPiece(i,j).relocate(i * cellWidth, j * cellHeight);
                    getPiece(i,j).resize(cellWidth, cellHeight);
                }
                windows[i][j].relocate(i * cellWidth, j * cellHeight);
                windows[i][j].resize(cellWidth, cellHeight);
            }
        }
    }

}
