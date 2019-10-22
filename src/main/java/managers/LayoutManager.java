package managers;

import database.ChessBoardInterface;

import entites.Piece;
import entites.Position;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class LayoutManager implements LayoutManagerInterface {

    private static final int chessboardSize = ConfigManager.getInt("chessboardSize");
    private static final int caseSize = ConfigManager.getInt("caseSize");
    private static final int homeSize = ConfigManager.getInt("homeSize");

    private Stage primaryStage;
    private ChessBoardInterface chessBoard;

    public LayoutManager(Stage primaryStage, ChessBoardInterface chessBoard) {
        this.primaryStage = primaryStage;
        this.chessBoard = chessBoard;
    }

    @Override
    public void load() {
        loadHomeScene();
    }

    private void loadHomeScene() {
        Pane root = new Pane();

        Button playButton = new Button();
        playButton.setOnAction(event -> loadPlayScene());

        root.getChildren().add(playButton);

        primaryStage.setScene(new Scene(root, homeSize, homeSize));
    }

    private void loadPlayScene() {
        GridPane root = new GridPane();
        chessBoard.init();

        for (int i = 0; i < chessboardSize; i++) {
            ColumnConstraints column = new ColumnConstraints(caseSize);
            RowConstraints row = new RowConstraints(caseSize);
            root.getColumnConstraints().add(column);
            root.getRowConstraints().add(row);
        }

        ChessBoardScene scene = new ChessBoardScene(root, chessboardSize, chessboardSize);

        //todo display pieces

        scene.setOnMouseClicked(mouseEvent -> {
            Position position = this.getPosition(mouseEvent.getX(), mouseEvent.getY());
            if(isAPiece(position)) {
                scene.setSelected(position);
            } else {
                if (scene.isClicked()) {
                    if(chessBoard.canMove(scene.getSelected(), position)) {

                    }
                }
            }
        });

        primaryStage.setScene(scene);
    }

    private Position getPosition(double x, double y) {
        return new Position((int) x*8/chessboardSize, (int) y*8/chessboardSize);
    }

    private boolean isAPiece(Position position) {
        Object object = chessBoard.get(position);
        return object instanceof Piece;
    }

}
