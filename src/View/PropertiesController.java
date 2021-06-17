package View;

import ViewModel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
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

    public javafx.scene.control.RadioButton myGenerate;
    public javafx.scene.control.RadioButton simpleGenerate;
    public javafx.scene.control.RadioButton empty;
    public javafx.scene.control.TextField threadNumText;

    private ToggleGroup generateMazeGroup;
    private static boolean firstIn=false;
    public  javafx.scene.layout.AnchorPane propPane;
    private DoubleProperty tSize1 = new SimpleDoubleProperty();
    private DoubleProperty tSize2 = new SimpleDoubleProperty();

    public javafx.scene.control.ChoiceBox searchingAlgorithm;
    public javafx.scene.control.ChoiceBox mazeGenerator;



    public PropertiesController() {



    }

    public void saveSettings(ActionEvent actionEvent) {
        try {
            int threadNum=10;
            try {
                threadNum = Integer.parseInt(threadNumText.getText());
                if(threadNum<=0)
                    throw new Exception();
                MyViewModel.setThreadsNumConfig(String.valueOf(threadNum));

                MyViewModel.setGeneratingAlgorithmConfig(mazeGenerator.getSelectionModel().getSelectedItem().toString());
                MyViewModel.setSearchAlgorithmConfig(searchingAlgorithm.getSelectionModel().getSelectedItem().toString());
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
        this.searchingAlgorithm.setValue("searchAlgo");
        this.mazeGenerator.setValue("generateAlgo");

        ObservableList<String> searchingAlgoOpt =FXCollections.observableArrayList("BreadthFirstSearch","DepthFirstSearch","BestFirstSearch");
        ObservableList<String> generateAlgoOpt =FXCollections.observableArrayList("MyMazeGenerator","SimpleMazeGenerator","EmptyMazeGenerator");
        searchingAlgorithm.setItems(searchingAlgoOpt);
        mazeGenerator.setItems(generateAlgoOpt);
        this.searchingAlgorithm.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        this.searchingAlgorithm.prefWidthProperty().bind(propPane.widthProperty().divide(3));
        this.mazeGenerator.prefHeightProperty().bind(propPane.heightProperty().divide(10));
        this.mazeGenerator.prefWidthProperty().bind(propPane.widthProperty().divide(3));

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
        this.searchingAlgorithm.prefHeightProperty().bind(searchLabel.prefHeightProperty().divide(2));
        this.mazeGenerator.prefHeightProperty().bind(generateLabel.prefHeightProperty().divide(2));
        tSize1.bind(saveButton.heightProperty().divide(2.5));

       // tSize2.bind(cancelButton.heightProperty().divide(4));
        saveButton.prefHeightProperty().bind(cancelButton.heightProperty());
        saveButton.prefWidthProperty().bind(cancelButton.widthProperty());
//        BFS.setToggleGroup(searchAlgorithmGroug);
//        BFS.setUserData("BreadthFirstSearch");
//        BEST.setToggleGroup(searchAlgorithmGroug);
//        BEST.setUserData("BestFirstSearch");
//        DFS.setToggleGroup(searchAlgorithmGroug);
//        DFS.setUserData("DepthFirstSearch");


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
//

                generateLabel.setLayoutX(propPane.getWidth()/10);
                threadNumLabel.setLayoutX(propPane.getWidth()/10);
                threadNumText.setLayoutX(propPane.getWidth()/10);
                searchLabel.setLayoutX(propPane.getWidth()/10);
                searchingAlgorithm.setLayoutX(propPane.getWidth()/10);
                mazeGenerator.setLayoutX(propPane.getWidth()/10);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                saveButton.setLayoutY(propPane.getHeight()/1.5);
                saveButton.setFont(new Font(saveButton.getFont().getName(),tSize1.doubleValue()));
                cancelButton.setLayoutY(propPane.getHeight()/1.5);
                cancelButton.setFont(new Font(cancelButton.getFont().getName(),tSize1.doubleValue()));


                searchLabel.setLayoutY(propPane.getHeight()/30);
                generateLabel.setLayoutY(propPane.getHeight()/4);
                threadNumLabel.setLayoutY(propPane.getHeight()/2.5);
                threadNumText.setLayoutY(propPane.getHeight()/2);
                searchingAlgorithm.setLayoutY(propPane.getHeight()/9);
                mazeGenerator.setLayoutY(propPane.getHeight()/3);
            }
        });

    }

    private void setControlsAsConfigFile() {

        String configSearch = MyViewModel.getSearchAlgorithmConfig();
        String configGenerate = MyViewModel.getGeneratingAlgorithmConfig();
        if (configSearch.equals("BreadthFirstSearch")) {
            searchingAlgorithm.getSelectionModel().select(0);
        } else if (configSearch.equals("BestFirstSearch")) {
            searchingAlgorithm.getSelectionModel().select(2);

        }
        else if (configSearch.equals("DepthFirstSearch")) {
            searchingAlgorithm.getSelectionModel().select(1);
        }
        if(configGenerate.equals("MyMazeGenerator"))
            mazeGenerator.getSelectionModel().select(0);
        else if(configGenerate.equals("SimpleMazeGenerator"))
            mazeGenerator.getSelectionModel().select(1);
        else if(configGenerate.equals("EmptyMazeGenerator"))
            mazeGenerator.getSelectionModel().select(2);
        threadNumText.setText(MyViewModel.getThreadsNumConfig());

    }

    public void cancelSettings(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Changes not saved");
        alert.show();
        alert.setOnCloseRequest(event -> openMainScene());

    }
}

