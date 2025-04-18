package grarchimedesai.archimedesai.centralized.grid;

import grarchimedesai.archimedesai.Pair;

public class Cell {

    private Pair[] pairs;

    private Cell(int arraySize) {
        this.pairs = new Pair[arraySize];
    }

    public void insert(Pair pair, int position) {
        pairs[position]= pair;
    }

    public static Cell newCell(int arraySize) {
        return new Cell(arraySize);
    }

    public Pair[] getPairs() {
        return pairs;
    }

    public void replace(Pair[] pairs){
        this.pairs = pairs;
    }

}
