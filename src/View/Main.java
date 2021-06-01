package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScene.fxml"));
        mainStage.setTitle("3000 Leagues In Search Of Mother Maze Game");
        mainStage.setScene(new Scene(root, 600, 400));
        mainStage.show();
        /*
        MyModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        primaryStage.setTitle("3000 Leagues In Search Of Mother Maze Game");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScene.fxml"));

        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
        */

    }

    public static void main(String[] args) {
        launch(args);
    }
}
