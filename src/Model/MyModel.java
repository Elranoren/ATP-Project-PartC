package Model;

import javafx.beans.InvalidationListener;

import java.util.Observable;

public class MyModel extends Observable implements IModel {
    private int rowIndexOfPlayer;
    private int colIndexOfPlayer;

    public MyModel() {
        this.rowIndexOfPlayer = 1;
        this.colIndexOfPlayer = 1;
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
    public void generateMaze(int row, int column) {

    }

    @Override
    public void solveMaze() {

    }


}