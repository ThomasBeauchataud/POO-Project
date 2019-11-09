package game;


import common.Position;
import pieces.*;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.StatusBar;
import view.StatusBarInterface;
import view.Window;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public class ChessBoard extends Pane implements ChessBoardGameInterface, ChessBoardViewInterface {

	public static int boardSize = 8;

	private GameManagementInterface gameManagement = new GameManagement();
	private PieceInterface[][] pieces;
	private Window[][] windows;
	private StatusBarInterface statusBar;
	private Rectangle background;
	private double cell_width;
	private double cell_height;
	private Timer timer;

	public ChessBoard(StatusBar newStatusBar) {
		statusBar = newStatusBar;
		statusBar.alertTurn(TeamColor.White);
		statusBar.removeAlert(TeamColor.Black);
		statusBar.resetTimer();
		background = new Rectangle();
		background.setFill(Color.WHITE);
		getChildren().add(background);
		pieces = new Piece[boardSize][boardSize];
		windows = new Window[boardSize][boardSize];
		for (int i = 0; i < 8; i++) {
			boolean isBlack;
			isBlack = i % 2 != 0;
			for (int j = 0; j < 8; j++) {
				if(isBlack) {
					windows[i][j] = new Window(0);
					isBlack = false;
				} else {
					windows[i][j] = new Window(1);
					isBlack = true; 
				}
				getChildren().add(windows[i][j]);
				pieces[i][j] = null;
			}
		}
		gameManagement.setCurrentPlayer(TeamColor.White);
		initPiece();
		Timer timer = new Timer(this);
		timer.timeline.setCycleCount(Timeline.INDEFINITE);
		timer.timeline.play();
		this.timer = timer;
		this.timer.setPlayerTurn(gameManagement.getCurrentPlayer());
	}

	@Override
	public GameManagementInterface getGameManagement() {
		return gameManagement;
	}

	@Override
	public StatusBarInterface getStatusBar() {
		return (statusBar);
	}

	public Timer getTimer() {
		return timer;
	}

	@Override
	public Piece getKing(TeamColor teamColor) {
		for(PieceInterface[] pieces : this.pieces) {
			for(PieceInterface piece : pieces) {
				if(piece instanceof King && piece.getTeamColor() == teamColor) {
					return (King)piece;
				}
			}
		}
		return null;
	}

	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		background.setWidth(width);
		background.setHeight(height);
		cell_width = width / 8.0;
		cell_height = height / 8.0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieces[i][j] != null) {
					pieces[i][j].relocate(i * cell_width, j * cell_height);
					pieces[i][j].resize(cell_width, cell_height);
				}
				windows[i][j].relocate(i * cell_width, j * cell_height);
				windows[i][j].resize(cell_width, cell_height);
			}
		}
	}

	@Override
	public void resetGame() {
		timer.setPlayerTurn(null);
		for(PieceInterface[] pieces : this.pieces) {
			for(PieceInterface piece : pieces) {
				if(piece != null) {
					getChildren().remove(piece.getImageView());
				}
			}
		}
		this.initPiece();
		statusBar.alertTurn(TeamColor.White);
		statusBar.removeAlert(TeamColor.Black);
		statusBar.removeWinner();
		timer.setTimeOver(false);
		gameManagement.resetGame();
		timer.setPlayerTurn(gameManagement.getCurrentPlayer());
		timer.timeline.play();
		timer.reset();
		unhighlightWindow();
	}
	
	@Override
	public void selectPiece(final double x, final double y){
		int indexX = (int) (x/ cell_width);
		int indexY = (int) (y/ cell_height);
		if (!gameManagement.isCheckmate() && !gameManagement.isStalemate() && !timer.isTimeOver()) {
			if (windows[indexX][indexY].isHighlighted()) {
				movePiece(x, y);
				unhighlightWindow();
				gameManagement.setSelectedPiece(null);
			}
			else {
				if(pieces[indexX][indexY] != null && pieces[indexX][indexY].getTeamColor() == gameManagement.getCurrentPlayer()){
					unhighlightWindow();
					PieceInterface piece = pieces[indexX][indexY];
					this.colorSquare(piece.getPosition().getX(), piece.getPosition().getY(), true);
					List<Position> positions = piece.getPossibilities(this);
					for(Position position : positions) {
						this.colorSquare(position.getX(), position.getY(), false);
					}
					gameManagement.setSelectedPiece(pieces[indexX][indexY]);
				}
			}
		}
	}

	public void movePiece(final double x, final double y){
		int indexX = (int) (x/ cell_width);
		int indexY = (int) (y/ cell_height);
		gameManagement.getSelectedPiece().move(this, indexX, indexY);
	}
	
	public void createPromotePiece(Piece piece) {
		Piece promotedPiece;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Promote a piece");
		alert.setHeaderText("You can promote your pawn into another piece");
		alert.setContentText("Choose one of the following piece");
		ButtonType buttonRook = new ButtonType("Rook");
		ButtonType buttonKnight = new ButtonType("Knight");
		ButtonType buttonBishop = new ButtonType("Bishop");
		ButtonType buttonQueen = new ButtonType("Queen");
		alert.getButtonTypes().setAll(buttonRook, buttonKnight, buttonBishop, buttonQueen);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonRook){
			promotedPiece = new Rook(piece.getTeamColor(), piece.getPosition());
			getChildren().remove(piece.getImageView());
			getChildren().add(promotedPiece.getImageView());
			pieces[piece.getPosition().getX()][piece.getPosition().getY()] = promotedPiece;
		}
		else if (result.get() == buttonKnight) {
			promotedPiece = new Knight(piece.getTeamColor(), piece.getPosition());
			getChildren().remove(piece.getImageView());
			getChildren().add(promotedPiece.getImageView());
			pieces[piece.getPosition().getX()][piece.getPosition().getY()] = promotedPiece;
		}
		else if (result.get() == buttonBishop) {
			promotedPiece = new Bishop(piece.getTeamColor(), piece.getPosition());
			getChildren().remove(piece.getImageView());
			getChildren().add(promotedPiece.getImageView());
			pieces[piece.getPosition().getX()][piece.getPosition().getY()] = promotedPiece;
		}
		else if (result.get() == buttonQueen) {
			promotedPiece = new Queen(piece.getTeamColor(), piece.getPosition());
			getChildren().remove(piece.getImageView());
			getChildren().add(promotedPiece.getImageView());
			pieces[piece.getPosition().getX()][piece.getPosition().getY()] = promotedPiece;
		}
	}

	public void colorSquare(int x, int y, boolean selectedPiece) {
		if (selectedPiece)
			windows[x][y].highlightWindow(Color.ORANGE);
		else
			windows[x][y].highlightWindow(Color.GREEN);
	}

	@Override
	public void timerOver(TeamColor playerOutOfTime) {
		timer.timeline.stop();
		statusBar.alertOutOfTime(TeamColor.White);
		statusBar.alertWinner(GameLogic.getEnemyTeamColor(playerOutOfTime));
	}

	@Override
	public TeamColor getBoardPosition(int x, int y)
	{ if(pieces[x][y] == null) {
			return null;
		}
		return (pieces[x][y].getTeamColor());
	}

	@Override
	public PieceInterface getPiece(int x, int y) {
		return (pieces[x][y]);
	}
	
	public void setPiece(int x, int y, PieceInterface piece) {
		this.pieces[x][y] = piece;
	}

    private void unhighlightWindow() {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (windows[x][y].getRectangle().getStroke() != null) {
                    windows[x][y].unhighlight();
                }
            }
        }
    }

	private void initPiece() {
		pieces[0][0] = new Rook(TeamColor.Black, new Position(0,0));
		pieces[1][0] = new Knight(TeamColor.Black, new Position(1, 0));
		pieces[2][0] = new Bishop(TeamColor.Black, new Position(2, 0));
		pieces[3][0] = new Queen(TeamColor.Black, new Position(3, 0));
		pieces[4][0] = new King(TeamColor.Black, new Position(4, 0));
		pieces[5][0] = new Bishop(TeamColor.Black, new Position(5, 0));
		pieces[6][0] = new Knight(TeamColor.Black, new Position(6, 0));
		pieces[7][0] = new Rook(TeamColor.Black, new Position(7, 0));
		pieces[0][1] = new Pawn(TeamColor.Black, new Position(0, 1));
		pieces[1][1] = new Pawn(TeamColor.Black, new Position(1, 1));
		pieces[2][1] = new Pawn(TeamColor.Black, new Position(2, 1));
		pieces[3][1] = new Pawn(TeamColor.Black, new Position(3, 1));
		pieces[4][1] = new Pawn(TeamColor.Black, new Position(4, 1));
		pieces[5][1] = new Pawn(TeamColor.Black, new Position(5, 1));
		pieces[6][1] = new Pawn(TeamColor.Black, new Position(6, 1));
		pieces[7][1] = new Pawn(TeamColor.Black, new Position(7, 1));

		pieces[0][6] = new Pawn(TeamColor.White, new Position(0, 6));
		pieces[1][6] = new Pawn(TeamColor.White, new Position(1, 6));
		pieces[2][6] = new Pawn(TeamColor.White, new Position(2, 6));
		pieces[3][6] = new Pawn(TeamColor.White, new Position(3, 6));
		pieces[4][6] = new Pawn(TeamColor.White, new Position(4, 6));
		pieces[5][6] = new Pawn(TeamColor.White, new Position(5, 6));
		pieces[6][6] = new Pawn(TeamColor.White, new Position(6, 6));
		pieces[7][6] = new Pawn(TeamColor.White, new Position(7, 6));
		pieces[0][7] = new Rook(TeamColor.White, new Position(0, 7));
		pieces[1][7] = new Knight(TeamColor.White, new Position(1, 7));
		pieces[2][7] = new Bishop(TeamColor.White, new Position(2, 7));
		pieces[3][7] = new Queen(TeamColor.White, new Position(3, 7));
		pieces[4][7] = new King(TeamColor.White, new Position(4, 7));
		pieces[5][7] = new Bishop(TeamColor.White, new Position(5, 7));
		pieces[6][7] = new Knight(TeamColor.White, new Position(6, 7));
		pieces[7][7] = new Rook(TeamColor.White, new Position(7, 7));
		for (int y = 2; y < 6; y++) {
			for (int x = 0; x < boardSize; x++) {
				pieces[x][y] = null;
			}
		}
		for(int i = 0; i < 8; i++){
			getChildren().addAll(pieces[i][0].getImageView(), pieces[i][1].getImageView(), pieces[i][6].getImageView(), pieces[i][7].getImageView());
		}
	}
	
}
