package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsSceneController implements Initializable {
    public  javafx.scene.control.Button saveButton;
    public javafx.scene.control.RadioButton BFS;
    public javafx.scene.control.RadioButton DFS;
    public javafx.scene.control.RadioButton BEST;
    private ToggleGroup searchAlgorithmGroug;

    public SettingsSceneController() {
        this.searchAlgorithmGroug = new ToggleGroup();
    }

    public void saveSettings(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WelcomeScene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Game");
            Stage currentStage = (Stage) saveButton.getScene().getWindow();
            currentStage.close();
            stage.show();

        } catch(Exception e) {
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
        String currentAlgorithm = MyViewModel.getConfigurationSolvingAlgorithmName();
        for (Toggle rb: solvingAlgorithmGroup.getToggles()) {
            if(rb.getUserData().toString().equals(currSearchingAlgorithm))
                rb.setSelected(true);
        }
    }
}
