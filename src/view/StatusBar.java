package view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import pieces.TeamColor;

@SuppressWarnings("WeakerAccess")
public class StatusBar extends HBox implements StatusBarInterface {

	private Button resetButton;
	private Label whitePlayerAlert;
	private Label blackPlayerAlert;
	private Label whitePlayerTimer;
	private Label blackPlayerTimer;
	private Label winner;

	public StatusBar() {
		GridPane statusBarGp = new GridPane();
		resetButton = new Button("Reset");
		whitePlayerAlert = new Label("");
		blackPlayerAlert = new Label("");
		whitePlayerTimer = new Label("");
		blackPlayerTimer = new Label("");
		winner = new Label("");
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(30);
		statusBarGp.getColumnConstraints().add(column);
		column = new ColumnConstraints();
		column.setPercentWidth(30);
		statusBarGp.getColumnConstraints().add(column);
		column = new ColumnConstraints();
		column.setPercentWidth(30);
		statusBarGp.getColumnConstraints().add(column);
		statusBarGp.setPrefSize(2000, 100);
		statusBarGp.getRowConstraints().add(new RowConstraints(35));
		statusBarGp.getRowConstraints().add(new RowConstraints(35));
		statusBarGp.addRow(0, whitePlayerAlert, resetButton, blackPlayerAlert);
		statusBarGp.addRow(1, whitePlayerTimer, winner, blackPlayerTimer);
		for (Node n: statusBarGp.getChildren()) {
			GridPane.setHalignment(n, HPos.CENTER);
			GridPane.setValignment(n, VPos.CENTER);
			if (n instanceof Label) {
				n.setStyle("-fx-font-size: 10pt; -fx-font-weight: bold; -fx-opacity: 1.0;");
			}
		}
		statusBarGp.setVgap(10);
		statusBarGp.setHgap(10);
		statusBarGp.setPadding(new Insets(10, 10, 10, 10));
		statusBarGp.setStyle("-fx-background-color: burlyWood; -fx-effect: innershadow(gaussian, rgba(0,0,0,0.4), 75, 0.5, 0, 10);");
		statusBarGp.setSnapToPixel(false);		
		getChildren().add(statusBarGp);
	}

	public Button getResetButton() {
		return resetButton;
	}

	@Override
	public void alertCheckmate(TeamColor teamColor) {
		if(teamColor == TeamColor.Black) {
			blackPlayerAlert.setText("Black Player is in checkmate");
		} else {
			whitePlayerAlert.setText("White Player is in checkmate");
		}
	}

	@Override
	public void alertCheck(TeamColor teamColor) {
		if(teamColor == TeamColor.Black) {
			blackPlayerAlert.setText("Black Player is in check");
		} else {
			whitePlayerAlert.setText("White Player is in check");
		}
	}

	@Override
	public void alertTurn(TeamColor teamColor) {
		if(teamColor == TeamColor.Black) {
			blackPlayerAlert.setText("Black Player turn");
		} else {
			whitePlayerAlert.setText("White Player turn");
		}
	}

	@Override
	public void alertWinner(TeamColor teamColor) {
		winner.setText(teamColor.name() + " Player won !");
	}

	@Override
	public void alertStalemate() {
		winner.setText("Stalemate !");
	}

	@Override
	public void alertOutOfTime(TeamColor teamColor) {
		if(teamColor == TeamColor.White) {
			whitePlayerAlert.setText("White Player run out of time");
		} else {
			blackPlayerAlert.setText("Black Player run out of time");
		}
	}

	@Override
	public void removeAlert(TeamColor teamColor) {
		if(teamColor == TeamColor.Black) {
			blackPlayerAlert.setText("");
		} else {
			whitePlayerAlert.setText("");
		}
	}

	@Override
	public void resetTimer() {
		whitePlayerTimer.setText("White timer: 15:00");
		blackPlayerTimer.setText("Black timer: 15:00");
	}

	@Override
	public void removeWinner() {
		winner.setText("");
	}

	@Override
	public Label getPlayerTimer(TeamColor teamColor) {
		if(teamColor == TeamColor.White) {
			return whitePlayerTimer;
		} else {
			return blackPlayerTimer;
		}
	}

	@Override
	public void resize(double width, double height){
		super.resize(width, height); 
		setWidth(width);
		setHeight(height);
	}

}