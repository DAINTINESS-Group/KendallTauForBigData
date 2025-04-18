package grarchimedesai.archimedesai.sampling;

import java.io.*;
import java.util.Random;

public class ExtractSample {
    public static void main(String[] args) {

        final String filePath = args[0];
        final String extractedFilePath = args[1];
        double sampleSize = Double.parseDouble(args[2]);

        try {
            Random rand = new Random();
            File file = new File(extractedFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (Double.compare(rand.nextDouble(), sampleSize) != 1) {
                        bw.write(line+"\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
