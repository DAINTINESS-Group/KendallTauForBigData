package grarchimedesai.archimedesai.distributed.grid;

import grarchimedesai.archimedesai.Pair;
import grarchimedesai.archimedesai.shapes.Rectangle;

import java.util.*;

public class AdaptiveGrid {

    private final Rectangle rectangle;
    private final double[] splitsX;
    private final double[] splitsY;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private AdaptiveGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, List<Pair> samples){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;

        double i = (double) samples.size() /cellsInXAxis;
        double j = (double) samples.size() /cellsInYAxis;

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
//        System.out.println(splitsX.length + " min:"+rectangle.getLowerBound().getX() + " " + Arrays.toString(splitsX) + " max:"+rectangle.getUpperBound().getX() );
//        System.out.println(splitsY.length + " min:"+rectangle.getLowerBound().getY() + " " + Arrays.toString(splitsY) + " max:"+rectangle.getUpperBound().getY() );
//        System.out.println(samples.size());
    }

    public int getCellId(double x, double y) {
        int xc = findFloorIndexX(x);
        int yc = findFloorIndexY(y);

//        if(xc==-1 || yc==-1){
//            try {
//                System.out.println(Arrays.toString(splitsX));
//                System.out.println(Arrays.toString(splitsY));
//
//                throw new Exception("Error for calculating xc or yc "+x +" "+y);
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }

        return (xc + (yc * cellsInXAxis));
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

    public static AdaptiveGrid newAdaptiveGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, List<Pair> samples){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new AdaptiveGrid(rectangle, cellsInXAxis, cellsInYAxis, samples);
    }

    public int getCellsInXAxis(){
        return cellsInXAxis;
    }
    public int getCellsInYAxis(){
        return cellsInYAxis;
    }

    public Rectangle getRectangle() {return rectangle;}

    public int getXStripeId(double x){
        return findFloorIndexX(x);
    }

    public int getYStripeId(double y){
        return findFloorIndexY(y);
    }

    public int getCellIdFromXcYc(int xc, int yc){
        return (xc + (yc * cellsInXAxis));
    }

    public double[] getSplitsX() {
        return splitsX;
    }

    public double[] getSplitsY() {
        return splitsY;
    }

}
