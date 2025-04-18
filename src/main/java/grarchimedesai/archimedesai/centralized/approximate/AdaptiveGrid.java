package grarchimedesai.archimedesai.centralized.approximate;

import grarchimedesai.archimedesai.Pair;
import grarchimedesai.archimedesai.shapes.Rectangle;

import java.util.Comparator;
import java.util.List;

public class AdaptiveGrid {

    private final Rectangle rectangle;

    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private final double x;
    private final double y;

    private final int[][] cells;

    private final double[] splitsX;
    private final double[] splitsY;

    private AdaptiveGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, List<Pair> samples){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        cells = new int[cellsInXAxis][cellsInYAxis];
        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX()) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY()) / cellsInYAxis;
        System.out.println("x: "+x +" y:"+y);

        splitsX = new double[cellsInXAxis-1];
        splitsY = new double[cellsInYAxis-1];

        samples.sort(Comparator.comparingDouble(Pair::getX));
        for (int k = 1; k < cellsInXAxis; k++) {
            splitsX[k-1] = samples.get((int) Math.floor(k * ((samples.size() - 1) / (double) cellsInXAxis))).getX();
        }

        samples.sort(Comparator.comparingDouble(Pair::getY));
        for (int k = 1; k < cellsInYAxis; k++) {
            splitsY[k-1] = samples.get((int) Math.floor(k * ((samples.size() - 1) / (double) cellsInYAxis))).getY();
        }
    }

    public void updateCell(double x, double y) {
        int xc = findFloorIndexX(x);
        int yc = findFloorIndexY(y);
        cells[xc][yc]++;
    }

    public int findFloorIndexX(double target) {
        int left = 0, right = splitsX.length - 1;
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (splitsX[mid] <= target) {
                result = mid+1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    public int findFloorIndexY(double target) {
        int left = 0, right = splitsY.length - 1;
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (splitsY[mid] <= target) {
                result = mid+1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    public static AdaptiveGrid newGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, List<Pair> samples){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new AdaptiveGrid(rectangle, cellsInXAxis, cellsInYAxis, samples);
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
