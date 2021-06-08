package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class WelcomeSceneController implements Initializable {
    public  javafx.scene.control.Button startButton;
    public  javafx.scene.control.Button settingsButton;
    public  javafx.scene.image.ImageView marcoImage;
    public  javafx.scene.layout.AnchorPane myPane;

    private DoubleProperty textSize = new SimpleDoubleProperty();
    public  void openMyView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Game");
            MyModel myModel = new MyModel();
            myModel.startServer();
            MyViewModel myViewModel = new MyViewModel(myModel);
            MyViewController myViewController = fxmlLoader.getController();
            /////////
            myViewController.setViewModel(myViewModel);
            myModel.addObserver(myViewModel);
            myViewModel.addObserver(myViewController);
            onCloseAppAction(stage,myViewController); // close the application (the stage and the servers)
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

    private void onCloseAppAction(Stage stage, MyViewController myViewController) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to exit the app?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() != ButtonType.OK)
                    windowEvent.consume();
                else {
                   myViewController.exitMenu(null);
                   System.exit(0);
                }
            }
        });
    }

    public void setSizeOfScene(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                startButton.setLayoutX(myPane.getWidth()/10);
                startButton.setFont(new Font(startButton.getFont().getName(), textSize.doubleValue()));
                settingsButton.setLayoutX(myPane.getWidth()/10);
                settingsButton.setFont(new Font(settingsButton.getFont().getName(), textSize.doubleValue()));

            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                startButton.setLayoutY(myPane.getHeight()/10);
                startButton.setFont(new Font(startButton.getFont().getName(), textSize.doubleValue()));
                settingsButton.setLayoutY(myPane.getHeight()/4);
                settingsButton.setFont(new Font(settingsButton.getFont().getName(), textSize.doubleValue()));
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startButton.prefHeightProperty().bind(myPane.heightProperty().divide(10));
        startButton.prefWidthProperty().bind(myPane.widthProperty().divide(3));
        settingsButton.prefHeightProperty().bind(myPane.heightProperty().divide(10));
        settingsButton.prefWidthProperty().bind(myPane.widthProperty().divide(3));
        marcoImage.fitWidthProperty().bind(myPane.widthProperty());
        marcoImage.fitHeightProperty().bind(myPane.heightProperty());

        startButton.prefHeightProperty().bind(settingsButton.prefHeightProperty());
        startButton.prefWidthProperty().bind(settingsButton.prefWidthProperty());
        //settingsButton.prefHeightProperty().bind(startButton.prefHeightProperty());
        //settingsButton.prefWidthProperty().bind(startButton.prefWidthProperty());
        textSize.bind(startButton.heightProperty().divide(2));
        Image image = new Image(getClass().getResourceAsStream("/images/startPic.jpg"));
        marcoImage.setImage(image);

    }

    public void openSettingsView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            Scene scene = new Scene(root,600,400);
            PropertiesController propertiesControllerController = fxmlLoader.getController();
            propertiesControllerController.setSizeOfScene(scene);
            stage.setScene(scene);


            stage.setTitle("Settings");
            stage.show();
            Stage cStage = (Stage) startButton.getScene().getWindow();
            cStage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
