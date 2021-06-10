package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import Model.MyModel;
import Server.Configurations;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private Solution sol;
    private Object solLock;
    private int colIndexOfPlayer;
    private int rowIndexOfPlayer;
    private StringProperty stringRowIndexOfPlayer;
    private StringProperty stringColIndexOfPlayer;
    private int rowIndexOfPipito;
    private int colIndexOfPipito;

    public StringProperty getStringRowIndexOfPlayer() {
        return stringRowIndexOfPlayer;
    }

    public StringProperty stringRowIndexOfPlayerProperty() {
        return stringRowIndexOfPlayer;
    }

    public void setStringRowIndexOfPlayer(String stringRowIndexOfPlayer) {
        this.stringRowIndexOfPlayer.set(stringRowIndexOfPlayer);
    }

    public StringProperty getStringColIndexOfPlayer() {
        return stringColIndexOfPlayer;
    }

    public StringProperty stringColIndexOfPlayerProperty() {
        return stringColIndexOfPlayer;
    }

    public void setStringColIndexOfPlayer(String stringColIndexOfPlayer) {
        this.stringColIndexOfPlayer.set(stringColIndexOfPlayer);
    }

    public MyViewModel(IModel model) {
        this.model = model;
        this.solLock = new Object();
    }

    public static String getThreadsNumConfig() {
        return IModel.getThreadsNumConfig();
    }

    public static String getGeneratingAlgorithmConfig() {
        return IModel.getGeneratingAlgorithmConfig();
    }

    public static String getSearchAlgorithmConfig() {
        return IModel.getSearchAlgorithmConfig();
    }

    public static void setThreadsNumConfig(String tNum) {
        IModel.setThreadsNumConfig(tNum);
    }

    public static void setGeneratingAlgorithmConfig(String generateAlgo) {
        IModel.setGeneratingAlgorithmConfig(generateAlgo);
    }

    public static void setSearchAlgorithmConfig(String searchAlgo) {
        IModel.setSearchAlgorithmConfig(searchAlgo);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Maze)
            mazeGenerated((Maze)arg);
        else if(arg instanceof Position[])
            playerMoved((Position[]) arg);
        else if(arg instanceof Solution)
            mazeSolved((Solution) arg);

            setChanged();
            notifyObservers(arg);
        }

    private void mazeSolved(Solution s) {
        this.sol = s;
        this.sol.getSolutionPath().remove(new MazeState(this.getMaze().getStartPosition(),null,null));
        setChanged();
        notifyObservers(sol);
    }

    private void playerMoved(Position[] arg) {
        this.rowIndexOfPlayer = arg[0].getRowIndex();
        this.rowIndexOfPipito = arg[1].getRowIndex();
        this.colIndexOfPlayer = arg[0].getColumnIndex();
        this.colIndexOfPipito = arg[1].getColumnIndex();
        synchronized (solLock){
            if(sol!=null)
            {
                if(sol.getSolutionPath().remove(new MazeState(arg[0],null,null)))
                {
                    setChanged();
                    notifyObservers(sol);
                }
            }

        }

    }

    private void mazeGenerated(Maze arg) {
        sol = null;
    }


    public int getColIndexOfPlayer() {
        return colIndexOfPlayer;
    }

    public void setColIndexOfPlayer(int colIndexOfPlayer) {
        this.colIndexOfPlayer = colIndexOfPlayer;
    }

    public int getRowIndexOfPlayer() {
        return rowIndexOfPlayer;
    }

    public void setRowIndexOfPlayer(int rowIndexOfPlayer) {
        this.rowIndexOfPlayer = rowIndexOfPlayer;
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public void generateMaze(int rows, int cols) {
        //this.sol=null;
        this.model.generateMaze(rows, cols);
    }

    public void movePlayer(KeyEvent keyEvent) {
        MovementDirection direction;
        switch (keyEvent.getCode()){
            case UP -> direction = MovementDirection.UP;
            case DOWN -> direction = MovementDirection.DOWN;
            case LEFT -> direction = MovementDirection.LEFT;
            case RIGHT -> direction = MovementDirection.RIGHT;
            case NUMPAD1 -> direction = MovementDirection.NUMPAD1;
            case NUMPAD2 -> direction = MovementDirection.NUMPAD2;
            case NUMPAD3 -> direction = MovementDirection.NUMPAD3;
            case NUMPAD4 -> direction = MovementDirection.NUMPAD4;
            case NUMPAD6 -> direction = MovementDirection.NUMPAD6;
            case NUMPAD7 -> direction = MovementDirection.NUMPAD7;
            case NUMPAD8 -> direction = MovementDirection.NUMPAD8;
            case NUMPAD9 -> direction = MovementDirection.NUMPAD9;

            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerPosition(direction);

        keyEvent.consume();
    }

    public void stopServers(){
        model.stopServer();
    }

    public void solveMaze() {
        model.solveMaze();
    }

    public void movePlayer(MouseEvent mouseEvent, double mousePosX, double mousePosY) {
        model. movePlayer( mouseEvent,mousePosX, mousePosY);
    }

    public void loadFile(Maze maze, Position playerPos, Position pipitopos) {
        model.loadFile(maze,playerPos,pipitopos);
    }

    public int getRowIndexOfPipito() {
        return rowIndexOfPipito;
    }

    public void setRowIndexOfPipito(int rowIndexOfPipito) {
        this.rowIndexOfPipito = rowIndexOfPipito;
    }

    public int getColIndexOfPipito() {
        return colIndexOfPipito;
    }

    public void setColIndexOfPipito(int colIndexOfPipito) {
        this.colIndexOfPipito = colIndexOfPipito;
    }
}

