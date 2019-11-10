package view;

import game.TimerInterface;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static game.ChessBoard.boardSize;

public abstract class ChessBoardView extends Pane implements ChessBoardViewInterface {

    private double cellWidth;
    private double cellHeight;
    private Rectangle background;
    private WindowInterface[][] windows;
    private StatusBarInterface statusBar;
    private TimerInterface timer;

    public ChessBoardView(StatusBarInterface statusBar) {
        this.statusBar = statusBar;
        this.statusBar.reset();
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

    protected void setTimer(TimerInterface timer) {
        this.timer = timer;
        this.timer.getTimeline().setCycleCount(Timeline.INDEFINITE);
        this.timer.getTimeline().play();
    }

    @Override
    public StatusBarInterface getStatusBar() {
        return (statusBar);
    }

    @Override
    public TimerInterface getTimer() {
        return timer;
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
