import database.ClassDatabase;
import database.DatabaseInterface;
import javafx.application.Application;
import javafx.stage.Stage;
import managers.ImageManager;
import managers.ImageManagerInterface;
import managers.LayoutManager;
import managers.LayoutManagerInterface;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        DatabaseInterface database = new ClassDatabase();
        ImageManagerInterface imageManager = new ImageManager();
        LayoutManagerInterface layoutManager = new LayoutManager(database, imageManager);
        layoutManager.load(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
