package database;

import entites.*;
import entites.enums.TeamColor;
import javafx.scene.image.ImageView;
import managers.ImageManagerInterface;

public class ChessBoard implements ChessBoardInterface {

    private ImageManagerInterface imageManager;
    private PieceInterface[][] matrix;

    public ChessBoard(ImageManagerInterface imageManager) {
        this.imageManager = imageManager;
    }

    @Override
    public void init() {
        this.matrix = new PieceInterface[8][8];
        matrix[0][0] = new Tower(TeamColor.white, new Position(0,0), new ImageView(imageManager.getTowerImage()));
        matrix[1][0] = new Rider(TeamColor.white, new Position(1,0), new ImageView(imageManager.getRiderImage()));
        matrix[2][0] = new Crazy(TeamColor.white, new Position(2,0), new ImageView(imageManager.getCrazyImage()));
        matrix[3][0] = new Queen(TeamColor.white, new Position(3,0), new ImageView(imageManager.getQueenImage()));
        matrix[4][0] = new King(TeamColor.white, new Position(4,0), new ImageView(imageManager.getKingImage()));
        matrix[5][0] = new Crazy(TeamColor.white, new Position(5,0), new ImageView(imageManager.getCrazyImage()));
        matrix[6][0] = new Rider(TeamColor.white, new Position(6,0), new ImageView(imageManager.getRiderImage()));
        matrix[7][0] = new Tower(TeamColor.white, new Position(7,0), new ImageView(imageManager.getTowerImage()));
        matrix[0][7] = new Tower(TeamColor.black, new Position(0,7), new ImageView(imageManager.getTowerImage()));
        matrix[1][7] = new Rider(TeamColor.black, new Position(1,7), new ImageView(imageManager.getRiderImage()));
        matrix[2][7] = new Crazy(TeamColor.black, new Position(2,7), new ImageView(imageManager.getCrazyImage()));
        matrix[3][7] = new Queen(TeamColor.black, new Position(3,7), new ImageView(imageManager.getQueenImage()));
        matrix[4][7] = new King(TeamColor.black, new Position(4,7), new ImageView(imageManager.getKingImage()));
        matrix[5][7] = new Crazy(TeamColor.black, new Position(5,7), new ImageView(imageManager.getCrazyImage()));
        matrix[6][7] = new Rider(TeamColor.black, new Position(6,7), new ImageView(imageManager.getRiderImage()));
        matrix[7][7] = new Tower(TeamColor.black, new Position(7,7), new ImageView(imageManager.getTowerImage()));
        for(int i = 0 ; i < 8 ; i ++) {
            matrix[i][1] = new Pawn(TeamColor.white, new ImageView(imageManager.getPawnImage()));
            matrix[i][6] = new Pawn(TeamColor.black, new ImageView(imageManager.getPawnImage()));
        }
    }

    @Override
    public Object get(PositionInterface position) {
        return matrix[position.getX()][position.getY()];
    }

    @Override
    public boolean canMove(PositionInterface from, PositionInterface to) {
        Piece piece = (Piece) get(from);
        if(piece instanceof Pawn) {
            return ((Pawn)piece).canGoTo(this, from, to);
        } else {
            return (piece.canGoTo(from, to) && this.canGoTo(piece, to));
        }

    }

    private boolean canGoTo(PieceInterface piece, PositionInterface position) {
        //todo implements this method
        return true;
    }

}
