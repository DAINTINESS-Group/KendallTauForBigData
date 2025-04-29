package grarchimedesai.archimedesai.centralized;

import grarchimedesai.archimedesai.Pair;
import grarchimedesai.archimedesai.algorithms.Algorithms;
import grarchimedesai.archimedesai.centralized.grid.Cell;
import grarchimedesai.archimedesai.centralized.grid.Grid;
import grarchimedesai.archimedesai.shapes.Point;
import grarchimedesai.archimedesai.shapes.Rectangle;
import org.junit.jupiter.api.Test;
import scala.Tuple2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class RegularGridTest {
    @Test
    void regularGridGaussianSet() {

        final String filePath = "./src/main/resources/gaussian.csv";
        final int xIndex = 0;
        final int yIndex = 1;
        final int cellsInXAxis = 50;
        final int cellsInYAxis = 50;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 1;
        final double maxY = 1;

        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            long t1 = System.currentTimeMillis();
            long lineCount = 0;

            long[] concordantsDiscordants = new long[4];

            int[][] histogram = new int[cellsInXAxis][cellsInYAxis];

            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(minX,minY),Point.newPoint(maxX,maxY)),cellsInXAxis, cellsInYAxis);

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    String[] vals = line.split(delimiter);
                    histogram[grid.getXStripeId(Double.parseDouble(vals[xIndex]))][grid.getYStripeId(Double.parseDouble(vals[yIndex]))]++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            grid.initializeMap(histogram);

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] vals = line.split(delimiter);
                    grid.putPair(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            long t2 = System.currentTimeMillis();
            System.out.println("Data loading: " + (t2-t1) + " ms");

            Cell[][] data = grid.getCells();

            grid.sortPairsInCellsByX();
            for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
                for (int yc = grid.getCellsInYAxis() - 1; yc > 0; yc--) {
                    Cell c1 = data[xc][yc];
                    if(c1!=null){
                        for (int y = 0; y < yc; y++) {
                            Cell c2 = data[xc][y];
                            if(c2!=null){
                                long[] res = Algorithms.southTile(c1.getPairs(),c2.getPairs());
                                concordantsDiscordants[0] += res[0];
                                concordantsDiscordants[1] += res[1];
                            }
                        }
                    }
                }
            }

            long t3 = System.currentTimeMillis();
            System.out.println("South tiles processing: " + (t3-t2) + " ms");

            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    Cell v = data[i][j];
                    if(v!=null){
                        Tuple2<Pair[],long[]> tuple2 = Algorithms.apacheCommons(v.getPairs());
                        long[] res = tuple2._2;
                        v.replace(tuple2._1);
                        concordantsDiscordants[0] += res[0];
                        concordantsDiscordants[1] += res[1];
                        concordantsDiscordants[2] += res[2];
                        concordantsDiscordants[3] += res[3];
                    }
                }
            }

            long t4 = System.currentTimeMillis();
            System.out.println("Inter cell processing: " + (t4-t3) + " ms");

            for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
                for (int xc = 0; xc < grid.getCellsInXAxis()-1; xc++) {
                    Cell c1 = data[xc][yc];
                    if(c1!=null){
                        for (int x = xc+1; x < grid.getCellsInXAxis(); x++) {
                            Cell c2 = data[x][yc];
                            if(c2!=null){

                                long[] res = Algorithms.eastTile(c1.getPairs(),c2.getPairs());
                                concordantsDiscordants[0] += res[0];
                                concordantsDiscordants[2] += res[1];
                            }
                        }
                    }
                }
            }

            long t5 = System.currentTimeMillis();
            System.out.println("East tiles processing: " + (t5-t4) + " ms");

            concordantsDiscordants[0] = concordantsDiscordants[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());

            long t6 = System.currentTimeMillis();
            System.out.println("Histogram processing: " + (t6-t5) + " ms");

            long concordants = ((lineCount*(lineCount-1))/2)-concordantsDiscordants[0]-concordantsDiscordants[1]-concordantsDiscordants[2]-concordantsDiscordants[3];
            double tau = (concordants-concordantsDiscordants[0])/Math.sqrt((double)(concordants+concordantsDiscordants[0]+concordantsDiscordants[1])*(concordants+concordantsDiscordants[0]+concordantsDiscordants[2]));
            long elapsedtime = ((System.currentTimeMillis() - t1));

            System.out.println("tau is: " + tau);
            long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            System.out.println("Elapsed time: " + elapsedtime + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }

    @Test
    void regularGridSierpinskiSet() {

        final String filePath = "./src/main/resources/sierpinski.csv";
        final int xIndex = 0;
        final int yIndex = 1;
        final int cellsInXAxis = 50;
        final int cellsInYAxis = 50;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 1;
        final double maxY = 1;

        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long t1 = System.currentTimeMillis();
        long lineCount = 0;

        long[] concordantsDiscordants = new long[4];

        int[][] histogram = new int[cellsInXAxis][cellsInYAxis];

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(minX,minY),Point.newPoint(maxX,maxY)),cellsInXAxis, cellsInYAxis);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] vals = line.split(delimiter);
                histogram[grid.getXStripeId(Double.parseDouble(vals[xIndex]))][grid.getYStripeId(Double.parseDouble(vals[yIndex]))]++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        grid.initializeMap(histogram);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(delimiter);
                grid.putPair(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        long t2 = System.currentTimeMillis();
        System.out.println("Data loading: " + (t2-t1) + " ms");

        Cell[][] data = grid.getCells();

        grid.sortPairsInCellsByX();
        for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
            for (int yc = grid.getCellsInYAxis() - 1; yc > 0; yc--) {
                Cell c1 = data[xc][yc];
                if(c1!=null){
                    for (int y = 0; y < yc; y++) {
                        Cell c2 = data[xc][y];
                        if(c2!=null){
                            long[] res = Algorithms.southTile(c1.getPairs(),c2.getPairs());
                            concordantsDiscordants[0] += res[0];
                            concordantsDiscordants[1] += res[1];
                        }
                    }
                }
            }
        }

        long t3 = System.currentTimeMillis();
        System.out.println("South tiles processing: " + (t3-t2) + " ms");

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                Cell v = data[i][j];
                if(v!=null){
                    Tuple2<Pair[],long[]> tuple2 = Algorithms.apacheCommons(v.getPairs());
                    long[] res = tuple2._2;
                    v.replace(tuple2._1);
                    concordantsDiscordants[0] += res[0];
                    concordantsDiscordants[1] += res[1];
                    concordantsDiscordants[2] += res[2];
                    concordantsDiscordants[3] += res[3];
                }
            }
        }

        long t4 = System.currentTimeMillis();
        System.out.println("Inter cell processing: " + (t4-t3) + " ms");

        for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
            for (int xc = 0; xc < grid.getCellsInXAxis()-1; xc++) {
                Cell c1 = data[xc][yc];
                if(c1!=null){
                    for (int x = xc+1; x < grid.getCellsInXAxis(); x++) {
                        Cell c2 = data[x][yc];
                        if(c2!=null){

                            long[] res = Algorithms.eastTile(c1.getPairs(),c2.getPairs());
                            concordantsDiscordants[0] += res[0];
                            concordantsDiscordants[2] += res[1];
                        }
                    }
                }
            }
        }

        long t5 = System.currentTimeMillis();
        System.out.println("East tiles processing: " + (t5-t4) + " ms");

        concordantsDiscordants[0] = concordantsDiscordants[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());

        long t6 = System.currentTimeMillis();
        System.out.println("Histogram processing: " + (t6-t5) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concordantsDiscordants[0]-concordantsDiscordants[1]-concordantsDiscordants[2]-concordantsDiscordants[3];
        double tau = (concordants-concordantsDiscordants[0])/Math.sqrt((double)(concordants+concordantsDiscordants[0]+concordantsDiscordants[1])*(concordants+concordantsDiscordants[0]+concordantsDiscordants[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + elapsedtime + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }

    @Test
    void regularGridTaxiSet() {

        final String filePath = "./src/main/resources/taxi.csv";
        final int xIndex = 0;
        final int yIndex = 2;
        final int cellsInXAxis = 50;
        final int cellsInYAxis = 50;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 6.40172993E8;
        final double maxY = 861611.06;

        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long t1 = System.currentTimeMillis();
        long lineCount = 0;

        long[] concordantsDiscordants = new long[4];

        int[][] histogram = new int[cellsInXAxis][cellsInYAxis];

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(minX,minY),Point.newPoint(maxX,maxY)),cellsInXAxis, cellsInYAxis);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] vals = line.split(delimiter);
                histogram[grid.getXStripeId(Double.parseDouble(vals[xIndex]))][grid.getYStripeId(Double.parseDouble(vals[yIndex]))]++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        grid.initializeMap(histogram);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(delimiter);
                grid.putPair(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        long t2 = System.currentTimeMillis();
        System.out.println("Data loading: " + (t2-t1) + " ms");

        Cell[][] data = grid.getCells();

        grid.sortPairsInCellsByX();
        for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
            for (int yc = grid.getCellsInYAxis() - 1; yc > 0; yc--) {
                Cell c1 = data[xc][yc];
                if(c1!=null){
                    for (int y = 0; y < yc; y++) {
                        Cell c2 = data[xc][y];
                        if(c2!=null){
                            long[] res = Algorithms.southTile(c1.getPairs(),c2.getPairs());
                            concordantsDiscordants[0] += res[0];
                            concordantsDiscordants[1] += res[1];
                        }
                    }
                }
            }
        }

        long t3 = System.currentTimeMillis();
        System.out.println("South tiles processing: " + (t3-t2) + " ms");

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                Cell v = data[i][j];
                if(v!=null){
                    Tuple2<Pair[],long[]> tuple2 = Algorithms.apacheCommons(v.getPairs());
                    long[] res = tuple2._2;
                    v.replace(tuple2._1);
                    concordantsDiscordants[0] += res[0];
                    concordantsDiscordants[1] += res[1];
                    concordantsDiscordants[2] += res[2];
                    concordantsDiscordants[3] += res[3];
                }
            }
        }

        long t4 = System.currentTimeMillis();
        System.out.println("Inter cell processing: " + (t4-t3) + " ms");

        for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
            for (int xc = 0; xc < grid.getCellsInXAxis()-1; xc++) {
                Cell c1 = data[xc][yc];
                if(c1!=null){
                    for (int x = xc+1; x < grid.getCellsInXAxis(); x++) {
                        Cell c2 = data[x][yc];
                        if(c2!=null){

                            long[] res = Algorithms.eastTile(c1.getPairs(),c2.getPairs());
                            concordantsDiscordants[0] += res[0];
                            concordantsDiscordants[2] += res[1];
                        }
                    }
                }
            }
        }

        long t5 = System.currentTimeMillis();
        System.out.println("East tiles processing: " + (t5-t4) + " ms");

        concordantsDiscordants[0] = concordantsDiscordants[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());

        long t6 = System.currentTimeMillis();
        System.out.println("Histogram processing: " + (t6-t5) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concordantsDiscordants[0]-concordantsDiscordants[1]-concordantsDiscordants[2]-concordantsDiscordants[3];
        double tau = (concordants-concordantsDiscordants[0])/Math.sqrt((double)(concordants+concordantsDiscordants[0]+concordantsDiscordants[1])*(concordants+concordantsDiscordants[0]+concordantsDiscordants[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + elapsedtime + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }

    @Test
    void regularGridRadiationSet() {

        final String filePath = "./src/main/resources/radiation.csv";
        final int xIndex = 0;
        final int yIndex = 2;
        final int cellsInXAxis = 50;
        final int cellsInYAxis = 50;
        final String delimiter =",";

        final double minX = 3.5;
        final double minY = 1.0;
        final double maxX = 90.1;
        final double maxY = 1351.1;

        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long t1 = System.currentTimeMillis();
        long lineCount = 0;

        long[] concordantsDiscordants = new long[4];

        int[][] histogram = new int[cellsInXAxis][cellsInYAxis];

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(minX,minY),Point.newPoint(maxX,maxY)),cellsInXAxis, cellsInYAxis);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] vals = line.split(delimiter);
                histogram[grid.getXStripeId(Double.parseDouble(vals[xIndex]))][grid.getYStripeId(Double.parseDouble(vals[yIndex]))]++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        grid.initializeMap(histogram);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(delimiter);
                grid.putPair(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        long t2 = System.currentTimeMillis();
        System.out.println("Data loading: " + (t2-t1) + " ms");

        Cell[][] data = grid.getCells();

        grid.sortPairsInCellsByX();
        for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
            for (int yc = grid.getCellsInYAxis() - 1; yc > 0; yc--) {
                Cell c1 = data[xc][yc];
                if(c1!=null){
                    for (int y = 0; y < yc; y++) {
                        Cell c2 = data[xc][y];
                        if(c2!=null){
                            long[] res = Algorithms.southTile(c1.getPairs(),c2.getPairs());
                            concordantsDiscordants[0] += res[0];
                            concordantsDiscordants[1] += res[1];
                        }
                    }
                }
            }
        }

        long t3 = System.currentTimeMillis();
        System.out.println("South tiles processing: " + (t3-t2) + " ms");

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                Cell v = data[i][j];
                if(v!=null){
                    Tuple2<Pair[],long[]> tuple2 = Algorithms.apacheCommons(v.getPairs());
                    long[] res = tuple2._2;
                    v.replace(tuple2._1);
                    concordantsDiscordants[0] += res[0];
                    concordantsDiscordants[1] += res[1];
                    concordantsDiscordants[2] += res[2];
                    concordantsDiscordants[3] += res[3];
                }
            }
        }

        long t4 = System.currentTimeMillis();
        System.out.println("Inter cell processing: " + (t4-t3) + " ms");

        for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
            for (int xc = 0; xc < grid.getCellsInXAxis()-1; xc++) {
                Cell c1 = data[xc][yc];
                if(c1!=null){
                    for (int x = xc+1; x < grid.getCellsInXAxis(); x++) {
                        Cell c2 = data[x][yc];
                        if(c2!=null){

                            long[] res = Algorithms.eastTile(c1.getPairs(),c2.getPairs());
                            concordantsDiscordants[0] += res[0];
                            concordantsDiscordants[2] += res[1];
                        }
                    }
                }
            }
        }

        long t5 = System.currentTimeMillis();
        System.out.println("East tiles processing: " + (t5-t4) + " ms");

        concordantsDiscordants[0] = concordantsDiscordants[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());

        long t6 = System.currentTimeMillis();
        System.out.println("Histogram processing: " + (t6-t5) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concordantsDiscordants[0]-concordantsDiscordants[1]-concordantsDiscordants[2]-concordantsDiscordants[3];
        double tau = (concordants-concordantsDiscordants[0])/Math.sqrt((double)(concordants+concordantsDiscordants[0]+concordantsDiscordants[1])*(concordants+concordantsDiscordants[0]+concordantsDiscordants[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + elapsedtime + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }



}