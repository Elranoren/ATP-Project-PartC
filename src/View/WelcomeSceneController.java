package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeSceneController {
    public void openMyView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Game");
            stage.show();
            Main.mainStage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        /*
        Stage stage = new Stage();
        stage.setTitle("Game");
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
            Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
            Scene newScene = new Scene(root,rectangle2D.getWidth()-stage.getWidth(), rectangle2D.getHeight()-stage.getHeight());
            newScene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
            stage.setScene(newScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }
}
