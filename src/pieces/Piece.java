package pieces;

import common.Position;
import game.ChessBoard;
import game.ChessBoardGameInterface;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.ImageManager;

import java.util.List;

/**
 * - firstTime is a boolean to know if the piece is used for the first time
 * - savior is a boolean to know if a the piece can move in a check situation
 */
public abstract class Piece extends LayoutEntity implements PieceInterface {

    private TeamColor teamColor;
    private Position position;
    private boolean firstTime;
    private boolean savior;

    public Piece(TeamColor teamColor, Position position) {
        this.teamColor = teamColor;
        this.position = position;
        this.firstTime = true;
        this.savior = false;
        ImageView imageView = new ImageView();
        if(teamColor == TeamColor.White){
            imageView.setImage(ImageManager.loadImage(this));
        } else {
            imageView.setImage(ImageManager.loadImage(this));
        }
        imageView.fitHeightProperty();
        imageView.fitWidthProperty();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        this.setImageView(imageView);
    }

    @Override
    public TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isFirstTime() {
        return firstTime;
    }

    @Override
    public boolean isSavior() {
        return savior;
    }

    @Override
    public void setSavior(boolean savior) {
        this.savior = savior;
    }

    @Override
    public abstract List<Position> getPossibilities(ChessBoardGameInterface chessBoard);

    @Override
    public void move(ChessBoard chessBoard, int x, int y) {
        chessBoard.setPiece(this.position.getX(), this.position.getY(), null);
        if (!chessBoard.getGameManagement().isCheckState() && this instanceof King) {
            if (((King)this).canCastle(chessBoard) == Castle.ShortBlack) {
                chessBoard.setPiece(x - 1, y, this);
                this.position.setX(x - 1);
                chessBoard.setPiece(5, y, chessBoard.getPiece(7, y));
                chessBoard.getPiece(7, y).getPosition().setX(7);
                chessBoard.setPiece(7, y, null);
            }
            if (((King)this).canCastle(chessBoard) == Castle.LongBlack) {
                chessBoard.setPiece(x + 2, y, this);
                this.position.setX(x + 2);
                chessBoard.setPiece(3, y, chessBoard.getPiece(0, y));
                chessBoard.getPiece(3, y).getPosition().setX(3);
                chessBoard.setPiece(0, y, null);
            }
            if (((King)this).canCastle(chessBoard) == Castle.ShortWhite) {
                chessBoard.setPiece(x - 1, y, this);
                this.position.setX(x - 1);
                chessBoard.setPiece(5, y, chessBoard.getPiece(7, y));
                chessBoard.getPiece(5, y).getPosition().setX(5);
                chessBoard.setPiece(7, y, null);
            }
            if (((King)this).canCastle(chessBoard) == Castle.LongWhite) {
                chessBoard.setPiece(x + 2, y, this);
                this.position.setX(x + 2);
                chessBoard.setPiece(3, y, chessBoard.getPiece(0, y));
                chessBoard.getPiece(3, y).getPosition().setX(3);
                chessBoard.setPiece(0, y, null);
            }
        } else {
            this.position.setX(x);
            this.position.setY(y);
            if (chessBoard.getPiece(x, y) != null)
                chessBoard.getPiece(x, y).capture(chessBoard);
            chessBoard.setPiece(x, y, this);
            if (this instanceof Pawn && ((this.teamColor == TeamColor.White && this.position.getY() == 0) || (this.teamColor == TeamColor.Black && this.position.getY() == 7))) {
                chessBoard.createPromotePiece(this);
            }
        }
        firstTime = false;
    }

    @Override
    public void resetPiece() {
        this.firstTime = true;
        this.savior = false;
    }

    @Override
    public void capture(ChessBoard chessBoard) {
        chessBoard.getChildren().remove(this.getImageView());
    }

}