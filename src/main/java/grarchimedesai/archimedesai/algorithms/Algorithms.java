package grarchimedesai.archimedesai.algorithms;

import grarchimedesai.archimedesai.Pair;
import grarchimedesai.archimedesai.centralized.hashmap.grid.Grid;
import scala.Tuple2;

import java.util.*;

public class Algorithms {

    public static void sortPairsByX(Pair[] pairs){
        Arrays.sort(pairs, new Comparator<Pair>() {
            /** {@inheritDoc} */
            @Override
            public int compare(Pair pair1, Pair pair2) {
                int compareFirst = Double.compare(pair1.getX(),pair2.getX());
                return compareFirst != 0 ? compareFirst : Double.compare(pair1.getY(),pair2.getY());
            }
        });
    }

    public static Tuple2<Pair[], long[]> apacheCommons(Pair[] points) {

        final int n = points.length;
//        final long numPairs = sum(n - 1);

//        Arrays.sort(points, new Comparator<Pair>() {
//            /** {@inheritDoc} */
//            @Override
//            public int compare(Pair pair1, Pair pair2) {
//                int compareFirst = Double.compare(pair1.getX(),pair2.getX());
//                return compareFirst != 0 ? compareFirst : Double.compare(pair1.getY(),pair2.getY());
//            }
//        });

        long tiedXPairs = 0;
        long tiedXYPairs = 0;
        long consecutiveXTies = 1;
        long consecutiveXYTies = 1;
        Pair prev = points[0];
        for (int i = 1; i < n; i++) {
            final Pair curr = points[i];
            if (Double.compare(curr.getX(),prev.getX() )==0) {
                consecutiveXTies++;
                if (Double.compare(curr.getY(),prev.getY())==0) {
                    consecutiveXYTies++;
                } else {
                    tiedXYPairs += sum(consecutiveXYTies - 1);
                    consecutiveXYTies = 1;
                }
            } else {
                tiedXPairs += sum(consecutiveXTies - 1);
                consecutiveXTies = 1;
                tiedXYPairs += sum(consecutiveXYTies - 1);
                consecutiveXYTies = 1;
            }
            prev = curr;
        }
        tiedXPairs += sum(consecutiveXTies - 1);
        tiedXYPairs += sum(consecutiveXYTies - 1);

        long swaps = 0;
        @SuppressWarnings("unchecked")
        Pair[] pairsDestination = new Pair[n];
        for (int segmentSize = 1; segmentSize < n; segmentSize <<= 1) {
            for (int offset = 0; offset < n; offset += 2 * segmentSize) {
                int i = offset;
                final int iEnd = Math.min(i + segmentSize, n);
                int j = iEnd;
                final int jEnd = Math.min(j + segmentSize, n);

                int copyLocation = offset;
                while (i < iEnd || j < jEnd) {
                    if (i < iEnd) {
                        if (j < jEnd) {
                            if (Double.compare(points[i].getY(),points[j].getY()) <= 0) {
                                pairsDestination[copyLocation] = points[i];
                                i++;
                            } else {
                                pairsDestination[copyLocation] = points[j];
                                j++;
                                swaps += iEnd - i;
                            }
                        } else {
                            pairsDestination[copyLocation] = points[i];
                            i++;
                        }
                    } else {
                        pairsDestination[copyLocation] = points[j];
                        j++;
                    }
                    copyLocation++;
                }
            }
            final Pair[] pairsTemp = points;
            points = pairsDestination;
            pairsDestination = pairsTemp;
        }

        long tiedYPairs = 0;
        long consecutiveYTies = 1;
        prev = points[0];
        for (int i = 1; i < n; i++) {
            final Pair curr = points[i];
            if (Double.compare(curr.getY(), prev.getY())==0) {
                consecutiveYTies++;
            } else {
                tiedYPairs += sum(consecutiveYTies - 1);
                consecutiveYTies = 1;
            }
            prev = curr;
        }
        tiedYPairs += sum(consecutiveYTies - 1);

//        System.out.println(tiedXYPairs);
//        System.out.println("c"+ (numPairs- (tiedXPairs-tiedXYPairs) - (tiedYPairs-tiedXYPairs)-swaps - tiedXYPairs));
//        System.out.println("d"+ swaps);
//        System.out.println("Txy"+ ((numPairs-(numPairs- (tiedXPairs-tiedXYPairs) - (tiedYPairs-tiedXYPairs)-swaps - tiedXYPairs) - swaps - (tiedXPairs-tiedXYPairs) - (tiedYPairs-tiedXYPairs) ) ));

        return Tuple2.apply(points,new long[]{swaps, tiedXPairs-tiedXYPairs,tiedYPairs-tiedXYPairs, tiedXYPairs});

//        return new long[]{numPairs - tiedXPairs - tiedYPairs + tiedXYPairs -swaps,swaps,tiedXPairs-tiedXYPairs,tiedYPairs-tiedXYPairs};
    }

