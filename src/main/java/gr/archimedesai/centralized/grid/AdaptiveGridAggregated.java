package gr.archimedesai.centralized.grid;

import gr.archimedesai.Pair;
import gr.archimedesai.algorithms.Algorithms;
import gr.archimedesai.shapes.Rectangle;
import scala.Tuple2;

import java.util.*;

public class AdaptiveGridAggregated {

    private final Rectangle rectangle;
    private final double[] splitsX;
    private final double[] splitsY;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private int[][] counter;
    private final Cell[][] cells;

    private Tuple2<double[], long[]>[][] aggregatedValuesX;
    private Tuple2<double[], long[]>[][] aggregatedValuesY;


    private AdaptiveGridAggregated(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, List<Pair> samples){
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
        cells = new Cell[cellsInXAxis][cellsInYAxis];
    }

    public void initializeMap(int[][] counter){
        this.counter=counter;

        for (int i = 0; i < counter.length; i++) {
            for (int j = 0; j < counter[0].length; j++) {
                if(counter[i][j]!=0){
                    cells[i][j]= Cell.newCell(counter[i][j]);
                    counter[i][j]= 0;
                }
            }
        }
    }

    public void putPair(Pair pair){
        cells[getXStripeId(pair.getX())][getYStripeId(pair.getY())].insert(pair, counter[getXStripeId(pair.getX())][getYStripeId(pair.getY())]);
        counter[getXStripeId(pair.getX())][getYStripeId(pair.getY())]++;
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

    public static AdaptiveGridAggregated newAdaptiveGridAggregated(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, List<Pair> samples){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new AdaptiveGridAggregated(rectangle, cellsInXAxis, cellsInYAxis, samples);
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
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if(counter[i][j]!=0) {
                    Algorithms.sortPairsByX(cells[i][j].getPairs());
                }
            }
        }
    }

    public void sortPairsInCellsByY(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if(counter[i][j]!=0) {
                    Arrays.sort(cells[i][j].getPairs(), new Comparator<Pair>() {
                        /**
                         * {@inheritDoc}
                         */
                        @Override
                        public int compare(Pair pair1, Pair pair2) {
                            int compareFirst = Double.compare(pair1.getY(), pair2.getY());
                            return compareFirst != 0 ? compareFirst : Double.compare(pair1.getX(), pair2.getX());
                        }
                    });
                }
            }
        }
    }

    public void aggregateValuesOnX(){
        aggregatedValuesX = new Tuple2[cellsInXAxis][cellsInYAxis];// HashMap<>(((int)Math.ceil(cells.size() / 0.75)));

        for (int i1 = 0; i1 < cells.length; i1++) {
            for (int j1 = 0; j1 < cells[0].length; j1++) {
                if(cells[i1][j1]!=null) {
                    Pair[] v = cells[i1][j1].getPairs();
                    int distinctCount = 1;
                    for (int i = 1; i < v.length; i++) {
                        if (Double.compare(v[i].getX(), v[i - 1].getX()) != 0) {
                            distinctCount++;
                        }
                    }

                    double[] values = new double[distinctCount];
                    long[] valuesFreq = new long[distinctCount];

                    values[0] = v[0].getX();
                    valuesFreq[0] = v.length;
                    int position = 1;

                    int valueDistinct = 1;
                    for (int i = 1; i < v.length; i++) {
                        if (Double.compare(v[i].getX(), v[i - 1].getX()) != 0) {
                            values[position] = v[i].getX();
                            valuesFreq[position] = valuesFreq[position - 1] - valueDistinct;
                            position++;
                            valueDistinct = 1;
                        } else {
                            valueDistinct++;
                        }
                    }
                    aggregatedValuesX[i1][j1] = new Tuple2(values, valuesFreq);
                }
            }
        }
    }

    public void aggregateValuesOnY(){
        aggregatedValuesY = new Tuple2[cellsInXAxis][cellsInYAxis];
        for (int i1 = 0; i1 < cells.length; i1++) {
            for (int j1 = 0; j1 < cells[0].length; j1++) {
                if(cells[i1][j1]!=null) {
                    Pair[] v = cells[i1][j1].getPairs();
                    int distinctCount = 1;
                    for (int i = 1; i < v.length; i++) {
                        if (Double.compare(v[i].getY(), v[i - 1].getY()) != 0) {
                            distinctCount++;
                        }
                    }

                    double[] values = new double[distinctCount];
                    long[] valuesFreq = new long[distinctCount];

                    values[0] = v[0].getY();
                    valuesFreq[0] = v.length;
                    int position = 1;

                    int valueDistinct = 1;
                    for (int i = 1; i < v.length; i++) {
                        if (Double.compare(v[i].getY(), v[i - 1].getY()) != 0) {
                            values[position] = v[i].getY();
                            valuesFreq[position] = valuesFreq[position - 1] - valueDistinct;
                            position++;
                            valueDistinct = 1;
                        } else {
                            valueDistinct++;
                        }
                    }
                    aggregatedValuesY[i1][j1] = new Tuple2(values, valuesFreq);
                }
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Tuple2<double[],long[]>[][] getAggregatedValuesX() {
        return aggregatedValuesX;
    }

    public Tuple2<double[],long[]>[][] getAggregatedValuesY() {
        return aggregatedValuesY;
    }

    public String cellsStats() {

        int nonEmpty = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if(cells[i][j]!=null){
                    nonEmpty++;
                }
            }
        }

        List<Integer> numbers = new ArrayList<>(nonEmpty);
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                Cell p = cells[i][j];
                if(p!=null){
                    numbers.add(p.getPairs().length);
                }
            }
        }

        double mean = numbers.stream().mapToDouble(i->i).average().orElse(Integer.MIN_VALUE);
        double min = numbers.stream().mapToDouble(i->i).min().orElse(Integer.MIN_VALUE);
        double max = numbers.stream().mapToDouble(i->i).max().orElse(Integer.MIN_VALUE);
        double variance = numbers.stream().mapToDouble(i->i).map(i -> Math.pow(i - mean, 2))
                .average()
                .orElse(Integer.MIN_VALUE);
        double stdDev = Math.sqrt(variance);
        return nonEmpty+","+(int) min + "," +(int) max + "," + stdDev;
    }
}
