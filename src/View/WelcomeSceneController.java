package View;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeSceneController implements Initializable {
    public  javafx.scene.control.Button startButton;
    public  javafx.scene.control.Button settingsButton;

    public  javafx.scene.layout.AnchorPane myPane;
    public  javafx.scene.image.ImageView marko;
    private DoubleProperty tSize = new SimpleDoubleProperty();
    public  void openMyView(ActionEvent actionEvent) {
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

    public void setSizeOfScene(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                startButton.setLayoutX(myPane.getWidth()/3);
                startButton.setFont(new Font(startButton.getFont().getName(),tSize.doubleValue()));
                settingsButton.setLayoutX(myPane.getWidth()/3);
                settingsButton.setFont(new Font(settingsButton.getFont().getName(),tSize.doubleValue()));
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                startButton.setLayoutY(myPane.getHeight()/10);
                startButton.setFont(new Font(startButton.getFont().getName(),tSize.doubleValue()));
                settingsButton.setLayoutY(myPane.getHeight()/10);
                settingsButton.setFont(new Font(settingsButton.getFont().getName(),tSize.doubleValue()));
            }
        });
//        startButton.prefWidthProperty().bind(myPane.widthProperty());
//        startButton.prefHeightProperty().bind(myPane.heightProperty());
//        startButton.setLayoutX(myPane.getWidth()/3);
//        startButton.setFont(new Font(startButton.getFont().getName(),tSize.doubleValue()));
//        startButton.setLayoutY(myPane.getHeight()/10);
//        startButton.setFont(new Font(startButton.getFont().getName(),tSize.doubleValue()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startButton.prefHeightProperty().bind(myPane.heightProperty().divide(10));
        startButton.prefWidthProperty().bind(myPane.widthProperty().divide(3));
        tSize.bind(startButton.heightProperty().divide(2));

    }

    public void openSettingsView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SettingsScene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Settings");
            stage.show();
            Stage cStage = (Stage) startButton.getScene().getWindow();
            cStage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
