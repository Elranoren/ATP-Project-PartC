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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

public class MyModel extends Observable implements IModel {
    private Solution sol;
    private int rowIndexOfPipito;
    private int colIndexOfPipito;
    private int rowIndexOfPlayer;
    private int colIndexOfPlayer;
    private Maze maze;
    private Server generateMazeServer;
    private Server solveMazeServer;
    private static boolean serverStart = false;
    private final Logger logger = LogManager.getLogger("MyModel");

    public MyModel() {
        this.generateMazeServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.solveMazeServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        this.rowIndexOfPlayer = 1;
        this.colIndexOfPlayer = 1;
        this.maze = null;
        this.sol = null;
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
        return maze;
    }

    @Override
    public void updatePlayerPosition(MovementDirection direction) {
        switch (direction) {
            case UP:
            case NUMPAD8:
                if (!(rowIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer - 1][colIndexOfPlayer] == 1)) {
                    rowIndexOfPipito = rowIndexOfPlayer;
                    colIndexOfPipito = colIndexOfPlayer;
                    rowIndexOfPlayer -= 1;
                }
                break;
            case DOWN:
            case NUMPAD2:
                if (!(rowIndexOfPlayer == maze.getRows() - 1 || maze.getMaze()[rowIndexOfPlayer + 1][colIndexOfPlayer] == 1)) {
                    rowIndexOfPipito = rowIndexOfPlayer;
                    colIndexOfPipito = colIndexOfPlayer;
                    rowIndexOfPlayer += 1;
                }
                break;
            case RIGHT:
            case NUMPAD6:
                if (!(colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer + 1] == 1)) {
                    rowIndexOfPipito = rowIndexOfPlayer;
                    colIndexOfPipito = colIndexOfPlayer;
                    colIndexOfPlayer += 1;
                }
                break;
            case LEFT:
            case NUMPAD4:
                if (!(colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer - 1] == 1)) {
                    rowIndexOfPipito = rowIndexOfPlayer;
                    colIndexOfPipito = colIndexOfPlayer;
                    colIndexOfPlayer -= 1;
                }
                break;
            case NUMPAD9:
                if (!(rowIndexOfPlayer == 0 || colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer - 1][colIndexOfPlayer + 1] == 1)) {
                    rowIndexOfPipito = rowIndexOfPlayer;
                    colIndexOfPipito = colIndexOfPlayer;
                    rowIndexOfPlayer -= 1;
                    colIndexOfPlayer += 1;
                }
                break;
            case NUMPAD7:
                if (!(rowIndexOfPlayer == 0 || colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer - 1][colIndexOfPlayer - 1] == 1)) {
                    rowIndexOfPipito = rowIndexOfPlayer;
                    colIndexOfPipito = colIndexOfPlayer;
                    rowIndexOfPlayer -= 1;
                    colIndexOfPlayer -= 1;
                }
                break;
            case NUMPAD1:
                if (!(rowIndexOfPlayer == maze.getRows() - 1 || colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer + 1][colIndexOfPlayer - 1] == 1)) {
                    rowIndexOfPipito = rowIndexOfPlayer;
                    colIndexOfPipito = colIndexOfPlayer;
                    rowIndexOfPlayer += 1;
                    colIndexOfPlayer -= 1;
                }
                break;
            case NUMPAD3:
                if (!(rowIndexOfPlayer == maze.getRows() - 1 || colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer + 1][colIndexOfPlayer + 1] == 1)) {
                    rowIndexOfPipito = rowIndexOfPlayer;
                    colIndexOfPipito = colIndexOfPlayer;
                    rowIndexOfPlayer += 1;
                    colIndexOfPlayer += 1;
                }
                break;
        }


        Position[] toSend = new Position[2];
        toSend[0] = new Position(rowIndexOfPlayer, colIndexOfPlayer);
        toSend[1] = new Position(rowIndexOfPipito, colIndexOfPipito);
        setChanged();
        notifyObservers(toSend);

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
                        byte[] decompressedMaze = new byte[row * column + 30];
                        is.read(decompressedMaze);
                        Maze maze = new Maze(decompressedMaze);
                        setMaze(maze);
                        setPlayerPos(maze.getStartPosition());


                    } catch (Exception e) {
                        logger.error("Exception",e);
                        e.printStackTrace();
                    }

                    try {
                        logger.info("client excepted : " + InetAddress.getLocalHost());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    logger.info("server port : 5400");
                    logger.info("Maze size: rows: "+maze.getRows()+" columns: "+maze.getColumns() );

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            logger.error("UnknownHostException",e);
            e.printStackTrace();
        }
        //this.sol = null;

    }

    private void setPlayerPos(Position pos) {
        this.rowIndexOfPlayer = pos.getRowIndex();
        this.colIndexOfPlayer = pos.getColumnIndex();
        this.colIndexOfPipito = pos.getColumnIndex();
        this.rowIndexOfPipito = pos.getRowIndex();
        Position[] toSend = new Position[2];
        toSend[0] = new Position(rowIndexOfPlayer, colIndexOfPlayer);
        toSend[1] = new Position(rowIndexOfPipito, colIndexOfPipito);
        setChanged();
        notifyObservers(toSend);
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
                        logger.error("Exception",e);
                        e.printStackTrace();
                    }
                    try {
                        logger.info("client excepted : " + InetAddress.getLocalHost());
                        logger.info("server port : 5401");
                        logger.info("Maze solving algorithm: "+ MyViewModel.getSearchAlgorithmConfig());
                        logger.info("solving path: "+ sol.getSolutionPath());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            logger.error("UnknownHostException",e);
            e.printStackTrace();
        }


    }


    public void startServer() {
        if(!serverStart){
            this.generateMazeServer.start();
            this.solveMazeServer.start();
            serverStart = true;
            logger.info("starting servers");
        }

    }

    public void stopServer() {
        this.generateMazeServer.stop();
        this.solveMazeServer.stop();
        logger.info("stop servers");
    }


    @Override
    public void movePlayer(MouseEvent mouseEvent, double mousePosX, double mousePosY) {
        if (maze != null) {
            if (!mouseEvent.isControlDown()) {
                if (mouseEvent.getY() > mousePosY && (Math.abs(mouseEvent.getY() - mousePosY) > 15)) {
                    if (!(rowIndexOfPlayer == maze.getRows() - 1 || maze.getMaze()[rowIndexOfPlayer + 1][colIndexOfPlayer] == 1)) {
                        rowIndexOfPipito = rowIndexOfPlayer;
                        colIndexOfPipito = colIndexOfPlayer;
                        rowIndexOfPlayer += 1;
                    }
                } else if (mouseEvent.getY() < mousePosY && (Math.abs(mouseEvent.getY() - mousePosY) > 15)) {
                    if (!(rowIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer - 1][colIndexOfPlayer] == 1)) {
                        rowIndexOfPipito = rowIndexOfPlayer;
                        colIndexOfPipito = colIndexOfPlayer;
                        rowIndexOfPlayer -= 1;
                    }

                } else if (mouseEvent.getX() > mousePosX) {
                    if (!(colIndexOfPlayer == maze.getColumns() - 1 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer + 1] == 1)) {
                        rowIndexOfPipito = rowIndexOfPlayer;
                        colIndexOfPipito = colIndexOfPlayer;
                        colIndexOfPlayer += 1;
                    }

                } else if (mouseEvent.getX() < mousePosX) {
                    if (!(colIndexOfPlayer == 0 || maze.getMaze()[rowIndexOfPlayer][colIndexOfPlayer - 1] == 1)) {
                        rowIndexOfPipito = rowIndexOfPlayer;
                        colIndexOfPipito = colIndexOfPlayer;
                        colIndexOfPlayer -= 1;
                    }
                }

            }
        }
        Position[] toSend = new Position[2];
        toSend[0] = new Position(rowIndexOfPlayer, colIndexOfPlayer);
        toSend[1] = new Position(rowIndexOfPipito, colIndexOfPipito);
        setChanged();
        notifyObservers(toSend);
    }

    @Override
    public void loadFile(Maze maze, Position playerPos, Position pipitopos) {

        this.sol = null;
        this.maze = maze;
        setChanged();
        notifyObservers(this.maze);
        setPlayerPos(playerPos);
        setChanged();
        Position[] toSend = new Position[2];
        toSend[0] = playerPos;
        toSend[1] =pipitopos;
        setChanged();
        notifyObservers(toSend);
        logger.info("Load maze succeeded");
    }
    private void setMaze(Maze maze) {
        this.maze = maze;
        setChanged();
        notifyObservers(maze);

    }


}