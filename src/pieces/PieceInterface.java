package pieces;

import common.Position;
import game.ChessBoard;
import game.ChessBoardGameInterface;
import game.ChessBoardPieceInterface;

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
     * Move the Piece to a Position
     * @param chessBoard ChessBoardPieceInterface
     * @param x int
     * @param y int
     */
    void move(ChessBoardPieceInterface chessBoard, int x, int y);

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
     * @param chessBoard ChessBoardPieceInterface
     */
    void capture(ChessBoardPieceInterface chessBoard);

    /**
     * Return the list of possibles position of a Piece
     * @param chessBoardSimpleInterface ChessBoardGameInterface
     * @return Position[]
     */
    List<Position> getPossibilities(ChessBoardGameInterface chessBoardSimpleInterface);

}
