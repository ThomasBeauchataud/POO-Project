package game;

import pieces.TeamColor;

/**
 * Manage Game Situation for Timer range
 */
public interface GameManagementTimerInterface {

    /**
     * Get the current player TeamColor
     * @return TeamColor
     */
    TeamColor getCurrentPlayer();

    /**
     * Return if the actual player can move
     * @return boolean
     */
    boolean isStalemate();

    /**
     * Return true if there is a Checkmate
     * @return boolean
     */
    boolean isCheckmate();

}
