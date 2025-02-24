package gr.archimedesai.distributed.partitioner;

import org.apache.spark.Partitioner;

public class CustomPartitioner extends Partitioner {
    private final int numParts;
    public CustomPartitioner(int numParts){
        this.numParts=numParts;
    }

    @Override
    public int numPartitions() {
        return numParts;
    }

    @Override
    public int getPartition(Object key) {
        return ((int)key)%numParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomPartitioner that = (CustomPartitioner) o;

        return numParts == that.numParts;
    }
}