    private static long sum(long n) {
        return n * (n + 1) / 2L;
    }

    public static long[] southTile(Pair[] tilePairs, Pair[] southTilePairs) {

        int cursor1 = 0, cursor2 = 0;
        long counterTieOnOtherAttribute = 0;
//        long counterPairsWithOtherAttributeSmaller = 0;
        long counterPairsWithOtherAttributeLarger = 0;
        int numberTuplesTile1 = tilePairs.length;
        int numberTuplesTile2 = southTilePairs.length;

        // Merge phase
        while (cursor1 < numberTuplesTile1 && cursor2 < numberTuplesTile2) {
            double key1 = tilePairs[cursor1].getX();
            double key2 = southTilePairs[cursor2].getX();

            if (key1 == key2) {
                // Find all matching rows in table1
                int startI = cursor1;
                while (cursor1 < tilePairs.length && Double.compare(tilePairs[cursor1].getX(), key1)==0) {
                    cursor1++;
                }

                // Find all matching rows in table2
                int startJ = cursor2;
                while (cursor2 < southTilePairs.length && Double.compare(southTilePairs[cursor2].getX(), key2)==0) {
                    cursor2++;
                }

                // Join each matching row from table1 with all matching rows from table2
//                for (int m = startI; m < cursor1; m++) {
//                    counterPairsWithOtherAttributeLarger += (numberTuplesTile2 - cursor2);
//                    for (int n = startJ; n < cursor2; n++) {
//                        counterTieOnOtherAttribute++;
//                    }
//                }

                counterPairsWithOtherAttributeLarger = counterPairsWithOtherAttributeLarger + ((long) (numberTuplesTile2 - cursor2)*(cursor1 - startI));
                counterTieOnOtherAttribute = counterTieOnOtherAttribute + ((long) (cursor1 - startI)*(cursor2 -startJ));

                // Reset j to continue checking for more matches in table1
                cursor2 = startJ;
            } else if (Double.compare(key1, key2)==-1) {
                cursor1++;
                counterPairsWithOtherAttributeLarger += (numberTuplesTile2 - cursor2);
            } else {
                cursor2++;
//                counterPairsWithOtherAttributeSmaller += (numberTuplesTile1 - cursor1);
            }
        }
        return new long []{counterPairsWithOtherAttributeLarger, counterTieOnOtherAttribute};
//        return new long []{counterPairsWithOtherAttributeSmaller, counterPairsWithOtherAttributeLarger, counterTieOnOtherAttribute};
    }
//
//    public static long[] southTileMultiWay(Pair[][] pairArray) {
//        for (int i = 0; i < pairArray.length-1; i++) {
//            Pair[] pairs = pairArray[i];
//            int[] cursors = new int [pairs.length-1-i];
//
//            for (int j = 0; j < pairArray[i].length; j++) {
//
//            }
//
//        }
//
//
//        int cursor1 = 0, cursor2 = 0;
//        long counterTieOnOtherAttribute = 0;
//        long counterPairsWithOtherAttributeSmaller = 0;
//        long counterPairsWithOtherAttributeLarger = 0;
//        int numberTuplesTile1 = tilePairs.length;
//        int numberTuplesTile2 = southTilePairs.length;
//
//        // Merge phase
//        while (cursor1 < numberTuplesTile1 && cursor2 < numberTuplesTile2) {
//            double key1 = tilePairs[cursor1].getX();
//            double key2 = southTilePairs[cursor2].getX();
//
//            if (key1 == key2) {
//                // Find all matching rows in table1
//                int startI = cursor1;
//                while (cursor1 < tilePairs.length && tilePairs[cursor1].getX() == key1) {
//                    cursor1++;
//                }
//
//                // Find all matching rows in table2
//                int startJ = cursor2;
//                while (cursor2 < southTilePairs.length && southTilePairs[cursor2].getX() == key2) {
//                    cursor2++;
//                }
//
//                // Join each matching row from table1 with all matching rows from table2
//                for (int m = startI; m < cursor1; m++) {
////                	for(int runner = cursor2; runner < numberTuplesTile2; runner++) {
////                		larger.add(new Pair<DoublePair, DoublePair>(tilePairs.get(m), southTilePairs.get(runner)));
////                	}
//                    counterPairsWithOtherAttributeLarger += (numberTuplesTile2 - cursor2);
//                    for (int n = startJ; n < cursor2; n++) {
////                        tied.add(new Pair<DoublePair, DoublePair>(tilePairs.get(m), southTilePairs.get(n)));
//                        counterTieOnOtherAttribute++;
//                    }
//                }
//
//                // Reset j to continue checking for more matches in table1
//                cursor2 = startJ;
//            } else if (key1 < key2) {
////            	for(int runner = cursor2; runner < numberTuplesTile2; runner++) {
////            		larger.add(new Pair<DoublePair, DoublePair>(tilePairs.get(cursor1), southTilePairs.get(runner)));
////            	}
//                cursor1++;
//                counterPairsWithOtherAttributeLarger += (numberTuplesTile2 - cursor2);
//            } else {
////            	for(int runner = cursor1; runner < numberTuplesTile1; runner++) {
////            		smaller.add(new Pair<DoublePair, DoublePair>(tilePairs.get(runner), southTilePairs.get(cursor2)));
////            	}
//                cursor2++;
//                counterPairsWithOtherAttributeSmaller += (numberTuplesTile1 - cursor1);
//            }
//        }
//        return new long []{counterPairsWithOtherAttributeSmaller, counterPairsWithOtherAttributeLarger, counterTieOnOtherAttribute};
////        correlationStats.incrementConcordantCount(counterPairsWithOtherAttributeSmaller);
////        correlationStats.incrementDiscordantCount(counterPairsWithOtherAttributeLarger);
////        correlationStats.incrementTiedXCount(counterTieOnOtherAttribute);
//    }



