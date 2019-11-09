import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.CustomControl;

public class Main extends Application {

	private StackPane stackPane;

	@Override
	public void init() {
		stackPane = new StackPane();
		CustomControl customControl = new CustomControl();
		stackPane.getChildren().add(customControl);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Chess game");
		primaryStage.setScene(new Scene(stackPane, 600, 700));
		primaryStage.setMinWidth(300);
		primaryStage.setMinHeight(300);
		primaryStage.show();
	}

	@Override
	public void stop() { }

	public static void main(String[] args) {
			launch(args);
	}

}