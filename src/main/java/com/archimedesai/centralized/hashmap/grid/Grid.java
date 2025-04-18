package com.archimedesai.centralized.hashmap.grid;

import com.archimedesai.Pair;
import com.archimedesai.algorithms.Algorithms;
import com.archimedesai.shapes.Rectangle;

import java.util.*;

public class Grid {

    private final Rectangle rectangle;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private final double x;
    private final double y;

    private HashMap<Integer, Integer> counter;
    private HashMap<Integer, Pair[]> cells;

    private Grid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX()) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY()) / cellsInYAxis;
        System.out.println("x: "+x +" y:"+y);
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

    public boolean checkIfEverythingIsSortedByY(){
        cells.forEach((k,v)->{
            if(v.length==2036){
                for (Pair pair : v) {
                    System.out.println(pair);
                }
            }
        });
        return false;
//        boolean resp = true;
//        for (Pair[] value : cells.values()) {
//            for (int i = 0; i < value.length-1; i++) {
//                for (int j = i+1; j < value.length; j++) {
//                    if(Double.compare(value[i].getY(), value[j].getY()) ==1){
//                        resp = false;
////                        System.out.println(value[i-2].getY()+" "+value[i-1].getY()+" "+value[i].getY()+" "+ value[i+1].getY()+" "+value[i+2].getY()+" "+value[i+3].getY());
////                        System.out.println(value[i-2].getX()+" "+value[i-1].getX()+" "+value[i].getX()+" "+ value[i+1].getX()+" "+ value[i+2].getX()+" "+ value[i+3].getX());
//                        System.out.println(i+" "+j+" "+value.length);
////                        System.out.println(i +" "+(i+1)+" "+value.length);
////                        break;
//                    }
//                }
//            }
//        }
//
//        return resp;
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
