package gr.archimedesai.centralized.hashmap.grid;

import gr.archimedesai.Pair;
import gr.archimedesai.algorithms.Algorithms;
import gr.archimedesai.shapes.Rectangle;

import java.util.*;

public class AdaptiveGrid {

    private final Rectangle rectangle;
    private final double[] splitsX;
    private final double[] splitsY;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private HashMap<Integer, Integer> counter;
    private HashMap<Integer, Pair[]> cells;

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
        System.out.println(samples.size());
    }

    public void initializeMap(HashMap<Integer, Integer> counter){
        this.counter=counter;
        cells = new HashMap<>(((int)Math.ceil(counter.size() / 0.75)));

        counter.forEach((k,v)->{
            cells.put(k, new Pair[v]);
        });

        counter.forEach((k,v)->{
            counter.replace(k,0);
        });

    }

    public void putPair(Pair pair){
        int cellId = getCellId(pair.getX(), pair.getY());
        int index = counter.get(cellId);
        counter.replace(cellId, index+1);
        cells.get(cellId)[index] = pair;
    }

//    public void clearCounter(){
//        counter.clear();
//        counter = null;
//    }

    //    public int getCellId(double x, double y) {
//        int xc = -1;
//        int yc = -1;
//
//        if(Double.compare(x,splitsX[splitsX.length-1])!=-1){
//            xc = cellsInXAxis-1;
//        }else{
//            for (int i = 0; i < splitsX.length; i++) {
//                if(Double.compare(x,splitsX[i])==-1){
//                    xc = i;
//                    break;
//                }
//            }
//        }
//
//        if(Double.compare(y,splitsY[splitsY.length-1])!=-1){
//            yc = cellsInYAxis-1;
//        }else {
//            for (int i = 0; i < splitsY.length; i++) {
//                if (Double.compare(y, splitsY[i]) == -1) {
//                    yc = i;
//                    break;
//                }
//            }
//        }
//
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
//
//        return (xc + (yc * cellsInXAxis));
//    }
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
//    public int getXStripeId(double x){
//        int xc = -1;
//        if(Double.compare(x,splitsX[splitsX.length-1])!=-1){
//            xc = cellsInXAxis-1;
//        }else {
//            for (int i = 0; i < splitsX.length; i++) {
//                if (Double.compare(x, splitsX[i]) == -1) {
//                    xc = i;
//                    break;
//                }
//            }
//        }
//        if(xc==-1){
//            try {
//                throw new Exception("Error for calculating xc");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return xc;
//    }
//
//    public int getYStripeId(double y){
//        int yc = -1;
//        if(Double.compare(y,splitsY[splitsY.length-1])!=-1){
//            yc = cellsInYAxis-1;
//        }else {
//            for (int i = 0; i < splitsY.length; i++) {
//                if (Double.compare(y, splitsY[i]) == -1) {
//                    yc = i;
//                    break;
//                }
//            }
//        }
//        if(yc==-1){
//            try {
//                throw new Exception("Error for calculating yc");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return yc;
//    }

    public int getCellIdFromXcYc(int xc, int yc){
        return (xc + (yc * cellsInXAxis));
    }

    public void sortPairsInCellsByX(){
        cells.forEach((k,v)->{
            Algorithms.sortPairsByX(v);
//            Arrays.sort(v, new Comparator<Pair>() {
//                /** {@inheritDoc} */
//                @Override
//                public int compare(Pair pair1, Pair pair2) {
//                    int compareFirst = Double.compare(pair1.getX(),pair2.getX());
//                    return compareFirst != 0 ? compareFirst : Double.compare(pair1.getY(),pair2.getY());
//                }
//            });
        });
    }

    public void sortPairsInCellsByY(){
        cells.forEach((k,v)->{
            Arrays.sort(v, new Comparator<Pair>() {
                /** {@inheritDoc} */
                @Override
                public int compare(Pair pair1, Pair pair2) {
                    int compareFirst = Double.compare(pair1.getY(),pair2.getY());
                    return compareFirst != 0 ? compareFirst : Double.compare(pair1.getX(),pair2.getX());
                }
            });
        });
    }

    public Map<Integer, Pair[]> getCells() {
        return cells;
    }

    public String cellsStats() {
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