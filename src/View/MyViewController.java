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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


public class MyViewController implements Observer, IView, Initializable {
    private MyViewModel myViewModel;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public double mousePosX;
    public double mousePosY;

    public javafx.scene.control.MenuItem menuItemNew;
    public javafx.scene.control.MenuItem menuItemSave;
    public javafx.scene.control.MenuItem menuItemLoad;
    public javafx.scene.control.MenuItem menuAbout;
    public javafx.scene.control.Button generateButton;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    private boolean startDrag;
    private MediaPlayer marcoSong;
    private Thread marcoThread;
    private boolean stopMarcoSong = false;

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
        startDrag = false;
        if (MyViewModel.getThreadsNumConfig() == null)
            MyViewModel.setThreadsNumConfig("10");
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void generateMaze(ActionEvent actionEvent) {
        mazeDisplayer.setS(null);
        int rows = -1;
        int cols = -1;
        try {
            rows = Integer.valueOf(textField_mazeRows.getText());
            cols = Integer.valueOf(textField_mazeColumns.getText());

        } catch (Exception e) {
            errorAlert("Please enter valid numbers of rows/columns");
            return;
        }
        if (rows < 2 || cols < 2) {
            errorAlert("Please enter rows and columns larger than 2 ");
            return;
        }

        this.myViewModel.generateMaze(rows, cols);
        menuItemSave.setDisable(false);
        if(marcoThread!=null&&marcoThread.isAlive()){
            stopMarcoSong = true;
            marcoSong.stop();
        }
        playMusic();

    }

    private void playMusic() {
        this.stopMarcoSong = false;
        marcoThread = new Thread(()->{
            try{
                while(!this.stopMarcoSong){
                    Media song = new Media(this.getClass().getResource("/music/primeSong.mp3").toString());
                    this.marcoSong = new MediaPlayer(song);
                    marcoSong.play();
                    Thread.sleep(156000);
                }

            }
            catch (Exception e ){
                System.out.println(e);
            }
        });
        marcoThread.start();
    }

    public void solveMaze(ActionEvent actionEvent) {
        if (this.myViewModel.getMaze() == null) {
            errorAlert("Maze didn't generate yet , please press first on 'Generate Maze'");
        } else {
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

        if (arg instanceof Maze)
            mazeGenerated((Maze) arg);
        else if (arg instanceof Position)
            playerMoved((Position) arg);
        else if (arg instanceof Solution)
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
        this.myViewModel = myViewModel;
        playerRow.textProperty().bind(myViewModel.getStringRowIndexOfPlayer());
        playerCol.textProperty().bind(myViewModel.getStringColIndexOfPlayer());
    }

    public void saveFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save maze");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        //fc.setInitialDirectory(new File("./resources"));
        File saveFile = fc.showSaveDialog(null);
        if (saveFile != null) {
            File newFile = new File(saveFile.getPath());
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(saveFile));
                Object[] gameObjects = new Object[2]; //maze & player position
                gameObjects[0] = myViewModel.getMaze();
                gameObjects[1] = new Position(myViewModel.getRowIndexOfPlayer(), myViewModel.getColIndexOfPlayer());
                objectOutputStream.writeObject(gameObjects);
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (IOException e) {
                errorAlert("Save failed");
                e.printStackTrace();
            }
        } else
            errorAlert("Save failed");

    }

    public void loadFile(ActionEvent actionEvent) {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Load maze");
            fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
            File loadFile = fc.showOpenDialog(null);
            if (loadFile != null) {
                File loadF = new File(loadFile.getPath());
                FileInputStream fileInputStream = new FileInputStream(loadF);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Object[] inputStreamObj = (Object[]) objectInputStream.readObject();
                Maze maze = (Maze) inputStreamObj[0];
                Position playerPos = (Position) inputStreamObj[1];

                myViewModel.loadFile(maze,playerPos);
                objectInputStream.close();
                fileInputStream.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
        this.myViewModel.addObserver(this);
    }

    public void mouseDragged(MouseEvent mouseEvent) {

            if (this.startDrag) {
                if (Math.abs(mouseEvent.getX() - mousePosX) >= mazeDisplayer.getCellWidth() || Math.abs(mouseEvent.getY() - mousePosY) >= mazeDisplayer.getCellHeight()) {
                    myViewModel.movePlayer(mouseEvent, mousePosX, mousePosY);
                    mousePosX = mouseEvent.getX();
                    mousePosY = mouseEvent.getY();
                }

            }

        }



    public void dragDetected(MouseEvent mouseEvent) {

            this.mousePosX = mouseEvent.getX();
            this.mousePosY = mouseEvent.getY();
            this.startDrag = true;
        }


    public void mousePressed(MouseEvent mouseEvent) {
        this.mousePosX = mouseEvent.getX();
        this.mousePosY = mouseEvent.getY();
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        this.startDrag = false;
        this.mousePosX = mouseEvent.getX();
        this.mousePosY = mouseEvent.getY();
        mouseEvent.consume();
    }

    public void scroll(ScrollEvent scrollEvent) {
        if(scrollEvent.isControlDown()){
            if(scrollEvent.getDeltaY()>=0)
                mazeDisplayer.zoomIn();
            else
                mazeDisplayer.zoomOut();
        }
    }

    public void exitMenu(ActionEvent actionEvent) {
        Stage cStage = (Stage) generateButton.getScene().getWindow();
        cStage.close();
        myViewModel.stopServers();
        System.exit(0);
    }

    public void propertiesMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Settings");
            stage.show();
            Stage cStage = (Stage) generateButton.getScene().getWindow();
            cStage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

