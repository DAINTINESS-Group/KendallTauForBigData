package gr.archimedesai.distributed.grid;

import gr.archimedesai.Pair;
import gr.archimedesai.shapes.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {

    private final Rectangle rectangle;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private final double x;
    private final double y;

    private Grid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;

        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX()) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY()) / cellsInYAxis;
        System.out.println("x: "+x +" y:"+y);
    }

    public int getCellId(double x, double y) {

        int xc = (int) ((x-rectangle.getLowerBound().getX()) / this.x);
        int yc = (int) ((y-rectangle.getLowerBound().getY()) / this.y);

        return (xc + (yc * cellsInXAxis));
    }

    public static Grid newGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new Grid(rectangle, cellsInXAxis, cellsInYAxis);
    }

    public int getCellsInXAxis(){
        return cellsInXAxis;
    }
    public int getCellsInYAxis(){
        return cellsInYAxis;
    }

    public Rectangle getRectangle() {return rectangle;}

    public int getXStripeId(double x){
        return (int) ((x-rectangle.getLowerBound().getX()) / this.x);
    }

    public int getYStripeId(double y){
        return (int) ((y-rectangle.getLowerBound().getY()) / this.y);
    }

    public double getXLowerRangeByStripeId(int xc){
        return (xc*x)+ rectangle.getLowerBound().getX();
    }

    public double getYLowerRangeByStripeId(int yc){
        return (yc*y)+ rectangle.getLowerBound().getY();
    }

    public double getXUpperRangeByStripeId(int xc){
        return ((xc+1)*x)+ rectangle.getLowerBound().getX();
    }

    public double getYUpperRangeByStripeId(int yc){
        return ((yc+1)*y)+ rectangle.getLowerBound().getY();
    }


//    public int getCellIdFromXStripes(int xc, double y){
//        int yc = (int) ((y-rectangle.getLowerBound().getY()) / this.y);
//        return (xc + (yc * cellsInXAxis));
//    }
//
//    public int getCellIdFromYStripes(int yc, double x){
//        int xc = (int) ((x-rectangle.getLowerBound().getX()) / this.x);
//        return (xc + (yc * cellsInXAxis));
//    }
//
//    public int getXc(int cellId){
//        int xc = cellId - (yc * cellsInXAxis);
//    }
//
//    public int getYc(int cellId){
//        int yc = cellId/cellsInXAxis;
//    }

    public int getCellIdFromXcYc(int xc, int yc){
        return (xc + (yc * cellsInXAxis));
    }

    public static String cellsStats(HashMap<Integer, Pair[]> cells) {
        List<Integer> numbers = new ArrayList<>(cells.size());
        for (Map.Entry<Integer, Pair[]> integerEntry : cells.entrySet()) {
            numbers.add(integerEntry.getValue().length);
        }

        double mean = numbers.stream().mapToDouble(i->i).average().orElse(Integer.MIN_VALUE);
        double min = numbers.stream().mapToDouble(i->i).min().orElse(Integer.MIN_VALUE);
        double max = numbers.stream().mapToDouble(i->i).max().orElse(Integer.MIN_VALUE);
        double variance = numbers.stream().mapToDouble(i->i).map(i -> Math.pow(i - mean, 2))
                .average()
                .orElse(Integer.MIN_VALUE);
        double stdDev = Math.sqrt(variance);
        return cells.size()+","+(int) min + "," +(int) max + "," + stdDev;
    }

}
