package grarchimedesai.archimedesai.centralized;

import grarchimedesai.archimedesai.Pair;
import grarchimedesai.archimedesai.algorithms.Algorithms;
import grarchimedesai.archimedesai.centralized.grid.Cell;
import grarchimedesai.archimedesai.centralized.grid.Grid;
import grarchimedesai.archimedesai.shapes.Point;
import grarchimedesai.archimedesai.shapes.Rectangle;
import scala.Tuple2;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MainGrid {

    public static void main(String[] args) {

        String filePath = args[0];
        final int xIndex = Integer.parseInt(args[1]);
        final int yIndex = Integer.parseInt(args[2]);
        final int cellsInXAxis = Integer.parseInt(args[8]);
        final int cellsInYAxis = Integer.parseInt(args[9]);
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

            long[] concordantsDiscordants = new long[4];

            int[][] histogram = new int[cellsInXAxis][cellsInYAxis];

            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(Double.parseDouble(args[4]),Double.parseDouble(args[5])),Point.newPoint(Double.parseDouble(args[6]),Double.parseDouble(args[7]))),cellsInXAxis, cellsInYAxis);

            long prosorino1=System.currentTimeMillis();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    String[] vals = line.split(args[3]);
                    histogram[grid.getXStripeId(Double.parseDouble(vals[xIndex]))][grid.getYStripeId(Double.parseDouble(vals[yIndex]))]++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            long prosorino2=System.currentTimeMillis();
            System.out.println("Prosorino2: "+(prosorino2-prosorino1)/1000);

            grid.initializeMap(histogram);
            long prosorino3=System.currentTimeMillis();
            System.out.println("Prosorino3: "+(prosorino3-prosorino2)/1000);

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] vals = line.split(args[3]);
                    grid.putPair(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            long prosorino4=System.currentTimeMillis();
            System.out.println("Prosorino4: "+(prosorino4-prosorino3)/1000);

//            grid.clearCounter();

            long t2 = System.currentTimeMillis();
            System.out.println("Data read: " + (t2-t1) + " ms");

            Cell[][] data = grid.getCells();

            grid.sortPairsInCellsByX();
            for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
                for (int yc = grid.getCellsInYAxis() - 1; yc > 0; yc--) {
                    Cell c1 = data[xc][yc];//data.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),null);
                    if(c1!=null){
                        for (int y = 0; y < yc; y++) {
                            Cell c2 = data[xc][y];//data.getOrDefault((grid.getCellIdFromXcYc(xc, y)),null);
                            if(c2!=null){
                                long[] res = Algorithms.southTile(c1.getPairs(),c2.getPairs());
                                concordantsDiscordants[0] += res[0];
                                concordantsDiscordants[1] += res[1];
    //                            concordantsDiscordants[2] += res[2];
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

            System.out.println(Arrays.toString(concordantsDiscordants));

            long t4 = System.currentTimeMillis();
            System.out.println("Inter cell processing: " + (t4-t3) + " ms");

    //        grid.sortPairsInCellsByY();

            for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
                for (int xc = 0; xc < grid.getCellsInXAxis()-1; xc++) {
                    Cell c1 = data[xc][yc];//data.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),null);
                    if(c1!=null){
                        for (int x = xc+1; x < grid.getCellsInXAxis(); x++) {
                            Cell c2 = data[x][yc];//data.getOrDefault((grid.getCellIdFromXcYc(x, yc)),null);
                            if(c2!=null){

                                long[] res = Algorithms.eastTile(c1.getPairs(),c2.getPairs());
                                concordantsDiscordants[0] += res[0];
    //                            concordantsDiscordants[1] += res[1];
                                concordantsDiscordants[2] += res[1];
                            }
                        }
                    }
                }
            }

            long t5 = System.currentTimeMillis();
            System.out.println("East tiles processing: " + (t5-t4) + " ms");

//            for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
//                for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
//                    Pair[] c = (data.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),null));
//                    if(c!=null){
//                        long cDisc = 0;
//                        for (int xcDisc = xc+1; xcDisc < grid.getCellsInXAxis(); xcDisc++) {
//                            for (int ycDisc = 0; ycDisc < yc; ycDisc++) {
//                                Pair[] c2 = (data.getOrDefault((grid.getCellIdFromXcYc(xcDisc, ycDisc)),null));
//                                if(c2!=null){
//                                    cDisc = cDisc + c2.length;
//                                }
//                            }
//                        }
//                        concordantsDiscordants[0] += cDisc*c.length;
//                    }
//                }
//            }

            concordantsDiscordants[0] = concordantsDiscordants[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());

            long t6 = System.currentTimeMillis();
            System.out.println("Histogram processing: " + (t6-t5) + " ms");

            long concordants = ((lineCount*(lineCount-1))/2)-concordantsDiscordants[0]-concordantsDiscordants[1]-concordantsDiscordants[2]-concordantsDiscordants[3];
            double tau = (concordants-concordantsDiscordants[0])/Math.sqrt((double)(concordants+concordantsDiscordants[0]+concordantsDiscordants[1])*(concordants+concordantsDiscordants[0]+concordantsDiscordants[2]));
            long elapsedtime = ((System.currentTimeMillis() - t1) / 1000);

            System.out.println(Arrays.toString(concordantsDiscordants));
            System.out.println(lineCount+ " "+concordants);
            System.out.println("tau is: " + tau);

            Path path = Paths.get(args[0]);
            BufferedWriter bwCells = new BufferedWriter(new FileWriter("cells-gridRegular-"+grid.getCellsInXAxis()+"-"+grid.getCellsInYAxis()+"-"+path.getFileName()));
            for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
                for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
                    if(data[xc][yc]!=null){
                        Pair[] pairs = data[xc][yc].getPairs();//.get((grid.getCellIdFromXcYc(xc, yc)));
                        bwCells.write(xc+"\t"+yc+"\t"+pairs.length+"\n");
                    }else{
                        bwCells.write(xc+"\t"+yc+"\t"+0+"\n");
                    }
                }
            }
            bwCells.close();
            long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            bw.write(Integer.parseInt(args[8])+","+elapsedtime+ ","+((t2-t1)/1000)+","+((t3-t2)/1000)+","+((t4-t3)/1000)+","+((t5-t4)/1000)+","+((t6-t5)/1000)+","+grid.cellsStats()+","+(afterUsedMem-beforeUsedMem)/(1024*1024)+"\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
