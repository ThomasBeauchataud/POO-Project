package view;

import javafx.scene.control.Label;
import pieces.TeamColor;

//TODO Comment
public interface StatusBarInterface {

    void alertCheckmate(TeamColor teamColor);

    void alertCheck(TeamColor teamColor);

    void alertTurn(TeamColor teamColor);

    void alertWinner(TeamColor teamColor);

    void alertStalemate();

    void alertOutOfTime(TeamColor teamColor);

    void removeAlert(TeamColor teamColor);

    void resetTimer();

    void removeWinner();

    Label getPlayerTimer(TeamColor teamColor);
    
}
