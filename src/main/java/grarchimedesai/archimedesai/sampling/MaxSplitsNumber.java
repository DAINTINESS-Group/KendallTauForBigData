package grarchimedesai.archimedesai.sampling;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MaxSplitsNumber {
    public static void main(String[] args) {

        final String filePath = args[0];
        final int colIndex = Integer.parseInt(args[1]);
        final String delimiter = args[2];

        List<Double> list = new ArrayList<>();
        long lineCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] vals = line.split(delimiter);
                    list.add(Double.parseDouble(vals[colIndex]));
                    lineCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        Map.Entry<Double, Long> maxEntry = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // Count occurrences
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Get the entry with the max value
                .orElse(null); // Handle empty list case

        System.out.println("Number with max frequency: " + maxEntry.getKey() +" Maximum frequency: " + maxEntry.getValue()+ " Max number of cells : "+ (int) lineCount/maxEntry.getValue());

    }
}
