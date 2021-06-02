package View;

import ViewModel.*;
import algorithms.mazeGenerators.IMazeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    public javafx.scene.control.Button saveButton;
    public javafx.scene.control.Button cancelButton;
    public javafx.scene.control.RadioButton BFS;
    public javafx.scene.control.RadioButton DFS;
    public javafx.scene.control.RadioButton BEST;
    public javafx.scene.control.RadioButton myGenerate;
    public javafx.scene.control.RadioButton simpleGenerate;
    public javafx.scene.control.TextField threadNumText;
    private ToggleGroup searchAlgorithmGroug;
    private ToggleGroup generateMazeGroup;
    private static boolean firstIn=false;
    public PropertiesController() {
        this.searchAlgorithmGroug = new ToggleGroup();
        this.generateMazeGroup = new ToggleGroup();
    }

    public void saveSettings(ActionEvent actionEvent) {
        try {
            int threadNum=5;
            try {
                threadNum = Integer.parseInt(threadNumText.getText());
                MyViewModel.setThreadsNumConfig(String.valueOf(threadNum));
                MyViewModel.setGeneratingAlgorithmConfig(generateMazeGroup.getSelectedToggle().getUserData().toString());
                MyViewModel.setSearchAlgorithmConfig(searchAlgorithmGroug.getSelectedToggle().getUserData().toString());
                openMainScene();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter a valid number");
                alert.show();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMainScene()
    {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WelcomeScene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Game");
            Stage currentStage = (Stage) saveButton.getScene().getWindow();
            currentStage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        if(!firstIn) {
            MyViewModel.setThreadsNumConfig(String.valueOf(10));
            MyViewModel.setGeneratingAlgorithmConfig("MyMazeGenerator");
            MyViewModel.setSearchAlgorithmConfig("BreadthFirstSearch");
            firstIn=true;
        }
        setControlsAsConfigFile();




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
        threadNumText.setText(MyViewModel.getThreadsNumConfig());

    }

    public void cancelSettings(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Changes not saved");
        alert.show();
        alert.setOnCloseRequest(event -> openMainScene());

    }
}

