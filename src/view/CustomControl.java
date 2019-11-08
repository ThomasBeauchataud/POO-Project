package view;

import game.ChessBoard;
import game.ChessBoardViewInterface;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;

public class CustomControl extends Control {

	//region Attributes

	private ChessBoardViewInterface chessBoard;
	private StatusBar statusBar;
	private int statusBarSize = 100;

	//endregion

	//region Constructor

	/**
	 * CustomControl Constructor
	 * Complex mouse interactions and key interactions
	 */
	public CustomControl() {
		setSkin(new CustomControlSkin(this));
		statusBar = new StatusBar();
		ChessBoard chessBoard = new ChessBoard(statusBar);
		getChildren().addAll(statusBar, chessBoard);
		this.chessBoard = chessBoard;
		setOnMouseClicked(event -> {
			int halfSize = statusBarSize / 2;
			this.chessBoard.selectPiece(event.getX(), event.getY() - (halfSize));
		});
		setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.SPACE) {
				this.chessBoard.resetGame();
			}
		});
		statusBar.getResetButton().setOnAction(event -> this.chessBoard.resetGame());
		
	}

	//endregion

	//region Public Methods

	@Override
	public void resize(double width, double height){
		super.resize(width, height - statusBarSize);
		int halfSize = statusBarSize / 2;
		this.chessBoard.setTranslateY(halfSize);
		this.chessBoard.resize(width, height - statusBarSize);
		statusBar.resize(width, statusBarSize);
		statusBar.setTranslateY(-(halfSize));
	}

	//endregion

}
