package game;

import pieces.PieceInterface;
import pieces.TeamColor;
import view.StatusBar;

/**
 * Manage the ChessBoard Game
 */
public interface ChessBoardGameInterface {

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

    //TODO Remove this
    void colorSquare(int x, int y, boolean bool);

    /**
     * Return the King of a player identified by his TeamColor
     * @param teamColor TeamColor
     * @return PieceInterface
     */
    PieceInterface getKing(TeamColor teamColor);

    /**
     * Return the GameManagement
     * @return GameManagementInterface
     */
    GameManagementInterface getGameManagement();

    /**
     * Return the StatusBar
     * @return StatusBar
     */
    StatusBar getStatusBar();

    /**
     * Set the time over status for a player
     * @param playerOutOfTime TeamColor
     */
    void timerOver(TeamColor playerOutOfTime);

}
