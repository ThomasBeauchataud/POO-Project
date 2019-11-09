package game;

import common.ConfigReader;
import pieces.TeamColor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import view.StatusBarTimerInterface;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class Timer implements TimerInterface {

	private int whiteTimer;
	private int blackTimer;
	private boolean timeOver;
	private GameManagementTimerInterface gameManagement;
	private StatusBarTimerInterface statusBar;
	private Timeline timeline;
	
	public Timer(GameManagementTimerInterface gameManagement, StatusBarTimerInterface statusBar) {
		this.gameManagement = gameManagement;
		this.statusBar = statusBar;
		blackTimer = ConfigReader.getInt("time");
		whiteTimer = ConfigReader.getInt("time");
		timeOver = false;
		loadTimeLine();
	}

	@Override
	public boolean isTimeOver() {
		return timeOver;
	}

	@Override
	public Timeline getTimeline() {
		return timeline;
	}

	@Override
	public void reset() {
		blackTimer = ConfigReader.getInt("time");
		whiteTimer = ConfigReader.getInt("time");
		timeOver = false;
		timeline.play();
	}

	private void loadTimeLine() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			if (gameManagement.getCurrentPlayer() == TeamColor.White && !timeOver && !gameManagement.isCheckmate() && !gameManagement.isStalemate()) {
				whiteTimer -= 1;
				statusBar.getPlayerTimer(TeamColor.White).setText("White timer: " + TimeUnit.SECONDS.toMinutes(whiteTimer) + ":" + (whiteTimer % 60));
			} else if (gameManagement.getCurrentPlayer() == TeamColor.Black && !timeOver) {
				blackTimer -= 1;
				statusBar.getPlayerTimer(TeamColor.Black).setText("Black timer: " + TimeUnit.SECONDS.toMinutes(blackTimer) + ":" + (blackTimer % 60));
			}
			if (!timeOver && (whiteTimer == 0 || blackTimer == 0)) {
				timeline.stop();
				statusBar.alertOutOfTime(TeamColor.White);
				timeOver = true;
			}
		}));
	}
}
