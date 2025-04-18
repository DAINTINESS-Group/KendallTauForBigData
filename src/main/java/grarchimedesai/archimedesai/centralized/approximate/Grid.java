package grarchimedesai.archimedesai.centralized.approximate;

import grarchimedesai.archimedesai.shapes.Rectangle;

public class Grid {

    private final Rectangle rectangle;

    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private final double x;
    private final double y;

    private final int[][] cells;

    private Grid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        cells = new int[cellsInXAxis][cellsInYAxis];
        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX()) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY()) / cellsInYAxis;
        System.out.println("x: "+x +" y:"+y);
    }

    public void updateCell(double x, double y) {
        int xc = (int) ((x-rectangle.getLowerBound().getX()) / this.x);
        int yc = (int) ((y-rectangle.getLowerBound().getY()) / this.y);
        cells[xc][yc]++;
    }

    public static Grid newGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new Grid(rectangle, cellsInXAxis, cellsInYAxis);
    }

    public int[][] getCells() {
        return cells;
    }

    public int getCellsInXAxis(){
        return cellsInXAxis;
    }
    public int getCellsInYAxis(){
        return cellsInYAxis;
    }

    public Rectangle getRectangle() {return rectangle;}

}
