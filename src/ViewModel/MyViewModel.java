package ViewModel;

import Model.IModel;
import Model.MyModel;
import Server.Configurations;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private ArrayList<AState> sol;
    private Object solLock;
    private int colIndexOfPlayer;
    private int rowIndexOfPlayer;
    private StringProperty stringRowIndexOfPlayer;
    private StringProperty stringColIndexOfPlayer;

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
    }
    public static String getThreadsNumConfig(){
        return IModel.getThreadsNumConfig();
    }

    public static String getGeneratingAlgorithmConfig(){
        return IModel.getGeneratingAlgorithmConfig();
    }

    public static String getSearchAlgorithmConfig(){
        return IModel.getSearchAlgorithmConfig();
    }

    public static void setThreadsNumConfig(String tNum){
        IModel.setThreadsNumConfig(tNum );
    }

    public static void setGeneratingAlgorithmConfig(String generateAlgo){
        IModel.setGeneratingAlgorithmConfig(generateAlgo);
    }

    public static void setSearchAlgorithmConfig(String searchAlgo){
        IModel.setSearchAlgorithmConfig(searchAlgo);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o==model){
            if(arg instanceof Position){
                Position p = (Position) arg;
                if(p.getColumnIndex()<colIndexOfPlayer)
                    MazeDisplayer.setRightMovment(false);
                else if(p.getColumnIndex()>colIndexOfPlayer)
                    MazeDisplayer.setRightMovment(true);
                rowIndexOfPlayer = model.getRowIndexOfPlayer();
                colIndexOfPlayer = model.getColIndexOfPlayer();
                stringColIndexOfPlayer.set("" + colIndexOfPlayer);
                stringRowIndexOfPlayer.set("" + rowIndexOfPlayer);
                synchronized (solLock){
                    if(sol!=null){
                        if(sol.remove(new MazeState(new Position(p.getRowIndex(),p.getColumnIndex()),null,null))){
                            setChanged();
                            notifyObservers(sol);
                        }

                    }
                }
            }

            else if(arg instanceof Maze)
                sol=null;
            else if(arg instanceof Solution){
                sol = ((Solution) arg).getSolutionPath();
                sol.remove(new MazeState(new Position(rowIndexOfPlayer,colIndexOfPlayer),null,null));


            }

            setChanged();
            notifyObservers(arg);
        }
    }
}

