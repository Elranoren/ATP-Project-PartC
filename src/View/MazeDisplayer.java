package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    private Solution s;
    private Position startP;
    private Position endP;
    private int playerRow;
    private int playerCol;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();

    public Position getStartP() {
        return startP;
    }

    public void setStartP(Position startP) {
        this.startP = startP;
    }

    public Position getEndP() {
        return endP;
    }

    public void setEndP(Position endP) {
        this.endP = endP;
    }
    public Solution getS() {
        return s;
    }

    public void setS(Solution s) {
        this.s = s;
    }
    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        draw();
    }

    private void draw() {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawSolution(graphicsContext, cellHeight, cellWidth,s);
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect( this.startP.getColumnIndex()*cellWidth,this.startP.getRowIndex()*cellHeight, cellWidth, cellHeight);
            graphicsContext.fillRect( this.endP.getColumnIndex()*cellWidth,this.endP.getRowIndex()*cellHeight, cellWidth, cellHeight);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    public void drawS(Solution s) {
        this.s = s;
        draw();
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth,Solution s) {
        if(this.s!=null){
            graphicsContext.setFill(Color.PINK);
            for (int i = 0; i < s.getSolutionPath().size(); i++) {
                MazeState mazeState = (MazeState)s.getSolutionPath().get(i);
                int r = mazeState.getMazeP().getRowIndex();
                int c = mazeState.getMazeP().getColumnIndex();
                double x = c * cellWidth;
                double y = r * cellHeight;
                graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        }

        }
    }
}