    public static long[] eastTile(Pair[] tilePairs, Pair[] eastTilePairs) {

        int cursor1 = 0, cursor2 = 0;
        long counterTieOnOtherAttribute = 0;
//        long counterPairsWithOtherAttributeConcordant = 0;
        long counterPairsWithOtherAttributeDiscordant = 0;
        int numberTuplesTile1 = tilePairs.length;
        int numberTuplesTile2 = eastTilePairs.length;

        // Merge phase
        while (cursor1 < numberTuplesTile1 && cursor2 < numberTuplesTile2) {
            double key1 = tilePairs[cursor1].getY();
            double key2 = eastTilePairs[cursor2].getY();

            if (key1 == key2) {
                // Find all matching rows in table1
                int startI = cursor1;
                while (cursor1 < numberTuplesTile1 && Double.compare(tilePairs[cursor1].getY(), key1)==0) {
                    cursor1++;
                }

                // Find all matching rows in table2
                int startJ = cursor2;
                while (cursor2 < numberTuplesTile2 && Double.compare(eastTilePairs[cursor2].getY(), key2)==0) {
                    cursor2++;
                }

                // Join each matching row from table1 with all matching rows from table2
//                for (int m = startI; m < cursor1; m++) {
//                    counterPairsWithOtherAttributeConcordant += (numberTuplesTile2 - cursor2);
//                    for (int n = startJ; n < cursor2; n++) {
//                        counterTieOnOtherAttribute++;
//                    }
//                }

                counterTieOnOtherAttribute = counterTieOnOtherAttribute + (long) (cursor1 - startI)*(cursor2 -startJ);

                // Reset j to continue checking for more matches in table1
                cursor2 = startJ;
            } else if (Double.compare(key1, key2)==-1) {
                cursor1++;
//                counterPairsWithOtherAttributeConcordant += (numberTuplesTile2 - cursor2);
            } else {
                cursor2++;
                counterPairsWithOtherAttributeDiscordant += (numberTuplesTile1 - cursor1);
                //WHICH ONE? (cursor1); //
            }
        }
//        return new long []{counterPairsWithOtherAttributeConcordant, counterPairsWithOtherAttributeDiscordant, counterTieOnOtherAttribute};
        return new long []{counterPairsWithOtherAttributeDiscordant, counterTieOnOtherAttribute};
    }


