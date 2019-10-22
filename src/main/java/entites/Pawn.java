package entites;

import database.ChessBoardInterface;
import entites.enums.TeamColor;
import javafx.scene.image.ImageView;

import static java.lang.Math.abs;

public class Pawn extends Piece {

    public Pawn(TeamColor teamColor, ImageView imageView) {
        super(teamColor, imageView);
    }

    @Override
    public boolean canGoTo(PositionInterface from, PositionInterface to) {
        if(this.getTeamColor() == TeamColor.white) {
            return (from.getX() == to.getX() && to.getY()-from.getY() <= 2 && to.getY()-from.getY() >= 1);
        }
        return (from.getX() == to.getX() && to.getY()-from.getY() >= -2 && to.getY()-from.getY() <= -1);
    }

    public boolean canGoTo(ChessBoardInterface chessBoard, PositionInterface from, PositionInterface to) {
        if(canGoTo(from, to)) {
            return true;
        }
        if(this.getTeamColor() == TeamColor.white) {
            return (from.getY()+1 == to.getY() && abs(from.getX()-to.getX()) == 1 && chessBoard.get(to) instanceof Piece);
        }
        return (from.getY()-1 == to.getY() && abs(from.getX()-to.getX()) == 1 && chessBoard.get(to) instanceof Piece);
    }

}
