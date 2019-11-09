package game;

import common.ConfigReader;
import pieces.TeamColor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class Timer {

	private int whiteTimer;
	private int blackTimer;
	private TeamColor playerTurn;
	private boolean timeOver;
	private ChessBoardGameInterface chessboard;
	
	public Timer(ChessBoard chessboard) {
		whiteTimer = ConfigReader.getInt("time");
		blackTimer = ConfigReader.getInt("time");
		this.chessboard = chessboard;
		playerTurn = null;
		timeOver = false;
	}

	public void setPlayerTurn(TeamColor playerTurn) {
		this.playerTurn = playerTurn;
	}

	public boolean isTimeOver() {
		return timeOver;
	}

	public void setTimeOver(boolean timeOver) {
		this.timeOver = timeOver;
	}

	public void reset() {
		whiteTimer = ConfigReader.getInt("time");
		blackTimer = ConfigReader.getInt("time");
	}

	public Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			if (playerTurn == TeamColor.White && !timeOver && !chessboard.getGameManagement().isCheckmate() && !chessboard.getGameManagement().isStalemate()) {
				whiteTimer -= 1;
				chessboard.getStatusBar().getPlayerTimer(TeamColor.White).setText("White timer: " + TimeUnit.SECONDS.toMinutes(whiteTimer) + ":" + (whiteTimer % 60));
			}
			else if (playerTurn == TeamColor.Black && !timeOver) {
				blackTimer -= 1;
				chessboard.getStatusBar().getPlayerTimer(TeamColor.Black).setText("Black timer: " + TimeUnit.SECONDS.toMinutes(blackTimer) + ":" + (blackTimer % 60));
			}
			if (!timeOver && (whiteTimer == 0 || blackTimer == 0)) {
				chessboard.timerOver(playerTurn);
				timeOver = true;
			}
		}
	}));
}
