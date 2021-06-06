package Model;
import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import javafx.beans.InvalidationListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

public class MyModel extends Observable implements IModel {
    private Solution sol;
    private int rowIndexOfPlayer;
    private int colIndexOfPlayer;
    private Maze maze;
    private Server generateMazeServer;
    private Server solveMazeServer;

    public MyModel() {
        this.generateMazeServer = new Server(5400,1000, new ServerStrategyGenerateMaze());
        this.solveMazeServer = new Server(5401,1000, new ServerStrategySolveSearchProblem());
        this.rowIndexOfPlayer = 1;
        this.colIndexOfPlayer = 1;
        this.maze=null;
        this.sol=null;
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
    public void updatePlayerPosition(MovementDirection direction) {
        switch (direction){
            case UP:
                if (!(rowIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer - 1][colIndexOfPlayer] == 1))
                    rowIndexOfPlayer -= 1;
                break;
            case DOWN:
                if (!(rowIndexOfPlayer == maze.getRows() - 1 || maze.getMaze()[rowIndexOfPlayer + 1][colIndexOfPlayer] == 1))
                    rowIndexOfPlayer += 1;
                break;
            case RIGHT:
                if (!(colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer + 1] == 1))
                    colIndexOfPlayer += 1;
                break;
            case LEFT:
                if (!(colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer - 1] == 1))
                    colIndexOfPlayer -= 1;
                break;
            case NUMPAD8:
                if (!(rowIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer - 1][colIndexOfPlayer] == 1))
                    rowIndexOfPlayer -= 1;
                break;
            case NUMPAD2:
                if (!(rowIndexOfPlayer == maze.getRows() - 1 || maze.getMaze()[rowIndexOfPlayer + 1][colIndexOfPlayer] == 1))
                    rowIndexOfPlayer += 1;
                break;
            case NUMPAD6:
                if (!(colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer + 1] == 1))
                    colIndexOfPlayer += 1;
                break;
            case NUMPAD4:
                if (!(colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer - 1] == 1))
                    colIndexOfPlayer -= 1;
                break;
            case NUMPAD9:
                if (!(rowIndexOfPlayer == 0 || colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer - 1][colIndexOfPlayer + 1] == 1)) {
                    rowIndexOfPlayer -= 1;
                    colIndexOfPlayer += 1;
                }
                break;
            case NUMPAD7:
                if (!(rowIndexOfPlayer == 0 || colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer - 1][colIndexOfPlayer - 1] == 1)) {
                    rowIndexOfPlayer -= 1;
                    colIndexOfPlayer -= 1;
                }
                break;
            case NUMPAD1:
                if (!(rowIndexOfPlayer == maze.getRows() - 1 || colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer + 1][colIndexOfPlayer - 1] == 1)) {
                    rowIndexOfPlayer += 1;
                    colIndexOfPlayer -= 1;
                }
                break;
            case NUMPAD3:
                if (!(rowIndexOfPlayer == maze.getRows() - 1 || colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer + 1][colIndexOfPlayer + 1] == 1)) {
                    rowIndexOfPlayer += 1;
                    colIndexOfPlayer += 1;
                }
                break;
        }

        setChanged();
        notifyObservers(new Position(rowIndexOfPlayer,colIndexOfPlayer));

    }


    @Override
    public void generateMaze(int row, int column) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                        @Override
                        public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                            try {
                                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                                toServer.flush();
                                int[] mazeDimensions = new int[]{row, column};
                                toServer.writeObject(mazeDimensions);
                                toServer.flush();
                                byte[] compressedMaze = (byte[]) fromServer.readObject();
                                InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                                byte[] decompressedMaze = new byte[row*column+30];
                                is.read(decompressedMaze);
                                Maze maze = new Maze(decompressedMaze);
                                setMaze(maze);
                                setPlayerPos(maze.getStartPosition());



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //this.sol = null;

    }

    private void setPlayerPos(Position pos) {
        this.rowIndexOfPlayer = pos.getRowIndex();
        this.colIndexOfPlayer = pos.getColumnIndex();
        setChanged();
        notifyObservers(pos);
    }

    private void setMaze(Maze maze) {
        this.maze = maze;
        setChanged();
        notifyObservers(maze);
    }

    @Override
    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed withMyCompressor) from server
                        //Print Maze Solution retrieved from the server
                        // System.out.println(String.format("Solution steps: %s", mazeSolution));
                        sol = mazeSolution;
                        setChanged();
                        notifyObservers(sol);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }


    public void startServer() {
        this.generateMazeServer.start();
        this.solveMazeServer.start();
    }

    public void stopServer() {
        this.generateMazeServer.stop();
        this.solveMazeServer.stop();
    }

    @Override
    public void movePlayer(MouseEvent mouseEvent, double mousePosX, double mousePosY) {
        if(maze!=null){
            if(!mouseEvent.isControlDown()){
                if(mouseEvent.getX()>mousePosX){
                    if (!(colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer + 1] == 1))
                        colIndexOfPlayer += 1;
                }
                else if(mouseEvent.getX()<mousePosX){
                    if (!(colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer - 1] == 1))
                        colIndexOfPlayer -= 1;
                }
                else if(mouseEvent.getY()>mousePosY ){
                    if (!(rowIndexOfPlayer == maze.getRows()-1 || maze.getMaze()[rowIndexOfPlayer+1][colIndexOfPlayer ] == 1))
                        rowIndexOfPlayer += 1;
                }
                else if(mouseEvent.getY()<mousePosY ){
                    if (!(rowIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer-1][colIndexOfPlayer ] == 1))
                        rowIndexOfPlayer -= 1;
                }
            }
        }
        setChanged();
        notifyObservers(new Position(rowIndexOfPlayer,colIndexOfPlayer));
    }
}