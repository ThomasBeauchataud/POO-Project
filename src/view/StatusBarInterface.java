package view;

import pieces.TeamColor;

/**
 * Handle the status bar management
 */
public interface StatusBarInterface extends StatusBarTimerInterface {

    /**
     * Modify the alert of a player for a checkmate situation and create the winner message
     * @param teamColor TeamColor
     */
    void alertCheckmate(TeamColor teamColor);

    /**
     * Modify the alert of a player for a check situation
     * @param teamColor TeamColor
     */
    void alertCheck(TeamColor teamColor);

    /**
     * Modify the alert of a player for his turn to play
     * @param teamColor TeamColor
     */
    void alertTurn(TeamColor teamColor);

    /**
     * Modify the winner alert for a stalemate situation
     */
    void alertStalemate();

    /**
     * Modify the alert of a player by removing it
     * @param teamColor TeamColor
     */
    void removeAlert(TeamColor teamColor);

    /**
     * Reset the status bar for a new game
     */
    void reset();
    
}
