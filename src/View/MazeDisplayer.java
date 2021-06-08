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

public class MazeDisplayer extends Canvas  {
    private int pipitoRow;
    private int pipitoCol;

    public int[][] getMaze() {
        return maze;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }
    private boolean changeSea = false;
    private int[][] maze;
    private Solution s;
    private Position startP;
    private Position endP;
    private static boolean rightMovment;
    private int playerRow;
    private int playerCol;
    // wall and player images:
    StringProperty imageFileNamePipito = new SimpleStringProperty();
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameseaDown = new SimpleStringProperty();
    private double cellHeight;
    private double cellWidth;
    private Object zoomLock;
    private double canvasHeight;
    private double canvasWidth;
    private double zoomVal;
    private double delta;
    private Object changeMazeSize;
    private double startY;
    private double startX;

    public double getZoomVal() {
        return zoomVal;
    }

    public void setZoomVal(double zoomVal) {
        this.zoomVal = zoomVal;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public String getImageFileNameseaDown() {
        return imageFileNameseaDown.get();
    }

    public StringProperty imageFileNameseaDownProperty() {
        return imageFileNameseaDown;
    }

    public void setImageFileNameseaDown(String imageFileNameseaDown) {
        this.imageFileNameseaDown.set(imageFileNameseaDown);
    }

    public int getPipitoRow() {
        return pipitoRow;
    }

    public void setPipitoRow(int pipitoRow) {
        this.pipitoRow = pipitoRow;
    }

    public int getPipitoCol() {
        return pipitoCol;
    }

    public void setPipitoCol(int pipitoCol) {
        this.pipitoCol = pipitoCol;
    }

    public String getImageFileNamePipito() {
        return imageFileNamePipito.get();
    }

    public StringProperty imageFileNamePipitoProperty() {
        return imageFileNamePipito;
    }

    public void setImageFileNamePipito(String imageFileNamePipito) {
        this.imageFileNamePipito.set(imageFileNamePipito);
    }

    public MazeDisplayer() {
        this.cellHeight = 0;
        this.cellWidth = 0;
        this.zoomLock = new Object();
        this.changeMazeSize = new Object();
        this.zoomVal = 0;
        this.delta = 5;
        this.startY=0;
        this.startX=0;
    }
    public String getImageFileNameSea() {
        return imageFileNameSea.get();
    }

    public StringProperty imageFileNameSeaProperty() {
        return imageFileNameSea;
    }

    public void setImageFileNameSea(String imageFileNameSea) {
        this.imageFileNameSea.set(imageFileNameSea);
    }

    StringProperty imageFileNameSea =  new SimpleStringProperty();

    public String getImageFileNameMother() {
        return imageFileNameMother.get();
    }

    public StringProperty imageFileNameMotherProperty() {
        return imageFileNameMother;
    }

    public void setImageFileNameMother(String imageFileNameMother) {
        this.imageFileNameMother.set(imageFileNameMother);
    }

    StringProperty imageFileNameMother =  new SimpleStringProperty();

    public static boolean isRightMovment() {
        return rightMovment;
    }

    public static void setRightMovment(boolean rightMovment) {
        MazeDisplayer.rightMovment = rightMovment;
    }


    public double getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(double cellHeight) {
        this.cellHeight = cellHeight;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;
    }


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
        drawS(s);
    }
    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(Position[] arg ) {
        this.playerRow = arg[0].getRowIndex();
        this.playerCol = arg[0].getColumnIndex();
        this.pipitoRow = arg[1].getRowIndex();
        this.pipitoCol = arg[1].getColumnIndex();
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
    //   public void setImageFileNameSea(String imageFileNameSea) {
//        this.imageFileNameSea.set(imageFileNameSea);
//    }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        this.canvasHeight = getHeight();
        this.canvasWidth = getWidth();
        draw();
    }

    private void draw() {
        if(maze != null){
            int rows = maze.length;
            int cols = maze[0].length;
            synchronized (this.changeMazeSize){
                synchronized (this.zoomLock){
                    this.cellHeight = (canvasHeight + zoomVal) / rows;
                    this.cellWidth = (canvasWidth + zoomVal) / cols;
                }
            }

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawSolution(graphicsContext, cellHeight, cellWidth);
            //graphicsContext.setFill(Color.BLACK);
            //graphicsContext.fillRect( this.startP.getColumnIndex()*cellWidth,this.startP.getRowIndex()*cellHeight, cellWidth, cellHeight);
            //graphicsContext.fillRect( this.endP.getColumnIndex()*cellWidth,this.endP.getRowIndex()*cellHeight, cellWidth, cellHeight);
            drawEndPosition (graphicsContext, cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawPipito(graphicsContext, cellHeight, cellWidth);
        }
    }

    private void drawPipito(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        if(!(getPipitoCol()==getPlayerCol() && getPlayerRow() == getPipitoRow())){
        double x = getPipitoCol() * cellWidth+startX;
        double y = getPipitoRow() * cellHeight+startY;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePipito()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }}

    private void drawEndPosition(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = this.endP.getColumnIndex() * cellWidth+startX;
        double y = this.endP.getRowIndex() * cellHeight+startY;
        graphicsContext.setFill(Color.BLACK);

        Image motherImage = null;
        try {
            motherImage = new Image(new FileInputStream(getImageFileNameMother()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(motherImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(motherImage, x, y, cellWidth, cellHeight);
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }
        Image seaImage = null;
        try{
            if(changeSea){
                seaImage = new Image(new FileInputStream(getImageFileNameSea()));
                changeSea = false;
            }
            else {
                seaImage = new Image(new FileInputStream(getImageFileNameseaDown()));
                changeSea = true;
            }
        } catch (FileNotFoundException e) {
            System.out.println("There is no sea image file");
        }


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth+startX;
                    double y = i * cellHeight+startY;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
                else{
                    double x = j * cellWidth+startX;
                    double y = i * cellHeight+startY;
                    if(seaImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(seaImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

//    private String getImageFileNameSea() {
//        return imageFileNameSea.get();
//    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth+startX;
        double y = getPlayerRow() * cellHeight+startY;
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

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        if(this.s!=null){
            graphicsContext.setFill(Color.PINK);
            for (int i = 0; i < s.getSolutionPath().size(); i++) {
                MazeState mazeState = (MazeState)s.getSolutionPath().get(i);
                int r = mazeState.getMazeP().getRowIndex();
                int c = mazeState.getMazeP().getColumnIndex();
                double x = c * cellWidth+startX;
                double y = r * cellHeight+startY;
                graphicsContext.fillRect(x, y, cellWidth, cellHeight);
            }

        }
    }

    public void zoomIn() {
        synchronized (this.zoomLock){
            zoomVal+=this.delta;
        }
        drawMaze(maze);
    }

    public void zoomOut() {
        synchronized (this.zoomLock){
            if(((canvasHeight+this.zoomVal)/this.maze.length>=1)&&(canvasWidth+this.zoomVal)/this.maze[0].length>=1)
                zoomVal-=this.delta;
        }
        drawMaze(maze);
    }

    public void changeMazePosition(double Vx, double Vy) {
        if(maze != null) {
            double addToX = (cellWidth * maze[0].length) / canvasWidth;
            double addToY = (cellHeight * maze.length) / canvasHeight;
            if (Vx < 0)
                addToX = -addToX;
            if (Vy < 0)
                addToY = -addToY;
            this.startX += Vx + addToX;
            this.startY += Vy + addToY;
            drawMaze(maze);
        }
    }

}