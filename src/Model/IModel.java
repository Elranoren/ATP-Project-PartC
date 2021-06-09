package Model;

import Server.Configurations;
import Server.Server;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public interface IModel {
    void generateMaze(int row , int column);
    void solveMaze();
    int getRowIndexOfPlayer();
    int getColIndexOfPlayer();
    void setRowIndexOfPlayer(int row);
    void setColIndexOfPlayer(int col);
    static String getThreadsNumConfig(){
        return Configurations.getThreadsNumConfig();
    }

    static String getGeneratingAlgorithmConfig(){
        return Configurations.getGeneratingAlgorithmConfig();
    }

    static String getSearchAlgorithmConfig(){
        return Configurations.getSearchAlgorithmConfig();
    }

    static void setThreadsNumConfig(String tNum){
        Configurations.setThreadsNumConfig(tNum );
    }

    static void setGeneratingAlgorithmConfig(String generateAlgo){
        Configurations.setGeneratingAlgorithmConfig(generateAlgo);
    }

    static void setSearchAlgorithmConfig(String searchAlgo){
        Configurations.setSearchAlgorithmConfig(searchAlgo);
    }

    Maze getMaze();

    void updatePlayerPosition(MovementDirection direction);

    void stopServer();

    void movePlayer(MouseEvent mouseEvent, double mousePosX, double mousePosY);

    void loadFile(Maze maze, Position playerPos, Position pipitopos);
}
