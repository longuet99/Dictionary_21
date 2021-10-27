package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root = loader.load();

            Controller controller = loader.getController();

            Scene searchScene = new Scene(root);

            primaryStage.setTitle("DICTIONARY - 2021");
            primaryStage.setScene(searchScene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}