package game;

import pieces.PieceInterface;
import pieces.TeamColor;
import view.StatusBarInterface;

/**
 * Manage the ChessBoard Game
 */
public interface ChessBoardGameInterface extends ChessBoardPieceInterface {

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
     * @return StatusBarInterface
     */
    StatusBarInterface getStatusBar();

    /**
     * Return the Timer
     * @return TimerInterface
     */
    TimerInterface getTimer();

}
