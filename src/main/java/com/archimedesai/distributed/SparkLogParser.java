package com.archimedesai.distributed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SparkLogParser {
    public static String getTimeFromStates(String filePath){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            List<Long> times = new ArrayList<>();
            long submissionTime = 0;
            long completedTime = 0;

            while((line = br.readLine())!=null){

                if(line.contains("\"SparkListenerStageCompleted\"")){
                    int index = line.indexOf("\"Submission Time\":");
                    String line1 = line.substring(index);
                    submissionTime = Long.parseLong(line1.substring(0,line1.indexOf(",")).substring(line1.indexOf(":")+1));

                    index = line.indexOf("\"Completion Time\":");
                    line1 = line.substring(index);
                    completedTime = Long.parseLong(line1.substring(0,line1.indexOf(",")).substring(line1.indexOf(":")+1));

                    times.add((completedTime-submissionTime)/1000);
                }
            }
            String result = times.stream()
                    .map(String::valueOf) // Convert each Long to String
                    .collect(Collectors.joining(","));

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String filePath, String property){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            long number = 0;
            while((line = br.readLine())!=null){
                if(line.contains(property)){
                    int index = line.indexOf(property);
                    line = line.substring(index);
                    number = number + Long.parseLong(line.substring(0,line.indexOf(",")).substring(line.indexOf(":")+1));
                }
            }
            return String.valueOf(number/(1024*1024));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
