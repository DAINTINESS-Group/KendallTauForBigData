package grarchimedesai.archimedesai.centralized.grid;

import grarchimedesai.archimedesai.Pair;
import grarchimedesai.archimedesai.algorithms.Algorithms;
import grarchimedesai.archimedesai.shapes.Rectangle;

import java.util.*;

public class Grid {

    private final Rectangle rectangle;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private final double x;
    private final double y;

    private int[][] counter;
    private final Cell[][] cells;

    private Grid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX()) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY()) / cellsInYAxis;
        cells = new Cell[cellsInXAxis][cellsInYAxis];
        System.out.println("x: "+x +" y:"+y);
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

//        for (int i = 0; i < counter.length; i++) {
//            for (int j = 0; j < counter[0].length; j++) {
//                counter[i][j]= 0;
//            }
//        }
    }

    public void putPair(Pair pair){
        cells[getXStripeId(pair.getX())][getYStripeId(pair.getY())].insert(pair, counter[getXStripeId(pair.getX())][getYStripeId(pair.getY())]);
        counter[getXStripeId(pair.getX())][getYStripeId(pair.getY())]++;
    }

//    public void clearCounter(){
//        counter.clear();
//        counter = null;
//    }

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


    public Cell[][] getCells() {
        return cells;
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
