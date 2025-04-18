package com.archimedesai.centralized;

import com.archimedesai.Pair;
import com.archimedesai.algorithms.Algorithms;

import java.io.*;
import java.util.Arrays;

public class MainArray {

    public static void main(String[] args) {

        final int xIndex = Integer.parseInt(args[1]);
        final int yIndex = Integer.parseInt(args[2]);
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        try {
            File file = new File(args[4]);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            long t1 = System.currentTimeMillis();
            long lineCount = 0;

            String filePath = args[0];
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                while (br.readLine() != null) {
                    lineCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Pair[] data = new Pair[(int)lineCount];
            int index = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] vals = line.split(args[3]);
                    data[index++] = Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            long t2 = System.currentTimeMillis();
            System.out.println("Data read: " + (t2-t1) + " ms");

            Algorithms.sortPairsByX(data);
            long[] concDisc = Algorithms.apacheCommons(data)._2;
            long t3 = System.currentTimeMillis();
            System.out.println("Knight processing: " + (t3-t2) + " ms");

            long concordants = ((lineCount*(lineCount-1))/2)-concDisc[0]-concDisc[1]-concDisc[2]-concDisc[3];
            double tau = (double)(concordants-concDisc[0])/Math.sqrt((double)(concordants+concDisc[0]+concDisc[1])*(concordants+concDisc[0]+concDisc[2]));
            long elapsedtime = ((System.currentTimeMillis() - t1) / 1000);

            System.out.println(Arrays.toString(concDisc));
            System.out.println("tau is: " + tau);
            long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            bw.write((elapsedtime+","+(t2-t1)/1000+","+ (t3-t2)/1000+","+(afterUsedMem-beforeUsedMem)/(1024*1024)+"\n"));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
