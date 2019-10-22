package entites;

import entites.enums.TeamColor;

public interface PieceInterface extends LayoutEntityInterface {

    TeamColor getTeamColor();

    void setTeamColor(TeamColor teamColor);

    /**
     * Return true if a piece can go from the Position from to the Position to
     * @param from PositionInterface
     * @param to PositionInterface
     * @return boolean
     */
    boolean canGoTo(PositionInterface from, PositionInterface to);

}
