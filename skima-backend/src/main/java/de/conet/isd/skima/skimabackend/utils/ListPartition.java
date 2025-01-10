package de.conet.isd.skima.skimabackend.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Util to partition a List of Objects into multiple sublists of fixed size.
 * The last sublist will contain the "rest" of the list's items, if the batchSize is not a
 * straight divisior of list size.
 */
public class ListPartition<T> extends ArrayList<List<T>> {

    public ListPartition(List<T> originList, int batchSize) {
        int originSize = originList != null ? originList.size() : 0;
        // int batchCount = originSize / batchSize + ((originSize % batchSize == 0) ? 0 : 1);

        for (int offset = 0; offset < originSize; offset += batchSize) {
            int upperLimit = offset + batchSize; // upperLimit exclusive
            if (upperLimit > originSize) {
                upperLimit = originSize;
            }

            add(originList.subList(offset, upperLimit));
        }
    }

    public static <IN, OUT> List<OUT> queryPartitioned(List<IN> list, int batchSize,
                                                       Function<List<IN>, List<OUT>> chunkQuery) {
        List<OUT> result = new ArrayList<>();
        if (list == null || list.isEmpty()) return result;

        ListPartition<IN> partitioned = new ListPartition<>(list, batchSize);
        for (List<IN> idsBulk : partitioned) {
            result.addAll(chunkQuery.apply(idsBulk));
        }
        return result;
    }
}
