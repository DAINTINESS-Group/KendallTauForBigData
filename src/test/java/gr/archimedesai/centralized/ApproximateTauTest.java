package gr.archimedesai.centralized;

import gr.archimedesai.Pair;
import gr.archimedesai.algorithms.Algorithms;
import gr.archimedesai.centralized.approximate.AdaptiveGrid;
import gr.archimedesai.shapes.Point;
import gr.archimedesai.shapes.Rectangle;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


class ApproximateTauTest {
    @Test
    void approximateGaussianSet() {
        final String filePath = "./src/main/resources/gaussian.csv";
        final String sampleFilePath = "./src/main/resources/samples/gaussian.csv";
        final int xIndex = 0;
        final int yIndex = 1;
        final int cellsInXAxis = 200;
        final int cellsInYAxis = 200;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 1;
        final double maxY = 1;

        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            long t1 = System.currentTimeMillis();
            long lineCount = 0;

            List<Pair> samplesPairs = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(sampleFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] vals = line.split(delimiter);
                    samplesPairs.add(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            AdaptiveGrid grid = AdaptiveGrid.newGrid(Rectangle.newRectangle(Point.newPoint(minX, minY),Point.newPoint(maxX, maxY)),cellsInXAxis,cellsInYAxis, samplesPairs);

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    String[] vals = line.split(delimiter);
                    grid.updateCell(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Sampling + Data loading: "+(System.currentTimeMillis()-t1)+" ms");


            long t2 = System.currentTimeMillis();

            double discordants = Algorithms.approximate(grid.getCells(), grid.getCellsInXAxis(), grid.getCellsInYAxis());
            double concordants = ((lineCount*(lineCount-1))/2d)-discordants;
            double tau = (concordants - discordants) /(concordants+discordants);

        System.out.println("Approximate processing "+(System.currentTimeMillis()-t2)+" ms");

        long elapsedtime = System.currentTimeMillis();

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + (elapsedtime-t1) + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }

    @Test
    void approximateSierpinskiSet() {
        final String filePath = "./src/main/resources/sierpinski.csv";
        final String sampleFilePath = "./src/main/resources/samples/sierpinski.csv";
        final int xIndex = 0;
        final int yIndex = 1;
        final int cellsInXAxis = 200;
        final int cellsInYAxis = 200;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 1;
        final double maxY = 1;

        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long t1 = System.currentTimeMillis();
        long lineCount = 0;

        List<Pair> samplesPairs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sampleFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(delimiter);
                samplesPairs.add(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        AdaptiveGrid grid = AdaptiveGrid.newGrid(Rectangle.newRectangle(Point.newPoint(minX, minY),Point.newPoint(maxX, maxY)),cellsInXAxis,cellsInYAxis, samplesPairs);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] vals = line.split(delimiter);
                grid.updateCell(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sampling + Data loading: "+(System.currentTimeMillis()-t1)+" ms");


        long t2 = System.currentTimeMillis();

        double discordants = Algorithms.approximate(grid.getCells(), grid.getCellsInXAxis(), grid.getCellsInYAxis());
        double concordants = ((lineCount*(lineCount-1))/2d)-discordants;
        double tau = (concordants - discordants) /(concordants+discordants);

        System.out.println("Approximate processing "+(System.currentTimeMillis()-t2)+" ms");

        long elapsedtime = System.currentTimeMillis();

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + (elapsedtime-t1) + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");

    }

    @Test
    void approximateTaxiSet() {
        final String filePath = "./src/main/resources/taxi.csv";
        final String sampleFilePath = "./src/main/resources/samples/taxi.csv";
        final int xIndex = 0;
        final int yIndex = 2;
        final int cellsInXAxis = 200;
        final int cellsInYAxis = 200;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 6.40172993E8;
        final double maxY = 861611.06;

        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long t1 = System.currentTimeMillis();
        long lineCount = 0;

        List<Pair> samplesPairs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sampleFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(delimiter);
                samplesPairs.add(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        AdaptiveGrid grid = AdaptiveGrid.newGrid(Rectangle.newRectangle(Point.newPoint(minX, minY),Point.newPoint(maxX, maxY)),cellsInXAxis,cellsInYAxis, samplesPairs);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] vals = line.split(delimiter);
                grid.updateCell(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sampling + Data loading: "+(System.currentTimeMillis()-t1)+" ms");


        long t2 = System.currentTimeMillis();

        double discordants = Algorithms.approximate(grid.getCells(), grid.getCellsInXAxis(), grid.getCellsInYAxis());
        double concordants = ((lineCount*(lineCount-1))/2d)-discordants;
        double tau = (concordants - discordants) /(concordants+discordants);

        System.out.println("Approximate processing "+(System.currentTimeMillis()-t2)+" ms");

        long elapsedtime = System.currentTimeMillis();

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + (elapsedtime-t1) + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");

    }

    @Test
    void approximateRadiationSet() {
        final String filePath = "./src/main/resources/radiation.csv";
        final String sampleFilePath = "./src/main/resources/samples/radiation.csv";
        final int xIndex = 0;
        final int yIndex = 2;
        final int cellsInXAxis = 200;
        final int cellsInYAxis = 200;
        final String delimiter =",";

        final double minX = 3.5;
        final double minY = 1.0;
        final double maxX = 90.1;
        final double maxY = 1351.1;

        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long t1 = System.currentTimeMillis();
        long lineCount = 0;

        List<Pair> samplesPairs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(sampleFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(delimiter);
                samplesPairs.add(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        AdaptiveGrid grid = AdaptiveGrid.newGrid(Rectangle.newRectangle(Point.newPoint(minX, minY),Point.newPoint(maxX, maxY)),cellsInXAxis,cellsInYAxis, samplesPairs);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] vals = line.split(delimiter);
                grid.updateCell(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sampling + Data loading: "+(System.currentTimeMillis()-t1)+" ms");


        long t2 = System.currentTimeMillis();

        double discordants = Algorithms.approximate(grid.getCells(), grid.getCellsInXAxis(), grid.getCellsInYAxis());
        double concordants = ((lineCount*(lineCount-1))/2d)-discordants;
        double tau = (concordants - discordants) /(concordants+discordants);

        System.out.println("Approximate processing "+(System.currentTimeMillis()-t2)+" ms");

        long elapsedtime = System.currentTimeMillis();

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + (elapsedtime-t1) + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");

    }
}