    public static Tuple2<Pair[], long[]> apacheCommonsWithoutSelfHorizontal(Pair[] points, Grid grid) {

        final int n = points.length;
        final long numPairs = sum(n - 1);

//        Arrays.sort(points, new Comparator<Pair>() {
//            /** {@inheritDoc} */
//            @Override
//            public int compare(Pair pair1, Pair pair2) {
//                int compareFirst = Double.compare(pair1.getX(),pair2.getX());
//                return compareFirst != 0 ? compareFirst : Double.compare(pair1.getY(),pair2.getY());
//            }
//        });

        long tiedXPairs = 0;
        long tiedXYPairs = 0;
        long consecutiveXTies = 1;
        long consecutiveXYTies = 1;
        Pair prev = points[0];
        for (int i = 1; i < n; i++) {
            final Pair curr = points[i];
            if (Double.compare(curr.getX(),prev.getX() )==0) {
                consecutiveXTies++;
                if (Double.compare(curr.getY(),prev.getY())==0) {
                    consecutiveXYTies++;
                } else {
                    tiedXYPairs += sum(consecutiveXYTies - 1);
                    consecutiveXYTies = 1;
                }
            } else {
                tiedXPairs += sum(consecutiveXTies - 1);
                consecutiveXTies = 1;
                tiedXYPairs += sum(consecutiveXYTies - 1);
                consecutiveXYTies = 1;
            }
            prev = curr;
        }
        tiedXPairs += sum(consecutiveXTies - 1);
        tiedXYPairs += sum(consecutiveXYTies - 1);

        long swaps = 0;
        @SuppressWarnings("unchecked")
        Pair[] pairsDestination = new Pair[n];
        for (int segmentSize = 1; segmentSize < n; segmentSize <<= 1) {
            for (int offset = 0; offset < n; offset += 2 * segmentSize) {
                int i = offset;
                final int iEnd = Math.min(i + segmentSize, n);
                int j = iEnd;
                final int jEnd = Math.min(j + segmentSize, n);

                int copyLocation = offset;
                while (i < iEnd || j < jEnd) {
                    if (i < iEnd) {
                        if (j < jEnd) {
                            if (Double.compare(points[i].getY(),points[j].getY()) <= 0) {
                                pairsDestination[copyLocation] = points[i];
                                i++;
                            } else {
                                pairsDestination[copyLocation] = points[j];
                                j++;
                                swaps += iEnd - i;
                            }
                        } else {
                            pairsDestination[copyLocation] = points[i];
                            i++;
                        }
                    } else {
                        pairsDestination[copyLocation] = points[j];
                        j++;
                    }
                    copyLocation++;
                }
            }
            final Pair[] pairsTemp = points;
            points = pairsDestination;
            pairsDestination = pairsTemp;
        }

        long tiedYPairs = 0;
        long consecutiveYTies = 1;
        prev = points[0];
        for (int i = 1; i < n; i++) {
            final Pair curr = points[i];
            if (Double.compare(curr.getY(), prev.getY())==0) {
                consecutiveYTies++;
            } else {
                tiedYPairs += sum(consecutiveYTies - 1);
                consecutiveYTies = 1;
            }
            prev = curr;
        }
        tiedYPairs += sum(consecutiveYTies - 1);

//        System.out.println(tiedXYPairs);
//        System.out.println("c"+ (numPairs- (tiedXPairs-tiedXYPairs) - (tiedYPairs-tiedXYPairs)-swaps - tiedXYPairs));
//        System.out.println("d"+ swaps);
//        System.out.println("Txy"+ ((numPairs-(numPairs- (tiedXPairs-tiedXYPairs) - (tiedYPairs-tiedXYPairs)-swaps - tiedXYPairs) - swaps - (tiedXPairs-tiedXYPairs) - (tiedYPairs-tiedXYPairs) ) ));

        return Tuple2.apply(points,new long[]{swaps, tiedXPairs-tiedXYPairs,tiedYPairs-tiedXYPairs, tiedXYPairs});

//        return new long[]{numPairs - tiedXPairs - tiedYPairs + tiedXYPairs -swaps,swaps,tiedXPairs-tiedXYPairs,tiedYPairs-tiedXYPairs};
    }


