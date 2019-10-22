package entites;

import entites.enums.TeamColor;
import javafx.scene.image.ImageView;

public abstract class Piece extends LayoutEntity implements PieceInterface {

    private TeamColor teamColor;

    public Piece(TeamColor teamColor, ImageView imageView) {
        super(imageView);
        this.teamColor = teamColor;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(TeamColor teamColor) {
        this.teamColor = teamColor;
    }

}
