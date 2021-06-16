package View;

import ViewModel.*;
import org.apache.logging.log4j.LogManager;
import algorithms.mazeGenerators.IMazeGenerator;
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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    public javafx.scene.control.Label threadNumLabel;
    public javafx.scene.control.Label generateLabel;
    public javafx.scene.control.Label searchLabel;
    public javafx.scene.control.Button saveButton;
    public javafx.scene.control.Button cancelButton;
    public javafx.scene.control.RadioButton BFS;
    public javafx.scene.control.RadioButton DFS;
    public javafx.scene.control.RadioButton BEST;
    public javafx.scene.control.RadioButton myGenerate;
    public javafx.scene.control.RadioButton simpleGenerate;
    public javafx.scene.control.RadioButton empty;
    public javafx.scene.control.TextField threadNumText;
    private ToggleGroup searchAlgorithmGroug;
    private ToggleGroup generateMazeGroup;
    private static boolean firstIn=false;
    public  javafx.scene.layout.AnchorPane propPane;
    private DoubleProperty tSize1 = new SimpleDoubleProperty();
    private DoubleProperty tSize2 = new SimpleDoubleProperty();

    public PropertiesController() {
        this.searchAlgorithmGroug = new ToggleGroup();
        this.generateMazeGroup = new ToggleGroup();
    }

    public void saveSettings(ActionEvent actionEvent) {
        try {
            int threadNum=10;
            try {
                threadNum = Integer.parseInt(threadNumText.getText());
                if(threadNum<=0)
                    throw new Exception();
                MyViewModel.setThreadsNumConfig(String.valueOf(threadNum));
                MyViewModel.setGeneratingAlgorithmConfig(generateMazeGroup.getSelectedToggle().getUserData().toString());
                MyViewModel.setSearchAlgorithmConfig(searchAlgorithmGroug.getSelectedToggle().getUserData().toString());
                openMainScene();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter a valid Therad number");
                alert.show();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openMainScene()
    {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/WelcomeScene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 335);
            stage.setScene(scene);
            stage.setTitle("Game");
            Stage currentStage = (Stage) saveButton.getScene().getWindow();
            WelcomeSceneController welcomeSceneController = fxmlLoader.getController();
            onCloseAppAction(stage, welcomeSceneController);
            currentStage.close();
            welcomeSceneController.setSizeOfScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void onCloseAppAction(Stage stage, WelcomeSceneController welcomeSceneController) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to exit the app?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() != ButtonType.OK)
                    windowEvent.consume();
                else {
                    System.exit(0);
                }
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BFS.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        BFS.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        DFS.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        DFS.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        BEST.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        BEST.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        simpleGenerate.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        simpleGenerate.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        empty.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        empty.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        myGenerate.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        myGenerate.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        saveButton.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        saveButton.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        cancelButton.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        cancelButton.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        threadNumLabel.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        threadNumLabel.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        generateLabel.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        generateLabel.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        searchLabel.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        searchLabel.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        threadNumText.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        threadNumText.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        myGenerate.prefHeightProperty().bind(simpleGenerate.prefHeightProperty());
        myGenerate.prefWidthProperty().bind(simpleGenerate.prefWidthProperty());
        tSize1.bind(saveButton.heightProperty().divide(2.5));

       // tSize2.bind(cancelButton.heightProperty().divide(4));
        saveButton.prefHeightProperty().bind(cancelButton.heightProperty());
        saveButton.prefWidthProperty().bind(cancelButton.widthProperty());
        BFS.setToggleGroup(searchAlgorithmGroug);
        BFS.setUserData("BreadthFirstSearch");
        BEST.setToggleGroup(searchAlgorithmGroug);
        BEST.setUserData("BestFirstSearch");
        DFS.setToggleGroup(searchAlgorithmGroug);
        DFS.setUserData("DepthFirstSearch");

        myGenerate.setToggleGroup(generateMazeGroup);
        myGenerate.setUserData("MyMazeGenerator");
        simpleGenerate.setToggleGroup(generateMazeGroup);
        simpleGenerate.setUserData("SimpleMazeGenerator");
        empty.setToggleGroup(generateMazeGroup);
        empty.setUserData("EmptyMazeGenerator");
        if(!firstIn) {
            MyViewModel.setThreadsNumConfig(String.valueOf(10));
            MyViewModel.setGeneratingAlgorithmConfig("MyMazeGenerator");
            MyViewModel.setSearchAlgorithmConfig("BreadthFirstSearch");
            firstIn=true;
        }
        setControlsAsConfigFile();

    }

    public void setSizeOfScene(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                saveButton.setLayoutX(propPane.getWidth()/10);
                saveButton.setFont(new Font(saveButton.getFont().getName(),tSize1.doubleValue()));
                cancelButton.setLayoutX(propPane.getWidth()/2);
                cancelButton.setFont(new Font(cancelButton.getFont().getName(),tSize1.doubleValue()));
                BFS.setLayoutX(propPane.getWidth()/10);
                DFS.setLayoutX(propPane.getWidth()/3.5);
                BEST.setLayoutX(propPane.getWidth()/2);
                myGenerate.setLayoutX(propPane.getWidth()/10);
                simpleGenerate.setLayoutX(propPane.getWidth()/3.5);
                empty.setLayoutX(propPane.getWidth()/2);
                generateLabel.setLayoutX(propPane.getWidth()/10);
                threadNumLabel.setLayoutX(propPane.getWidth()/10);
                threadNumText.setLayoutX(propPane.getWidth()/10);
                searchLabel.setLayoutX(propPane.getWidth()/10);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                saveButton.setLayoutY(propPane.getHeight()/1.5);
                saveButton.setFont(new Font(saveButton.getFont().getName(),tSize1.doubleValue()));
                cancelButton.setLayoutY(propPane.getHeight()/1.5);
                cancelButton.setFont(new Font(cancelButton.getFont().getName(),tSize1.doubleValue()));
                BFS.setLayoutY(propPane.getHeight()/4);
                DFS.setLayoutY(propPane.getHeight()/4);
                BEST.setLayoutY(propPane.getHeight()/4);
                myGenerate.setLayoutY(propPane.getHeight()/3);
                simpleGenerate.setLayoutY(propPane.getHeight()/3);
                empty.setLayoutY(propPane.getHeight()/3);
                searchLabel.setLayoutY(propPane.getHeight()/5.5);
                generateLabel.setLayoutY(propPane.getHeight()/3.5);
                threadNumLabel.setLayoutY(propPane.getHeight()/2.5);
                threadNumText.setLayoutY(propPane.getHeight()/2);
            }
        });

    }

    private void setControlsAsConfigFile() {

        String configSearch = MyViewModel.getSearchAlgorithmConfig();
        String configGenerate = MyViewModel.getGeneratingAlgorithmConfig();
        if (configSearch.equals("BreadthFirstSearch"))
            BFS.setSelected(true);
        else if (configSearch.equals("BestFirstSearch"))
            BEST.setSelected(true);
        else if (configSearch.equals("DepthFirstSearch"))
            DFS.setSelected(true);
        if(configGenerate.equals("MyMazeGenerator"))
            myGenerate.setSelected(true);
        else if(configGenerate.equals("SimpleMazeGenerator"))
            simpleGenerate.setSelected(true);
        else if(configGenerate.equals("EmptyMazeGenerator"))
            empty.setSelected(true);
        threadNumText.setText(MyViewModel.getThreadsNumConfig());

    }

    public void cancelSettings(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Changes not saved");
        alert.show();
        alert.setOnCloseRequest(event -> openMainScene());

    }
}