    public static long[] southTile(double[] tilePairs, long[] tilePairsFreq, double[] southTilePairs, long[] southTilePairsFreq) {

        int cursor1 = 0, cursor2 = 0;
        long counterTieOnOtherAttribute = 0;
        long counterPairsWithOtherAttributeLarger = 0;
        int numberTuplesTile1 = tilePairs.length;
        int numberTuplesTile2 = southTilePairs.length;

        // Merge phase
        while (cursor1 < numberTuplesTile1 && cursor2 < numberTuplesTile2) {
            double key1 = tilePairs[cursor1];
            double key2 = southTilePairs[cursor2];

            if (key1 == key2) {

                int key1DistinctVals;
                int key2DistinctVals;
                int keyNextCursor2 = 0;


                if(cursor1==numberTuplesTile1-1){
                    key1DistinctVals = (int) tilePairsFreq[cursor1];
                }else{
                    key1DistinctVals = (int) (tilePairsFreq[cursor1] - tilePairsFreq[cursor1+1]);
                }

                if(cursor2==numberTuplesTile2-1){
                    key2DistinctVals = (int) southTilePairsFreq[cursor2];
                }else{
                    key2DistinctVals = (int) (southTilePairsFreq[cursor2] - southTilePairsFreq[cursor2+1]);
                    keyNextCursor2 =(int) southTilePairsFreq[cursor2+1];
                }
                cursor1++;

//                counterPairsWithOtherAttributeLarger = counterPairsWithOtherAttributeLarger + ((long) (numberTuplesTile2 - cursor2)*(cursor1 - startI));
//                counterTieOnOtherAttribute = counterTieOnOtherAttribute + ((long) (cursor1 - startI)*(cursor2 -startJ));

                counterPairsWithOtherAttributeLarger = counterPairsWithOtherAttributeLarger + ((long) keyNextCursor2*key1DistinctVals);
                counterTieOnOtherAttribute = counterTieOnOtherAttribute + ((long) key1DistinctVals*key2DistinctVals);

            } else if (Double.compare(key1, key2)==-1) {
//                cursor1++;
//                counterPairsWithOtherAttributeLarger += (numberTuplesTile2 - cursor2);
                int key1DistinctVals;
                if(cursor1==numberTuplesTile1-1){
                    key1DistinctVals = (int) tilePairsFreq[cursor1];
                }else{
                    key1DistinctVals = (int) (tilePairsFreq[cursor1] - tilePairsFreq[cursor1+1]);
                }

                counterPairsWithOtherAttributeLarger += (long)(southTilePairsFreq[cursor2]*key1DistinctVals);
                cursor1++;
            } else {
                cursor2++;
            }
        }
        return new long []{counterPairsWithOtherAttributeLarger, counterTieOnOtherAttribute};
    }

