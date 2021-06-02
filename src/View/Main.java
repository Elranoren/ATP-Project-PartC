package View;

import Model.MyModel;
import Server.Configurations;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WelcomeScene.fxml"));
        Parent root = fxmlLoader.load();
        WelcomeSceneController view = fxmlLoader.getController();
        Scene scene = new Scene(root, 500, 500);
        mainStage.setTitle("3000 Leagues In Search Of Mother Maze Game");
        mainStage.setScene(scene);
        view.setSizeOfScene(scene);
        mainStage.show();
        /////////////
//        MyModel model = new MyModel();
//        MyViewModel viewModel = new MyViewModel(model);
//        model.addObserver(viewModel);
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
