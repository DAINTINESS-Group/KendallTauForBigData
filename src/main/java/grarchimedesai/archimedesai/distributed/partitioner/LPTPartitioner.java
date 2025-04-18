package grarchimedesai.archimedesai.distributed.partitioner;

import org.apache.spark.Partitioner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LPTPartitioner extends Partitioner {

    private final int numParts;
    private final Map<Integer, Integer> cellsPartitonsMap; //map cells to spark partitions
    public LPTPartitioner(Map<Integer,Integer> hashMap, int numParts){
        cellsPartitonsMap = new HashMap<>();
        ltp(hashMap, numParts);
        this.numParts=numParts;
    }

    private void ltp(Map<Integer,Integer> hashMap, int numParts){
        int[] partitionsCosts = new int[numParts];

        List<Map.Entry<Integer,Integer>> sortedEntries = hashMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())).collect(Collectors.toList());

        for (int i = 0; i < sortedEntries.size(); i++) {
            int index = getIndexOfMin(partitionsCosts);
            partitionsCosts[index] = partitionsCosts[index] + sortedEntries.get(i).getValue();
            cellsPartitonsMap.put(sortedEntries.get(i).getKey(),index);
        }
    }

    private int getIndexOfMin(int[] partitionsCosts){
        int index = -1;
        int value = Integer.MAX_VALUE;
        for (int i = 0; i < partitionsCosts.length; i++) {
            if(partitionsCosts[i] < value ){
                value = partitionsCosts[i];
                index = i;
            }
        }
        return index;
    }

    @Override
    public int numPartitions() {
        return numParts;
    }

    @Override
    public int getPartition(Object key) {
        if(cellsPartitonsMap.containsKey((int)key)){
            return cellsPartitonsMap.get((int)key);
        }
        return ((int)key)%numParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LPTPartitioner that = (LPTPartitioner) o;

        return numParts == that.numParts;
    }
}