    public static long[] eastTile(double[] tilePairs,  long[] tilePairsFreq, double[] eastTilePairs, long[] eastTilePairsFreq) {

        int cursor1 = 0, cursor2 = 0;
        long counterTieOnOtherAttribute = 0;
        long counterPairsWithOtherAttributeDiscordant = 0;
        int numberTuplesTile1 = tilePairs.length;
        int numberTuplesTile2 = eastTilePairs.length;

        // Merge phase
        while (cursor1 < numberTuplesTile1 && cursor2 < numberTuplesTile2) {
            double key1 = tilePairs[cursor1];
            double key2 = eastTilePairs[cursor2];

            if (key1 == key2) {
                int key1DistinctVals;
                int key2DistinctVals;

                if(cursor1==numberTuplesTile1-1){
                    key1DistinctVals = (int) tilePairsFreq[cursor1];
                }else{
                    key1DistinctVals = (int) (tilePairsFreq[cursor1] - tilePairsFreq[cursor1+1]);
                }

                if(cursor2==numberTuplesTile2-1){
                    key2DistinctVals = (int) eastTilePairsFreq[cursor2];
                }else{
                    key2DistinctVals = (int) (eastTilePairsFreq[cursor2] - eastTilePairsFreq[cursor2+1]);
                }
                cursor1++;

                counterTieOnOtherAttribute = counterTieOnOtherAttribute + ((long) key1DistinctVals*key2DistinctVals);

//                // Find all matching rows in table1
//                int startI = cursor1;
//                while (cursor1 < numberTuplesTile1 && Double.compare(tilePairs[cursor1][0], key1)==0) {
//                    cursor1++;
//                }
//
//                // Find all matching rows in table2
//                int startJ = cursor2;
//                while (cursor2 < numberTuplesTile2 && Double.compare(eastTilePairs[cursor2][0], key2)==0) {
//                    cursor2++;
//                }

//                counterTieOnOtherAttribute = counterTieOnOtherAttribute + (long) (cursor1 - startI)*(cursor2 -startJ);

//                // Reset j to continue checking for more matches in table1
//                cursor2 = startJ;
            } else if (Double.compare(key1, key2)==-1) {
                cursor1++;
            } else {

//                int key1DistinctVals;
//                if(cursor1==numberTuplesTile1-1){
//                    key1DistinctVals = (int) tilePairsFreq[cursor1];
//                }else{
//                    key1DistinctVals = (int) (tilePairsFreq[cursor1] - tilePairsFreq[cursor1+1]);
//                }
//                counterPairsWithOtherAttributeDiscordant += (long)(eastTilePairsFreq[cursor2]*key1DistinctVals);
//                cursor2++;
//                counterPairsWithOtherAttributeDiscordant += (numberTuplesTile1 - cursor1);

                int key2DistinctVals;
                if(cursor2==numberTuplesTile2-1){
                    key2DistinctVals = (int) eastTilePairsFreq[cursor2];
                }else{
                    key2DistinctVals = (int) (eastTilePairsFreq[cursor2] - eastTilePairsFreq[cursor2+1]);
                }
                counterPairsWithOtherAttributeDiscordant += (long)(tilePairsFreq[cursor1]*key2DistinctVals);
                cursor2++;
            }
        }
        return new long []{counterPairsWithOtherAttributeDiscordant, counterTieOnOtherAttribute};
    }

//    public long discordantCells(HashMap<Integer,Integer> histogram, int cellsInXAxis, int cellsInYAxis) {
//
//        int[] previous = new int[cellsInXAxis];
//        int[] current = new int[cellsInXAxis];
//        long discorants = 0;
//
//        previous[cellsInXAxis-1]=histogram.getOrDefault(cellsInXAxis-1,0/*lastcellrightbottom*/);
//        for (int i = cellsInXAxis-2; i >= 0; i--) {
//            previous[i] = histogram.getOrDefault(i,0/*i,0*/) + previous[i+1];
//        }
//
//        for (int j = 1; j < cellsInYAxis; j++) {
//            current[cellsInXAxis-1] = previous[cellsInXAxis-1] + histogram.getOrDefault(/*cellsInXAxis-1,j*/cellsInXAxis-1+(cellsInXAxis*j),0);
//            for (int i = cellsInXAxis-2; i >= 0; i--) {
//                current[i] = current[i+1] + previous[i] - previous[i+1] + histogram.getOrDefault(/*i,j*/i+(cellsInXAxis*j),0);
////                current[i] = current[i-1] + previous[j] - previous[j-1] + histogram.getOrDefault(/*i,j*/i+(cellsInXAxis*j),0);
//                discorants += histogram.getOrDefault(/*i,j*/i+(cellsInXAxis*j),0)*previous[j-1];
//            }
////            previous = Array.copyOf(current);
//            System.arraycopy(current, 0, previous, 0, cellsInXAxis);
//            Arrays.fill(current, 0);
//        }
//        return discorants;
//    }

    public static long discordantCells(HashMap<Integer,Integer> histogram, int cellsInXAxis, int cellsInYAxis) {

        int[] myarr = new int[cellsInXAxis];
        long discorants = 0;

        int ijCell;
        myarr[cellsInXAxis-1]=histogram.getOrDefault(cellsInXAxis-1,0);
        for (int i = cellsInXAxis-2; i >= 0; i--) {
            myarr[i] = histogram.getOrDefault(i,0/*i,0*/) + myarr[i+1];
        }

        for (int j = 1; j < cellsInYAxis; j++) {
            int diag = myarr[cellsInXAxis-1];
            myarr[cellsInXAxis-1] = myarr[cellsInXAxis-1] + histogram.getOrDefault(cellsInXAxis-1+(cellsInXAxis*j),0);
            for (int i = cellsInXAxis-2; i >= 0; i--) {
                int below = myarr[i];
                ijCell = histogram.getOrDefault(i+(cellsInXAxis*j),0);
                myarr[i] += myarr[i+1]-diag + ijCell;
                discorants = discorants + (long) ijCell *diag;
                diag = below;
            }
        }
        return discorants;
    }

