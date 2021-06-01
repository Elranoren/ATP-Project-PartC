package Model;

import Server.Configurations;
import algorithms.mazeGenerators.IMazeGenerator;

public interface IModel {
    void generateMaze(int row , int column);
    void solveMaze();

//    static int getThreadsNumConfig(){
//       // return Configurations.p.getProperty("mazeGeneratingAlgorithm");
//    }
//
//    static IMazeGenerator getGeneratingAlgorithmConfig(){
//    }
//
//    static String getSearchAlgorithmConfig(){
//    }
}
