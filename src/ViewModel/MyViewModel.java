package ViewModel;

import Model.IModel;
import Model.MyModel;
import Server.Configurations;

public class MyViewModel {
    private IModel model;
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
}

