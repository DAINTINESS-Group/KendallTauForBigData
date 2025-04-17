package gr.archimedesai.algorithms;

import gr.archimedesai.centralized.approximate.Grid;
import gr.archimedesai.shapes.Point;
import gr.archimedesai.shapes.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    @Test
    void approximate() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 16; i++) {
            map.put(i, 4);
        }
        System.out.println(Algorithms.approximate(map,4,4));
        System.out.println(Algorithms.discordantCells(map,4,4));
    }

    @Test
    void approximate2DArray() {
        int[][] cells = new int[4][4];

        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[0].length; j++){
                cells[i][j] = 4;
            }
        }
        System.out.println(cells.length + " "+ cells[0].length);
        System.out.println(Algorithms.approximate(cells, cells.length, cells[0].length));
        System.out.println(Algorithms.discordantCells(cells, cells.length, cells[0].length));
    }

    @Test
    void gridClassApproximate() {

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0,0),Point.newPoint(10,10)), 2, 2);

        grid.updateCell(2.5,4);
        grid.updateCell(3.5,4);
        grid.updateCell(1.5,3);
        grid.updateCell(0,1.5);

        grid.updateCell(7,4);
        grid.updateCell(8,4);
        grid.updateCell(9,3);
        grid.updateCell(9,1.5);

        grid.updateCell(2.5,6);
        grid.updateCell(3.5,7);
        grid.updateCell(1.5,8);
        grid.updateCell(0,8);

        grid.updateCell(7,9);
        grid.updateCell(8,9);
        grid.updateCell(9,9);
        grid.updateCell(9,9);
        for (int i = 0; i < grid.getCells().length; i++) {
            for (int j = 0; j < grid.getCells()[0].length; j++) {
                System.out.println(grid.getCells()[i][j]);
            }
        }

        System.out.println(Algorithms.approximate(grid.getCells(), grid.getCells().length, grid.getCells()[0].length));
//        System.out.println(Algorithms.discordantCells(grid.getCells(), grid.getCells().length, grid.getCells()[0].length));

    }

    @Test
    void testTwoApproximates() {
        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0,0),Point.newPoint(10,10)), 324, 4);
        Random random = new Random();

        for (int i = 0; i < 1000000; i++) {
            grid.updateCell(random.nextInt(9)* random.nextDouble(), random.nextInt(9)* random.nextDouble());
        }

//        grid.getCells()[0][0]=3;
//        grid.getCells()[0][1]=1;
//        grid.getCells()[0][2]=0;
//        grid.getCells()[1][0]=2;
//        grid.getCells()[1][1]=1;
//        grid.getCells()[1][2]=0;
//        grid.getCells()[2][0]=0;
//        grid.getCells()[2][1]=1;
//        grid.getCells()[2][2]=2;

        System.out.println(grid.getCells()[0][0]);
        System.out.println(grid.getCells()[0][1]);
        System.out.println(grid.getCells()[0][2]);
        System.out.println(grid.getCells()[1][0]);
        System.out.println(grid.getCells()[1][1]);
        System.out.println(grid.getCells()[1][2]);
        System.out.println(grid.getCells()[2][0]);
        System.out.println(grid.getCells()[2][1]);
        System.out.println(grid.getCells()[2][2]);


//        System.out.println(Algorithms.approximate1(grid.getCells(),grid.getCellsInXAxis(), grid.getCellsInYAxis()));
//        System.out.println(Algorithms.approximate2(grid.getCells(),grid.getCellsInXAxis(), grid.getCellsInYAxis()));
        System.out.println(Algorithms.discordantCells(grid.getCells(),grid.getCellsInXAxis(), grid.getCellsInYAxis()));

    }

    @Test
    void anotherApproximate() {
        int i = 0*(-1)/4;
        System.out.println(i);
    }

}