    public static long approximate(HashMap<Integer,Integer> histogram, int cellsInXAxis, int cellsInYAxis) {

        long[] arr = new long[cellsInXAxis];
        long discorants = 0;
        arr[cellsInXAxis-1]=histogram.getOrDefault(cellsInXAxis-1,0).longValue();
        discorants = (arr[cellsInXAxis-1] * (arr[cellsInXAxis-1]- 1))/4;

        long lastCellInRow;
        long ijcell;
        long below;

        for (int i = cellsInXAxis-2; i >= 0; i--) {
            long icell = histogram.getOrDefault(i,0).longValue();
            arr[i] = icell + arr[i+1];
            discorants = discorants + ((icell *  (icell -1))/4);
            discorants = discorants + ((icell * arr[i+1])/2);
//            for (int k = i+1; k<cellsInXAxis ; k++) {
//                discorants = discorants + ((icell*histogram.getOrDefault(k,0).longValue())/2);
//            }
        }

        for (int j = 1; j < cellsInYAxis; j++) {
            long diagonal = arr[cellsInXAxis-1];
            lastCellInRow = histogram.getOrDefault((cellsInXAxis-1)+(cellsInXAxis * j),0);
            arr[cellsInXAxis-1] = arr[cellsInXAxis-1] + lastCellInRow;
            discorants = discorants + ((lastCellInRow * (lastCellInRow-1))/4);
            discorants = discorants + ((lastCellInRow*diagonal)/2);
//            for(int l = 0;l<j; l++) {
//                discorants = discorants + ((lastCellInRow * histogram.getOrDefault((cellsInXAxis-1)+(cellsInXAxis * l),0)))/2;
//            }

            for(int i = cellsInXAxis-2; i >= 0; i--) {
                ijcell = histogram.getOrDefault(i+ (cellsInXAxis*j),0).longValue();

//                for(int k = i+1; k<cellsInXAxis ; k++) {
//                    discorants = discorants + ((ijcell * histogram.getOrDefault(k+ (cellsInXAxis*j), 0).longValue())/2);
//                }
//
//                for(int l = j-1; l>=0 ; l--) {
//                    discorants = discorants + ((ijcell * histogram.getOrDefault(i+ (cellsInXAxis*l), 0).longValue())/2);
//                }

                below = arr[i];
                arr[i] = arr[i]+arr[i+1]-diagonal+ijcell;
                discorants = discorants + (ijcell * diagonal);
                discorants = discorants + ((ijcell*(ijcell-1))/4);
                discorants = discorants + ((ijcell*(arr[i+1]-diagonal))/2);
                discorants = discorants + (((ijcell*(below-diagonal))/2));
                diagonal = below;
            }
        }
        return discorants;
    }















