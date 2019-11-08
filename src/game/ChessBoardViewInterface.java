package game;

/**
 * Manage the ChessBoard View
 */
public interface ChessBoardViewInterface {

    /**
     * Translate the ChessBoard
     * @param y double
     */
    void setTranslateY(double y);

    /**
     * Resize the ChessBoard
     * @param width double
     * @param height double
     */
    void resize(double width, double height);

    /**
     * Reset the Game
     */
    void resetGame();

    /**
     * Select a Piece at specific coordinates
     * @param x double
     * @param y double
     */
    void selectPiece(double x, double y);

}
