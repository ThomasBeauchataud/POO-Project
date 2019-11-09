package view;

import javafx.scene.control.Label;
import pieces.TeamColor;

/**
 * Handle the status bar management for Timer range
 */
public interface StatusBarTimerInterface {

    /**
     * Return the label of the timer of a player
     * @param teamColor TeamColor
     * @return Label
     */
    Label getPlayerTimer(TeamColor teamColor);

    /**
     * Modify the alert of a player for an out of time situation and create the winner message
     * @param teamColor TeamColor
     */
    void alertOutOfTime(TeamColor teamColor);

}
