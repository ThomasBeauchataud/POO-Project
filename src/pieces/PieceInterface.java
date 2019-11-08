package pieces;

import common.Position;
import game.ChessBoard;
import game.ChessBoardGameInterface;

import java.util.List;

/**
 * Manage Piece Entities
 */
public interface PieceInterface extends LayoutEntityInterface {

    /**
     * Return the TeamColor of the Piece
     * @return TeamColor
     */
    TeamColor getTeamColor();

    /**
     * Return the Position of the Piece
     * @return Position
     */
    Position getPosition();

    /**
     * Reset the Piece attributes
     */
    void resetPiece();

    /**
     * Move the Piece to a Position
     * @param chessBoard ChessBoard
     * @param x int
     * @param y int
     */
    void move(ChessBoard chessBoard, int x, int y);

    /**
     * Return if the piece has already moved or not
     * @return boolean
     */
    boolean isFirstTime();

    /**
     * Set if the piece if a savior of a check situation
     * @param savior boolean
     */
    void setSavior(boolean savior);

    /**
     * Return if the piece if a savior of a check situation
     * @return boolean
     */
    boolean isSavior();

    /**
     * Capture a Piece a the same position of the ChessBoard
     * @param chessBoard ChessBoard
     */
    void capture(ChessBoard chessBoard);

    /**
     * Return the list of possibles position of a Piece
     * @param chessBoardSimpleInterface ChessBoardGameInterface
     * @return Position[]
     */
    List<Position> getPossibilities(ChessBoardGameInterface chessBoardSimpleInterface);

}
