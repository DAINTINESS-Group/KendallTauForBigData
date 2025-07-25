package gr.archimedesai.centralized.hashmap;

import gr.archimedesai.Pair;
import gr.archimedesai.algorithms.Algorithms;
import gr.archimedesai.centralized.hashmap.grid.AdaptiveGrid;
import gr.archimedesai.shapes.Point;
import gr.archimedesai.shapes.Rectangle;
import scala.Tuple2;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MainAdaptiveGrid {

    public static void main(String[] args) {

        String filePath = args[0];
        final int xIndex = Integer.parseInt(args[1]);
        final int yIndex = Integer.parseInt(args[2]);
        final String sampleFilePath = args[11];

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

            HashMap<Integer, Integer> histogram = new HashMap<>();

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

            AdaptiveGrid grid = AdaptiveGrid.newAdaptiveGrid(Rectangle.newRectangle(Point.newPoint(Double.parseDouble(args[4]),Double.parseDouble(args[5])),Point.newPoint(Double.parseDouble(args[6]),Double.parseDouble(args[7]))),Integer.parseInt(args[8]),Integer.parseInt(args[9]),samplesPairs);

            long prosorino1=System.currentTimeMillis();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lineCount++;
                    String[] vals = line.split(args[3]);
                    int cellId = grid.getCellId(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex]));
                    histogram.compute(cellId, (key, value) -> (value==null)?1:(value+1));
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

            long t2 = System.currentTimeMillis();
            System.out.println("Data read: " + (t2-t1) + " ms");

            Map<Integer, Pair[]> data = grid.getCells();

            grid.sortPairsInCellsByX();
            for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
                for (int yc = grid.getCellsInYAxis() - 1; yc > 0; yc--) {
                    Pair[] c1 = data.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),null);
                    if(c1!=null){
                        for (int y = 0; y < yc; y++) {
                            Pair[] c2 = data.getOrDefault((grid.getCellIdFromXcYc(xc, y)),null);
                            if(c2!=null){
//                                long e1= System.currentTimeMillis();
                                long[] res = Algorithms.southTile(c1,c2);
//                                System.out.println("c"+grid.getCellIdFromXcYc(xc, yc)+": "+c1.length+ " c"+grid.getCellIdFromXcYc(xc, y)+": "+c2.length+ " time: "+(System.currentTimeMillis()-e1)+ " ms "+"ties: "+res[1]);
                                concordantsDiscordants[0] += res[0];
                                concordantsDiscordants[1] += res[1];
    //                            concordantsDiscordants[2] += res[2];
                            }
                        }
                    }
                }
            }
            System.out.println(Arrays.toString(concordantsDiscordants));

            long t3 = System.currentTimeMillis();
            System.out.println("South tiles processing: " + (t3-t2) + " ms");

            data.forEach((k, v)-> {
                Tuple2<Pair[],long[]> tuple2 = Algorithms.apacheCommons(v);
                long[] res = tuple2._2;
                data.replace(k, tuple2._1);
                concordantsDiscordants[0] += res[0];
                concordantsDiscordants[1] += res[1];
                concordantsDiscordants[2] += res[2];
                concordantsDiscordants[3] += res[3];
            });

            long t4 = System.currentTimeMillis();
            System.out.println("Inter cell processing: " + (t4-t3) + " ms");


//            grid.sortPairsInCellsByY();
            for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
                for (int xc = 0; xc < grid.getCellsInXAxis()-1; xc++) {
                    Pair[] c1 = data.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),null);
                    if(c1!=null){
                        for (int x = xc+1; x < grid.getCellsInXAxis(); x++) {
                            Pair[] c2 = data.getOrDefault((grid.getCellIdFromXcYc(x, yc)),null);
                            if(c2!=null){
                                long[] res = Algorithms.eastTile(c1,c2);
                                concordantsDiscordants[0] += res[0];
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
//
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
            BufferedWriter bwCells = new BufferedWriter(new FileWriter("cells-gridAdaptive-"+grid.getCellsInXAxis()+"-"+grid.getCellsInYAxis()+"-"+path.getFileName()));
            for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
                for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
                    Pair[] pairs = data.get((grid.getCellIdFromXcYc(xc, yc)));
                    int c = -1;
                    if(pairs!=null){
                        c = pairs.length;
                    }
                    if(c!=-1){
                        bwCells.write(xc+"\t"+yc+"\t"+c+"\n");
                    }else{
                        bwCells.write(xc+"\t"+yc+"\t"+0+"\n");
                    }
                }
            }
            bwCells.close();

            bw.write(Integer.parseInt(args[8])+","+elapsedtime+ ","+((t2-t1)/1000)+","+((t3-t2)/1000)+","+((t4-t3)/1000)+","+((t5-t4)/1000)+","+((t6-t5)/1000)+","+grid.cellsStats()+"\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
