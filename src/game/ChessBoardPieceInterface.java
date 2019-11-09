package game;

import pieces.PieceInterface;
import pieces.TeamColor;

/**
 * Handle ChessBoard manage at Piece range
 */
public interface ChessBoardPieceInterface {

    /**
     * Return the TeamColor of the Piece at a specific position
     * Return null if there is no Piece
     * @param x int
     * @param y int
     * @return TeamColor
     */
    TeamColor getBoardPosition(int x, int y);

    /**
     * Return a Piece from coordinates
     * Return null if there is no Piece
     * @param x int
     * @param y int
     * @return PieceInterface
     */
    PieceInterface getPiece(int x, int y);

    /**
     * Set a piece on the chessboard at specific position
     * @param x int
     * @param y int
     * @param piece PieceInterface
     */
    void setPiece(int x, int y, PieceInterface piece);

    /**
     * Color some square due to king castle possibilities
     * @param x int
     * @param y int
     * @param bool boolean
     */
    void colorSquare(int x, int y, boolean bool);

    /**
     * Return the GameManagement
     * @return GameManagementInterface
     */
    GameManagementInterface getGameManagement();

    /**
     * Create a promote piece
     * @param piece PieceInterface
     */
    void createPromotePiece(PieceInterface piece);

    /**
     * Remove a Piece view of the chessboard when its capture
     * @param piece PieceInterface
     */
    void removePieceView(PieceInterface piece);

}
