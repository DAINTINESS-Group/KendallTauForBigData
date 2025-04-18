package grarchimedesai.archimedesai.centralized.approximate;

import grarchimedesai.archimedesai.Pair;
import grarchimedesai.archimedesai.algorithms.Algorithms;
import grarchimedesai.archimedesai.shapes.Point;
import grarchimedesai.archimedesai.shapes.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class MainAdaptiveApproximate {
    public static void main(String[] args) {

        String filePath = args[0];
        final int xIndex = Integer.parseInt(args[1]);
        final int yIndex = Integer.parseInt(args[2]);
        final String sampleFilePath = args[11];

        final double actual = Double.parseDouble(args[12]);
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        try {
            File file = new File(args[10]);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            long t1 = System.currentTimeMillis();
            long lineCount = 0;

            List<Pair> samplesPairs = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(sampleFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] vals = line.split(args[3]);
                    samplesPairs.add(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            AdaptiveGrid grid = AdaptiveGrid.newGrid(Rectangle.newRectangle(Point.newPoint(Double.parseDouble(args[4]),Double.parseDouble(args[5])),Point.newPoint(Double.parseDouble(args[6]),Double.parseDouble(args[7]))),Integer.parseInt(args[8]),Integer.parseInt(args[9]), samplesPairs);

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    String[] vals = line.split(args[3]);
                    grid.updateCell(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            long t2 = System.currentTimeMillis();

            double discordants = Algorithms.approximate(grid.getCells(), grid.getCellsInXAxis(), grid.getCellsInYAxis());
            double concordants = ((lineCount*(lineCount-1))/2d)-discordants;
            double tau = (concordants - discordants) /(concordants+discordants);

            long elapsedtime = System.currentTimeMillis();

            System.out.println(lineCount+ " "+concordants);
            System.out.println("tau is: " + tau);
            long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            bw.write(Integer.parseInt(args[8])+","+((elapsedtime-t1)/1000)+ ","+((t2-t1)/1000)+","+((elapsedtime-t2)/1000)+","+(afterUsedMem-beforeUsedMem)/(1024*1024)+","+tau+","+actual+","+Math.abs(actual-tau)+","+Math.abs(actual-tau)/Math.abs(actual)+"\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
