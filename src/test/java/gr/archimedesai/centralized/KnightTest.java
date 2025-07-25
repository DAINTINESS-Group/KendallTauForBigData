package gr.archimedesai.centralized;

import gr.archimedesai.Pair;
import gr.archimedesai.algorithms.Algorithms;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


class KnightTest {
    @Test
    void knightGaussianSet() {
            final int xIndex = 0;
            final int yIndex = 1;
            final String filePath ="./src/main/resources/gaussian.csv";
            final String delimiter = ",";
            long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                long t1 = System.currentTimeMillis();
                long lineCount = 0;

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
                        String[] vals = line.split(delimiter);
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
                System.out.println("Processing with Knight algorithm: " + (t3-t2) + " ms");

                long concordants = ((lineCount*(lineCount-1))/2)-concDisc[0]-concDisc[1]-concDisc[2]-concDisc[3];
                double tau = (double)(concordants-concDisc[0])/Math.sqrt((double)(concordants+concDisc[0]+concDisc[1])*(concordants+concDisc[0]+concDisc[2]));
                long elapsedtime = ((System.currentTimeMillis() - t1));

                System.out.println("tau is: " + tau);
                long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                System.out.println("Elapsed time: " + elapsedtime + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }

    @Test
    void knightSierpinskiSet() {
        final int xIndex = 0;
        final int yIndex = 1;
        final String filePath ="./src/main/resources/sierpinski.csv";
        final String delimiter = ",";
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long t1 = System.currentTimeMillis();
        long lineCount = 0;

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
                String[] vals = line.split(delimiter);
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
        System.out.println("Processing with Knight algorithm: " + (t3-t2) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concDisc[0]-concDisc[1]-concDisc[2]-concDisc[3];
        double tau = (double)(concordants-concDisc[0])/Math.sqrt((double)(concordants+concDisc[0]+concDisc[1])*(concordants+concDisc[0]+concDisc[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + elapsedtime + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }

    @Test
    void knightTaxiSet() {
        final int xIndex = 0;
        final int yIndex = 2;
        final String filePath ="./src/main/resources/taxi.csv";
        final String delimiter = ",";
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long t1 = System.currentTimeMillis();
        long lineCount = 0;

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
                String[] vals = line.split(delimiter);
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
        System.out.println("Processing with Knight algorithm: " + (t3-t2) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concDisc[0]-concDisc[1]-concDisc[2]-concDisc[3];
        double tau = (double)(concordants-concDisc[0])/Math.sqrt((double)(concordants+concDisc[0]+concDisc[1])*(concordants+concDisc[0]+concDisc[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + elapsedtime + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }

    @Test
    void knightRadiationSet() {
        final int xIndex = 0;
        final int yIndex = 2;
        final String filePath ="./src/main/resources/radiation.csv";
        final String delimiter = ",";
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long t1 = System.currentTimeMillis();
        long lineCount = 0;

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
                String[] vals = line.split(delimiter);
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
        System.out.println("Processing with Knight algorithm: " + (t3-t2) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concDisc[0]-concDisc[1]-concDisc[2]-concDisc[3];
        double tau = (double)(concordants-concDisc[0])/Math.sqrt((double)(concordants+concDisc[0]+concDisc[1])*(concordants+concDisc[0]+concDisc[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Elapsed time: " + elapsedtime + " ms" +", "+"Memory Usage: " + ((afterUsedMem-beforeUsedMem)/(1024*1024))+ " MB");
    }

}