    public static long discordantCells(int[][] histogram, int cellsInXAxis, int cellsInYAxis) {

        int[] myarr = new int[cellsInXAxis];
        long discorants = 0;

        myarr[cellsInXAxis-1]=histogram[cellsInXAxis-1][0];
        for (int i = cellsInXAxis-2; i >= 0; i--) {
            myarr[i] = histogram[i][0] + myarr[i+1];
        }

        for (int j = 1; j < cellsInYAxis; j++) {
            int diag = myarr[cellsInXAxis-1];
            myarr[cellsInXAxis-1] = myarr[cellsInXAxis-1] + histogram[cellsInXAxis-1][j];//.getOrDefault(cellsInXAxis-1+(cellsInXAxis*j),0);
            for (int i = cellsInXAxis-2; i >= 0; i--) {
                int below = myarr[i];
                myarr[i] += myarr[i+1]-diag + histogram[i][j];//.getOrDefault(i+(cellsInXAxis*j),0);
                discorants = discorants + (long) histogram[i][j]*diag;//.getOrDefault(i + (cellsInXAxis * j), 0) *diag;
                diag = below;
            }
        }
        return discorants;

    }

//    public static double approximate1(int[][] histogram, int cellsInXAxis, int cellsInYAxis) {
//
//        double[] arr = new double[cellsInXAxis];
//        double discorants = 0;
//        arr[cellsInXAxis-1]=histogram[cellsInXAxis-1][0];//.getOrDefault(cellsInXAxis-1,0).longValue();
//        discorants = (arr[cellsInXAxis-1] * (arr[cellsInXAxis-1]- 1))/4;
//
//        double lastCellInRow;
//        double ijcell;
//        double below;
//
//        for (int i = cellsInXAxis-2; i >= 0; i--) {
//            double icell = histogram[i][0];//.getOrDefault(i,0).longValue();
//            arr[i] = icell + arr[i+1];
//            discorants = discorants + ((icell *  (icell -1))/4);
//            for (int k = i+1; k<cellsInXAxis ; k++) {
//                discorants = discorants + ((icell*histogram[k][0])/2);
//            }
//        }
//
//        for (int j = 1; j < cellsInYAxis; j++) {
//            double diagonal = arr[cellsInXAxis-1];
//            lastCellInRow = histogram[cellsInXAxis-1][j];//.getOrDefault((cellsInXAxis-1)+(cellsInXAxis * j),0);
//            arr[cellsInXAxis-1] = arr[cellsInXAxis-1] + lastCellInRow;
//            discorants = discorants + ((lastCellInRow * (lastCellInRow-1))/4);
//
//            for(int l = 0;l<j; l++) {
//                discorants = discorants + ((lastCellInRow * histogram[cellsInXAxis-1][l]))/2;
//            }
//
//            for(int i = cellsInXAxis-2; i >= 0; i--) {
//                ijcell = histogram[i][j];
//                discorants = discorants + ((ijcell*(ijcell-1))/4);
//
//                for(int k = i+1; k<cellsInXAxis ; k++) {
//                    discorants = discorants + ((ijcell * histogram[k][j])/2);
//                }
//
//                for(int l = j-1; l>=0 ; l--) {
//                    discorants = discorants + ((ijcell * histogram[i][l])/2);
//                }
//
//                below = arr[i];
//                arr[i] = arr[i]+arr[i+1]-diagonal+ijcell;
//                discorants = discorants + (ijcell * diagonal);
//                diagonal = below;
//            }
//        }
//        return discorants;
//    }

    public static double approximate(int[][] histogram, int cellsInXAxis, int cellsInYAxis) {

        double[] arr = new double[cellsInXAxis];
        double discorants = 0;
        arr[cellsInXAxis-1]=histogram[cellsInXAxis-1][0];
        discorants = (arr[cellsInXAxis-1] * (arr[cellsInXAxis-1]- 1))/4;

        double lastCellInRow;
        double ijcell;
        double below;

        for (int i = cellsInXAxis-2; i >= 0; i--) {
            double icell = histogram[i][0];//.getOrDefault(i,0).longValue();
            arr[i] = icell + arr[i+1];
            discorants = discorants + ((icell *  (icell -1))/4);
            discorants = discorants + ((icell*arr[i+1])/2);
//            for (int k = i+1; k<cellsInXAxis ; k++) {
//                discorants = discorants + ((icell*histogram[k][0])/2);
//            }
        }

        for (int j = 1; j < cellsInYAxis; j++) {
            double diagonal = arr[cellsInXAxis-1];
            lastCellInRow = histogram[cellsInXAxis-1][j];//.getOrDefault((cellsInXAxis-1)+(cellsInXAxis * j),0);
            arr[cellsInXAxis-1] = arr[cellsInXAxis-1] + lastCellInRow;
            discorants = discorants + ((lastCellInRow * (lastCellInRow-1))/4);
            discorants = discorants + ((lastCellInRow*diagonal)/2);
//            for(int l = 0;l<j; l++) {
//                discorants = discorants + ((lastCellInRow * histogram[cellsInXAxis-1][l]))/2;
//            }

            for(int i = cellsInXAxis-2; i >= 0; i--) {
                ijcell = histogram[i][j];

//                for(int k = i+1; k<cellsInXAxis ; k++) {
//                    discorants = discorants + ((ijcell * histogram[k][j])/2);
//                }
//
//                for(int l = j-1; l>=0 ; l--) {
//                    discorants = discorants + ((ijcell * histogram[i][l])/2);
//                }

                below = arr[i];
                arr[i] = arr[i]+arr[i+1]-diagonal+ijcell;
                discorants = discorants + (ijcell * diagonal);
                discorants = discorants + ((ijcell*(ijcell-1))/4);
                discorants = discorants + ((ijcell*(arr[i+1]-diagonal))/2);
                discorants = discorants + (((ijcell*(below-diagonal))/2));
                diagonal = below;
            }
        }
        return discorants;
    }

}
