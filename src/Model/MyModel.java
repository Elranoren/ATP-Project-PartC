package Model;

import algorithms.mazeGenerators.Maze;
import javafx.beans.InvalidationListener;

import java.util.Observable;

public class MyModel extends Observable implements IModel {
    private int rowIndexOfPlayer;
    private int colIndexOfPlayer;
    private Maze maze;

    public MyModel() {
        this.rowIndexOfPlayer = 1;
        this.colIndexOfPlayer = 1;
        this.maze=null;
    }
    @Override
    public int getRowIndexOfPlayer() {
        return rowIndexOfPlayer;
    }

    @Override
    public int getColIndexOfPlayer() {
        return colIndexOfPlayer;
    }

    @Override
    public void setRowIndexOfPlayer(int row) {
        this.rowIndexOfPlayer = row;

    }

    @Override
    public void setColIndexOfPlayer(int col) {
        this.colIndexOfPlayer = col;

    }

    @Override
    public Maze getMaze() {
        return maze ;
    }


    @Override
    public void generateMaze(int row, int column) {

    }

    @Override
    public void solveMaze() {

    }


}