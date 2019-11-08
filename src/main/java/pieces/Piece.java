package pieces;

import view.ChessBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Translate;
import game.GameLogic;
import view.ImageManager;

import java.util.List;

public abstract class Piece extends LayoutEntity {

    // Piece can be either white (1) or black (2)
    private TeamColor teamColor;
    private Position position;
    protected Translate pos;
    // GameLogic
    protected GameLogic gameLogic = new GameLogic();
    // True if it's the first time that the Piece is used.
    private boolean firstTime;
    // Variable to know if the piece can move in a check situation
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

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isSavior() {
        return savior;
    }

    public void setSavior(boolean savior) {
        this.savior = savior;
    }

    // Select method: When a piece is selected by a first click
    // Highlight all the available position where the piece can go
    public abstract List<Position> getPossibilities(ChessBoard board);

    // Move method: When a piece is already selected and that the player click on a highlighted position
    // Change the position of the piece and update the board
    public void MovePiece(ChessBoard chessBoard, int x, int y) {
        chessBoard.setBoard(this.position.getX(), this.position.getY(), null);
        chessBoard.setPiece(this.position.getX(), this.position.getY(), null);
        if (!chessBoard.checkState && this.canCastle(chessBoard) != null) {
            if (this.canCastle(chessBoard) == Castle.ShortBlack) {
                chessBoard.setBoard(x - 1, y, this.teamColor);
                chessBoard.setPiece(x - 1, y, this);
                this.position.setX(x - 1);
                chessBoard.setBoard(5, y, chessBoard.getPiece(7, y).getTeamColor());
                chessBoard.setPiece(5, y, chessBoard.getPiece(7, y));
                chessBoard.getPiece(7, y).getPosition().setX(7);
                chessBoard.setBoard(7, y, null);
                chessBoard.setPiece(7, y, null);
            }
            if (this.canCastle(chessBoard) == Castle.LongBlack) {
                chessBoard.setBoard(x + 2, y, this.teamColor);
                chessBoard.setPiece(x + 2, y, this);
                this.position.setX(x + 2);
                chessBoard.setBoard(3, y, chessBoard.getPiece(0, y).getTeamColor());
                chessBoard.setPiece(3, y, chessBoard.getPiece(0, y));
                chessBoard.getPiece(3, y).getPosition().setX(3);
                chessBoard.setBoard(0, y, null);
                chessBoard.setPiece(0, y, null);
            }
            if (this.canCastle(chessBoard) == Castle.ShortWhite) {
                chessBoard.setBoard(x - 1, y, this.teamColor);
                chessBoard.setPiece(x - 1, y, this);
                this.position.setX(x - 1);
                chessBoard.setBoard(5, y, chessBoard.getPiece(7, y).getTeamColor());
                chessBoard.setPiece(5, y, chessBoard.getPiece(7, y));
                chessBoard.getPiece(5, y).getPosition().setX(5);
                chessBoard.setBoard(7, y, null);
                chessBoard.setPiece(7, y, null);
            }
            if (this.canCastle(chessBoard) == Castle.LongWhite) {
                chessBoard.setBoard(x + 2, y, this.teamColor);
                chessBoard.setPiece(x + 2, y, this);
                this.position.setX(x + 2);
                chessBoard.setBoard(3, y, chessBoard.getPiece(0, y).getTeamColor());
                chessBoard.setPiece(3, y, chessBoard.getPiece(0, y));
                chessBoard.getPiece(3, y).getPosition().setX(3);
                chessBoard.setBoard(0, y, null);
                chessBoard.setPiece(0, y, null);
            }
        } else {
            this.position.setX(x);
            this.position.setY(y);
            if (chessBoard.getPiece(x, y) != null)
                chessBoard.getPiece(x, y).capture(chessBoard);
            chessBoard.setBoard(x, y, this.teamColor);
            chessBoard.setPiece(x, y, this);
            if (this instanceof Pawn && ((this.teamColor == TeamColor.White && this.position.getY() == 0) || (this.teamColor == TeamColor.Black && this.position.getY() == 7))) {
                chessBoard.createPromotePiece(this);
                if (this.teamColor == TeamColor.White)
                    chessBoard.playerOnePawn--;
                else
                    chessBoard.playerTwoPawn--;
            }
        }
    }

    public void centerImage() {
        ImageView imageView = this.getImageView();
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);
            this.setImageView(imageView);

        }
    }

    // Capture method: When a piece is captured by another one
    public void capture(ChessBoard chessBoard) {
        if (this.teamColor == TeamColor.White) {
            if (this instanceof Rook)
                chessBoard.playerOneRook--;
            else if (this instanceof Knight)
                chessBoard.playerOneKnight--;
            else if (this instanceof Queen)
                chessBoard.playerOneQueen--;
            else if (this instanceof Pawn)
                chessBoard.playerOnePawn--;
            else if (this instanceof Bishop && (this.position.getX() + this.position.getY()) % 2 != 0)
                chessBoard.playerOneBishopDarkSquare--;
            else if (this instanceof Bishop && (this.position.getX() + this.position.getY()) % 2 == 0)
                chessBoard.playerOneBishopLightSquare--;
        } else {
            if (this instanceof Rook)
                chessBoard.playerTwoRook--;
            else if (this instanceof Knight)
                chessBoard.playerTwoKnight--;
            else if (this instanceof Queen)
                chessBoard.playerTwoQueen--;
            else if (this instanceof Pawn)
                chessBoard.playerTwoPawn--;
            else if (this instanceof Bishop && (this.position.getX() + this.position.getY()) % 2 == 0)
                chessBoard.playerTwoBishopLightSquare--;
            else if (this instanceof Bishop && (this.position.getX() + this.position.getY()) % 2 != 0)
                chessBoard.playerTwoBishopDarkSquare--;
        }
        chessBoard.getChildren().remove(this.getImageView());
    }

    public Castle canCastle(ChessBoard chessBoard) {
        return null;
    }

    public void resize(double width, double height) {
        ImageView imageView = this.getImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        this.setImageView(imageView);
    }

    // overridden version of the relocate method
    public void relocate(double x, double y) {
        ImageView imageView = this.getImageView();
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        this.setImageView(imageView);
        centerImage();
    }

    public void resetPiece() {
        this.firstTime = true;
        this.savior = false;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }


}