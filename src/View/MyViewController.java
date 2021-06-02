package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MyViewController implements Observer,IView,Initializable {
    private MyViewModel myViewModel;
    public Maze maze;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;

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
        System.out.println();
        String generateMethod = MyViewModel.getGeneratingAlgorithmConfig();
        if (generateMethod == null) {
            try {
                maze = new MyMazeGenerator().generate(Integer.valueOf(textField_mazeRows.getText()), Integer.valueOf(textField_mazeColumns.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                generateMethod = MyViewModel.getGeneratingAlgorithmConfig();
                if (generateMethod.equals("MyMazeGenerator"))
                    maze = new MyMazeGenerator().generate(Integer.valueOf(textField_mazeRows.getText()), Integer.valueOf(textField_mazeColumns.getText()));
                else
                    maze = new SimpleMazeGenerator().generate(Integer.valueOf(textField_mazeRows.getText()), Integer.valueOf(textField_mazeColumns.getText()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mazeDisplayer.setS(null);
        mazeDisplayer.setStartP(maze.getStartPosition());
        mazeDisplayer.setEndP(maze.getGoalPosition());
        mazeDisplayer.drawMaze(maze.getMaze());
        setPlayerPosition(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
    }

    public void solveMaze(ActionEvent actionEvent) {
        if (maze == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Maze didn't generate yet , please press first on 'Generate Maze'");
            alert.show();
        } else {
            ISearchingAlgorithm searchingAlgorithm = null;
            Solution s = null;
            ISearchable searchable = new SearchableMaze(maze);
            String solvingMethod = MyViewModel.getSearchAlgorithmConfig();
            if (solvingMethod == null)
                searchingAlgorithm = new BreadthFirstSearch();
            else if (solvingMethod.equals("BreadthFirstSearch"))
                searchingAlgorithm = new BreadthFirstSearch();
            else if (solvingMethod.equals("BestFirstSearch"))
                searchingAlgorithm = new BestFirstSearch();
            else
                searchingAlgorithm = new DepthFirstSearch();
            try {
                s = searchingAlgorithm.solve(searchable);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mazeDisplayer.drawS(s);

        }

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText("Solving maze...");
//        alert.show();
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
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();

        switch (keyEvent.getCode()) {
            case UP:
                if (!(row == 0 || maze.getMaze()[row - 1][col] == 1))
                    row -= 1;
                break;
            case DOWN:
                if (!(row == maze.getRows() - 1 || maze.getMaze()[row + 1][col] == 1))
                    row += 1;
                break;
            case RIGHT:
                if (!(col == maze.getColumns() - 1 || maze.getMaze()[row][col + 1] == 1))
                    col += 1;
                break;
            case LEFT:
                if (!(col == 0 || maze.getMaze()[row][col - 1] == 1))
                    col -= 1;
                break;
            case NUMPAD8:
                if (!(row == 0 || maze.getMaze()[row - 1][col] == 1))
                    row -= 1;
                break;
            case NUMPAD2:
                if (!(row == maze.getRows() - 1 || maze.getMaze()[row + 1][col] == 1))
                    row += 1;
                break;
            case NUMPAD6:
                if (!(col == maze.getColumns() - 1 || maze.getMaze()[row][col + 1] == 1))
                    col += 1;
                break;
            case NUMPAD4:
                if (!(col == 0 || maze.getMaze()[row][col - 1] == 1))
                    col -= 1;
                break;
            case NUMPAD9:
                if (!(row == 0 || col == maze.getColumns() - 1 || maze.getMaze()[row - 1][col + 1] == 1)) {
                    row -= 1;
                    col += 1;
                }
                break;
            case NUMPAD7:
                if (!(row == 0 || col == 0 || maze.getMaze()[row - 1][col - 1] == 1)) {
                    row -= 1;
                    col -= 1;
                }
                break;
            case NUMPAD1:
                if (!(row == maze.getRows() - 1 || col == 0 || maze.getMaze()[row + 1][col - 1] == 1)) {
                    row += 1;
                    col -= 1;
                }
                break;
            case NUMPAD3:
                if (!(row == maze.getRows() - 1 || col == maze.getColumns() - 1 || maze.getMaze()[row + 1][col + 1] == 1)) {
                    row += 1;
                    col += 1;
                }
                break;
        }
        setPlayerPosition(row, col);

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

    }

    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel=myViewModel;
        playerRow.textProperty().bind(myViewModel.getStringRowIndexOfPlayer());
        playerCol.textProperty().bind(myViewModel.getStringColIndexOfPlayer());
    }
}

