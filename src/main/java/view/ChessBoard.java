package view;


import pieces.*;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import game.GameLogic;
import view.StatusBar;
import game.Timer;
import view.Window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ChessBoard extends Pane {

	public ChessBoard(StatusBar newStatusBar) {
		// initalize the board: background, data structures, inital layout of
		// pieces
		statusBar = newStatusBar;
		statusBar.whitePlayerAlert.setText("White Player turn");
		statusBar.blackPlayerAlert.setText("");
		statusBar.whitePlayerTimer.setText("White timer: 15:00");
		statusBar.blackPlayerTimer.setText("Black timer: 15:00");

		background = new Rectangle();
		background.setFill(Color.WHITE);

		getChildren().add(background);

		// initialize board array to the correct size
		board = new TeamColor[boardWidth][boardHeight];

		// initialize pieces array to the correct size
		pieces = new Piece[boardWidth][boardHeight];

		// initialize windows array to the correct size
		windows = new Window[boardWidth][boardHeight];

		// for loop to populate all arrays to default values and add the windows
		// to the board
		for (int i = 0; i < 8; i++) {
			if(i%2 == 0 || i ==0){
				isBlack =false;
			}
			else 
				isBlack = true;
			for (int j = 0; j < 8; j++) {
				board[i][j] = null;
				if(isBlack){
					windows[i][j] = new Window(0); //Black 
					isBlack=false;
				}else{
					windows[i][j] = new Window(1); //White
					isBlack = true; 
				}
				getChildren().add(windows[i][j]);
				pieces[i][j] = null;
			}
		}

		// set the current player to white
		current_player = PlayerWhite;
		initPiece();
		timer = new Timer(this);
		timer.timeline.setCycleCount(Timeline.INDEFINITE);
		timer.timeline.play();
		timer.playerTurn = current_player;
	}
		
	public void initPiece()
	{
		// Initialize the pieces and put it on the board
		// BLACK Pieces
		rook_2_1 = new Rook(TeamColor.Black, new Position(0,0));
		knight_2_1 = new Knight(TeamColor.Black, new Position(1, 0));
		bishop_2_1 = new Bishop(TeamColor.Black, new Position(2, 0));
		queen_2 = new Queen(TeamColor.Black, new Position(3, 0));
		king_2 = new King(TeamColor.Black, new Position(4, 0));
		bishop_2_2 = new Bishop(TeamColor.Black, new Position(5, 0));
		knight_2_2 = new Knight(TeamColor.Black, new Position(6, 0));
		rook_2_2 = new Rook(TeamColor.Black, new Position(7, 0));
		pawn_2_1 = new Pawn(TeamColor.Black, new Position(0, 1));
		pawn_2_2 = new Pawn(TeamColor.Black, new Position(1, 1));
		pawn_2_3 = new Pawn(TeamColor.Black, new Position(2, 1));
		pawn_2_4 = new Pawn(TeamColor.Black, new Position(3, 1));
		pawn_2_5 = new Pawn(TeamColor.Black, new Position(4, 1));
		pawn_2_6 = new Pawn(TeamColor.Black, new Position(5, 1));
		pawn_2_7 = new Pawn(TeamColor.Black, new Position(6, 1));
		pawn_2_8 = new Pawn(TeamColor.Black, new Position(7, 1));
		
		//WHITE Pieces
		rook_1_1 = new Rook(TeamColor.White, new Position(0, 7));
		knight_1_1 = new Knight(TeamColor.White, new Position(1, 7));
		bishop_1_1 = new Bishop(TeamColor.White, new Position(2, 7));
		queen_1 = new Queen(TeamColor.White, new Position(3, 7));
		king_1 = new King(TeamColor.White, new Position(4, 7));
		bishop_1_2 = new Bishop(TeamColor.White, new Position(5, 7));
		knight_1_2 = new Knight(TeamColor.White, new Position(6, 7));
		rook_1_2 = new Rook(TeamColor.White, new Position(7, 7));
		pawn_1_1 = new Pawn(TeamColor.White, new Position(0, 6));
		pawn_1_2 = new Pawn(TeamColor.White, new Position(1, 6));
		pawn_1_3 = new Pawn(TeamColor.White, new Position(2, 6));
		pawn_1_4 = new Pawn(TeamColor.White, new Position(3, 6));
		pawn_1_5 = new Pawn(TeamColor.White, new Position(4, 6));
		pawn_1_6 = new Pawn(TeamColor.White, new Position(5, 6));
		pawn_1_7 = new Pawn(TeamColor.White, new Position(6, 6));
		pawn_1_8 = new Pawn(TeamColor.White, new Position(7, 6));
		
		pieces[0][0] = rook_2_1;
		pieces[1][0] = knight_2_1;
		pieces[2][0] = bishop_2_1;
		pieces[3][0] = queen_2;
		pieces[4][0] = king_2;
		pieces[5][0] = bishop_2_2;
		pieces[6][0] = knight_2_2;
		pieces[7][0] = rook_2_2;
		
		pieces[0][1] = pawn_2_1;
		pieces[1][1] = pawn_2_2;
		pieces[2][1] = pawn_2_3;
		pieces[3][1] = pawn_2_4;
		pieces[4][1] = pawn_2_5;
		pieces[5][1] = pawn_2_6;
		pieces[6][1] = pawn_2_7;
		pieces[7][1] = pawn_2_8;
		
		for (int y = 2; y < 6; y++)
		{
			for (int x = 0; x < boardWidth; x++)
			{
				pieces[x][y] = null;
			}
		}
		
		pieces[0][6] = pawn_1_1;
		pieces[1][6] = pawn_1_2;
		pieces[2][6] = pawn_1_3;
		pieces[3][6] = pawn_1_4;
		pieces[4][6] = pawn_1_5;
		pieces[5][6] = pawn_1_6;
		pieces[6][6] = pawn_1_7;
		pieces[7][6] = pawn_1_8;

		pieces[0][7] = rook_1_1;
		pieces[1][7] = knight_1_1;
		pieces[2][7] = bishop_1_1;
		pieces[3][7] = queen_1;
		pieces[4][7] = king_1;
		pieces[5][7] = bishop_1_2;
		pieces[6][7] = knight_1_2;
		pieces[7][7] = rook_1_2;
		
		for (int y = 0; y < boardHeight; y++)
		{
			for (int x = 0; x < boardWidth; x++)
			{
				if (y == 0 || y == 1)
					board[x][y] = TeamColor.Black;
				else if (y == 6 || y == 7)
					board[x][y] = TeamColor.White;
				else
					board[x][y] = null;
			}
		}

		for(int i = 0; i < 8; i++){
			getChildren().addAll(pieces[i][0].getImageView(), pieces[i][1].getImageView(), pieces[i][6].getImageView(), pieces[i][7].getImageView());
		}
	}

	// resize method
	@Override
	public void resize(double width, double height) {
		// call the superclass resize method
		super.resize(width, height);

		// resize the rectangle to take the full size of the widget
		background.setWidth(width);
		background.setHeight(height);

		// calculate the width and height of a cell in which a windows and a
		// piece will sit
		cell_width = width / 8.0;
		cell_height = height / 8.0;

		// nested for loop to reset the sizes and positions of all pieces that
		// were already placed
		// and update the position of the windows in the board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null) {
					pieces[i][j].relocate(i * cell_width, j * cell_height);
					pieces[i][j].resize(cell_width, cell_height);
				}
				windows[i][j].relocate(i * cell_width, j * cell_height);
				windows[i][j].resize(cell_width, cell_height);
			}
		}
	}

	// reset game method
	public void resetGame() {
		timer.playerTurn = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = null;
				if (pieces[i][j] != null)
					getChildren().remove(pieces[i][j].getImageView());
				getChildren().remove(pieces[i][j]);
				pieces[i][j] = null;
			}
		}
		current_player = PlayerWhite;
		initPiece();
		for(int i = 0; i < 8; i++){
			pieces[i][0].resetPiece();
			pieces[i][1].resetPiece();
			pieces[i][6].resetPiece();
			pieces[i][7].resetPiece();
		}
		unhighlightWindow();
		statusBar.whitePlayerAlert.setText("White Player turn");
		statusBar.blackPlayerAlert.setText("");
		statusBar.whitePlayerTimer.setText("White timer: 15:00");
		statusBar.blackPlayerTimer.setText("Black timer: 15:00");
		statusBar.winner.setText("");
		checkmate = false;
		checkState = false;
		stalemate = false;
		selectedPiece = null;
		playerOneRook = 2;
		playerOneBishopLightSquare = 1;
		playerOneBishopDarkSquare = 1;
		playerOneKnight = 2;
		playerOneQueen = 1;
		playerOnePawn = 8;
		playerTwoRook = 2;
		playerTwoBishopLightSquare = 1;
		playerTwoBishopDarkSquare = 1;
		playerTwoKnight = 2;
		playerTwoQueen = 1;
		playerTwoPawn = 8;
		checkPieces.clear();
		saviorPieces.clear();
		timer.timeIsOver = false;
		timer.whiteTimer = 900;
		timer.blackTimer = 900;
		timer.playerTurn = current_player;
		timer.timeline.play();
	}
	
	// select piece method
	public void getPossibilities(final double x, final double y){
		int indexX = (int) (x/ cell_width);
		int indexY = (int) (y/ cell_height);
		
		if (!checkmate && !stalemate && !timer.timeIsOver)
		{
			if (windows[indexX][indexY].isHighlighted())
			{
				movePiece(x, y);
				unhighlightWindow();
				selectedPiece = null;
			}
			else
			{
				//add condition to know if the player is in check
				if(board[indexX][indexY] == current_player){
					unhighlightWindow();
					Piece piece = pieces[indexX][indexY];
					this.colorSquare(piece.getPosition().getX(), piece.getPosition().getY(), true);
					List<Position> positions = piece.getPossibilities(this);
					for(Position position : positions) {
						this.colorSquare(position.getX(), position.getY(), false);
					}
					selectedPiece = pieces[indexX][indexY];
				}
			}
		}
	}
	
	// move piece method
	public void movePiece(final double x, final double y){
		int indexX = (int) (x/ cell_width);
		int indexY = (int) (y/ cell_height);
		
		// add a condition to know if the player can put his piece there
		selectedPiece.MovePiece(this, indexX, indexY);
		// change isFirstTime (pas beau, il faudrait le faire qu'une fois) 
		selectedPiece.setFirstTime(false);
		// don't forget to change the player
		if (current_player == PlayerWhite)
		{
			current_player = PlayerBlack;
			statusBar.whitePlayerAlert.setText("");
			checkState = false;
			for(Iterator<Piece> piece = saviorPieces.iterator(); piece.hasNext(); ) {
			    Piece item = piece.next();
			    item.setSavior(false);
			}
			if (gameLogic.isCheck(this, king_2.getPosition().getX(), king_2.getPosition().getY(), current_player, true))
			{
				checkPieces.clear();
				saviorPieces.clear();
				checkState = true;
				gameLogic.findAllCheckPieces(this, king_2.getPosition().getX(), king_2.getPosition().getY(), current_player);
				if (gameLogic.isCheckmate(this, king_2.getPosition().getX(), king_2.getPosition().getY(), current_player))
				{
					checkmate = true;
					statusBar.blackPlayerAlert.setText("Black player is in checkmate");
					statusBar.winner.setText("White player won !");
				}
				else
					statusBar.blackPlayerAlert.setText("Black player is in check");
			}
			else if (gameLogic.isStalemate(this, king_2, current_player))
				statusBar.winner.setText("Stalemate !");
			else
				statusBar.blackPlayerAlert.setText("Black Player turn");
		}
		else
		{
			current_player = PlayerWhite;
			statusBar.blackPlayerAlert.setText("");
			checkState = false;
			for(Iterator<Piece> piece = saviorPieces.iterator(); piece.hasNext(); ) {
			    Piece item = piece.next();
			    item.setSavior(false);
			}
			if (gameLogic.isCheck(this, king_1.getPosition().getX(), king_1.getPosition().getY(), current_player, true))
			{
				checkPieces.clear();
				saviorPieces.clear();
				checkState = true;
				gameLogic.findAllCheckPieces(this, king_1.getPosition().getX(), king_1.getPosition().getY(), current_player);
				if (gameLogic.isCheckmate(this, king_1.getPosition().getX(), king_1.getPosition().getY(), current_player))
				{
					checkmate = true;
					statusBar.whitePlayerAlert.setText("White player is in checkmate");
					statusBar.winner.setText("Black player won !");
				}
				else
					statusBar.whitePlayerAlert.setText("White player is in check");
			}
			else if (gameLogic.isStalemate(this, king_1, current_player))
				statusBar.winner.setText("Stalemate !");
			else
				statusBar.whitePlayerAlert.setText("White Player turn");
		}
		timer.playerTurn = current_player;
	}
	
	public void createPromotePiece(Piece piece)
	{
		Piece promotedPiece;
		        
		alert = new Alert(AlertType.CONFIRMATION);
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
			if (piece.getTeamColor() == TeamColor.White)
				playerOneRook++;
			else
				playerTwoRook++;
		}
		else if (result.get() == buttonKnight) {
			promotedPiece = new Knight(piece.getTeamColor(), piece.getPosition());
			getChildren().remove(piece.getImageView());
			getChildren().add(promotedPiece.getImageView());
			pieces[piece.getPosition().getX()][piece.getPosition().getY()] = promotedPiece;
			if (piece.getTeamColor() == TeamColor.White)
				playerOneKnight++;
			else
				playerTwoKnight++;
		}
		else if (result.get() == buttonBishop) {
			promotedPiece = new Bishop(piece.getTeamColor(), piece.getPosition());
			getChildren().remove(piece.getImageView());
			getChildren().add(promotedPiece.getImageView());
			pieces[piece.getPosition().getX()][piece.getPosition().getY()] = promotedPiece;
			if (piece.getTeamColor() == TeamColor.White)
			{
				if ((piece.getPosition().getX() + piece.getPosition().getY()) % 2 != 0)
					playerOneBishopDarkSquare++;
				else
					playerOneBishopLightSquare++;
			}
			else
			{
				if ((piece.getPosition().getX() + piece.getPosition().getY()) % 2 == 0)
					playerTwoBishopLightSquare++;
				else
					playerTwoBishopDarkSquare++;
			}
		}
		else if (result.get() == buttonQueen) {
			promotedPiece = new Queen(piece.getTeamColor(), piece.getPosition());
			getChildren().remove(piece.getImageView());
			getChildren().add(promotedPiece.getImageView());
			pieces[piece.getPosition().getX()][piece.getPosition().getY()] = promotedPiece;
			if (piece.getTeamColor() == TeamColor.White)
				playerOneQueen++;
			else
				playerTwoQueen++;
		}
	}
	
	public void colorSquare(Position position, boolean selectedPiece) {
		if (selectedPiece)
			windows[position.getX()][position.getY()].highlightWindow(Color.ORANGE);
		else
			windows[position.getX()][position.getY()].highlightWindow(Color.GREEN);
	}

	public void colorSquare(int x, int y, boolean selectedPiece) {
		if (selectedPiece)
			windows[x][y].highlightWindow(Color.ORANGE);
		else
			windows[x][y].highlightWindow(Color.GREEN);
	}
	
	public void unhighlightWindow()
	{
		for (int y = 0; y < boardHeight; y++)
		{
			for (int x = 0; x < boardWidth; x++)
			{
				if (windows[x][y].getRectangle().getStroke() != null)
					windows[x][y].unhighlight();
			}
		}
	}
	
	public void timerOver(TeamColor playerOutOfTime)
	{
		timer.timeline.stop();
		if (playerOutOfTime == TeamColor.White)
		{
			statusBar.whitePlayerAlert.setText("White player run out of time");
			statusBar.winner.setText("Black player won !");
		}
		else if (playerOutOfTime == TeamColor.Black)
		{
			statusBar.blackPlayerAlert.setText("Black player run out of time");
			statusBar.winner.setText("White player won !");
		}
	}
	
	// Getter and setter method
	
	public Piece getKing(TeamColor teamColor)
	{
		if (teamColor == TeamColor.White)
			return (king_1);
		return (king_2);
	}

	public int getBoardHeight()
	{
		return (this.boardHeight);
	}
	
	public int getBoardWidth()
	{
		return (this.boardWidth);
	}
	
	public TeamColor getBoardPosition(int x, int y)
	{
		return (this.board[x][y]);
	}
	
	public void setBoard(int x, int y, TeamColor teamColor)
	{
		this.board[x][y] = teamColor;
	}
	
	public Piece getPiece(int x, int y)
	{
		return (pieces[x][y]);
	}
	
	public void setPiece(int x, int y, Piece piece)
	{
		this.pieces[x][y] = piece;
	}
	
	public StatusBar getStatusBar()
	{
		return (statusBar);
	}
	
		// private fields
	private int boardWidth = 8;
	private int boardHeight = 8;
	private TeamColor[][] board;

	private Piece[][] pieces;
	private Window[][] windows;

	// pieceName_color_number
	private Rook rook_2_1; 
	private Knight knight_2_1;
	private Bishop bishop_2_1;
	private Queen queen_2; 
	private King king_2; 
	private Bishop bishop_2_2;
	private Knight knight_2_2;
	private Rook rook_2_2;
	private Pawn pawn_2_1;
	private Pawn pawn_2_2;
	private Pawn pawn_2_3;
	private Pawn pawn_2_4;
	private Pawn pawn_2_5;
	private Pawn pawn_2_6;
	private Pawn pawn_2_7;
	private Pawn pawn_2_8;
	
	private Rook rook_1_1; 
	private Knight knight_1_1;
	private Bishop bishop_1_1;
	private Queen queen_1; 
	private King king_1; 
	private Bishop bishop_1_2;
	private Knight knight_1_2;
	private Rook rook_1_2;
	private Pawn pawn_1_1;
	private Pawn pawn_1_2;
	private Pawn pawn_1_3;
	private Pawn pawn_1_4;
	private Pawn pawn_1_5;
	private Pawn pawn_1_6;
	private Pawn pawn_1_7;
	private Pawn pawn_1_8;
		
	private Piece selectedPiece = null;

	private StatusBar statusBar = null;
	
	// GameLogic linked variable
	private GameLogic gameLogic = new GameLogic();
	public List<Piece> checkPieces = new ArrayList<Piece>();
	public List<Piece> saviorPieces = new ArrayList<Piece>();
	public int	playerOneRook = 2;
	public int	playerOneBishopLightSquare = 1;
	public int	playerOneBishopDarkSquare = 1;
	public int	playerOneKnight = 2;
	public int	playerOneQueen = 1;
	public int	playerOnePawn = 8;
	public int	playerTwoRook = 2;
	public int	playerTwoBishopLightSquare = 1;
	public int	playerTwoBishopDarkSquare = 1;
	public int	playerTwoKnight = 2;
	public int	playerTwoQueen = 1;
	public int	playerTwoPawn = 8;
	private Alert alert;
		
	private Rectangle background;
	private double cell_width;
	private double cell_height;
	private TeamColor current_player;
	private boolean isBlack = false; 
	public boolean checkmate = false;
	public boolean checkState = false;
	public boolean stalemate = false;
	
	private final int EMPTY = 0;
	private final TeamColor PlayerWhite = TeamColor.White;
	private final TeamColor PlayerBlack = TeamColor.Black;
	
	private Timer timer;
}
