package Model;

import Server.Configurations;
import Server.Server;
import algorithms.mazeGenerators.IMazeGenerator;


public interface IModel {
    void generateMaze(int row , int column);
    void solveMaze();

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

}
