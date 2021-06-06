package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MyViewController implements Observer,IView,Initializable {
    private MyViewModel myViewModel;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;

    public javafx.scene.control.MenuItem menuItemNew;
    public javafx.scene.control.MenuItem menuItemSave;
    public javafx.scene.control.MenuItem menuItemLoad;
    public javafx.scene.control.MenuItem menuAbout;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(MyViewModel.getThreadsNumConfig()==null)
            MyViewModel.setThreadsNumConfig("10");
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void generateMaze(ActionEvent actionEvent) {
        mazeDisplayer.setS(null);
        int rows = -1;
        int cols = -1;
        try{
             rows = Integer.valueOf(textField_mazeRows.getText());
             cols = Integer.valueOf(textField_mazeColumns.getText());

        }
        catch (Exception e){
            errorAlert("Please enter valid numbers of rows/columns");
            return;
        }
        if(rows<2 || cols<2){
            errorAlert("Please enter rows and columns larger than 2 ");
            return;
        }

        this.myViewModel.generateMaze(rows, cols);
        menuItemSave.setDisable(false);
//        String generateMethod = MyViewModel.getGeneratingAlgorithmConfig();
//        if (generateMethod == null) {
//            try {
//                maze = new MyMazeGenerator().generate(Integer.valueOf(textField_mazeRows.getText()), Integer.valueOf(textField_mazeColumns.getText()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                generateMethod = MyViewModel.getGeneratingAlgorithmConfig();
//                if (generateMethod.equals("MyMazeGenerator"))
//                    maze = new MyMazeGenerator().generate(Integer.valueOf(textField_mazeRows.getText()), Integer.valueOf(textField_mazeColumns.getText()));
//                else
//                    maze = new SimpleMazeGenerator().generate(Integer.valueOf(textField_mazeRows.getText()), Integer.valueOf(textField_mazeColumns.getText()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }

    public void solveMaze(ActionEvent actionEvent) {
        if(this.myViewModel.getMaze()==null){
            errorAlert("Maze didn't generate yet , please press first on 'Generate Maze'");
        }
        else {
            this.myViewModel.solveMaze();
        }

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Solving maze...");
//        alert.show();
    }

    private void errorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        alert.show();
    }


    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent) {
        this.myViewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col) {
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void displayMaze(Maze maze) {

    }

    @Override
    public void update(Observable o, Object arg) {

        if(arg instanceof Maze)
            mazeGenerated((Maze)arg);
        else if(arg instanceof Position)
            playerMoved((Position)arg);
        else if(arg instanceof Solution)
            mazeSolved((Solution) arg);

    }

    private void mazeSolved(Solution s) {
        mazeDisplayer.setS(s);


    }

    private void playerMoved(Position arg) {
        setPlayerPosition(arg.getRowIndex(), arg.getColumnIndex());
    }

    private void mazeGenerated(Maze arg) {
        mazeDisplayer.setStartP(arg.getStartPosition());
        mazeDisplayer.setEndP(arg.getGoalPosition());
        mazeDisplayer.drawMaze(arg.getMaze());
    }

    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel=myViewModel;
        playerRow.textProperty().bind(myViewModel.getStringRowIndexOfPlayer());
        playerCol.textProperty().bind(myViewModel.getStringColIndexOfPlayer());
    }

    public void saveFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save maze");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        //fc.setInitialDirectory(new File("./resources"));
        File saveFile = fc.showSaveDialog(null);
        if(saveFile!=null)
        {
            File newFile = new File(saveFile.getPath());
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(saveFile));
                Object[] objects = new Object[2];
                objects[0] = myViewModel.getMaze();
                objects[1] = new Position(myViewModel.getRowIndexOfPlayer(),myViewModel.getColIndexOfPlayer());
                objectOutputStream.writeObject(objects);
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (IOException e) {
                errorAlert("Save failed");
                e.printStackTrace();
            }
        }
        else
            errorAlert("Save failed");

    }

    public void loadFile(ActionEvent actionEvent) {

    }

    public void aboutMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("About");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
        this.myViewModel.addObserver(this);
    }
}

