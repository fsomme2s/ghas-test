package de.conet.isd.skima.skimabackend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;


/**
 * Tests {@link ListPartition}
 */
class ListPartitionTest {
    /**
     * Asserts Util {@link ListPartition} produces right batch sizes, even if the target list size is not
     * dividable through batchsize without rest.
     */
    @Test
    void testPartitionSizesWithRest() {
        // 100 elements list: (0..99: .range() end is exclusive)
        List<Integer> testInput = IntStream.range(0, 100).boxed().toList();

        // util under test: Partition into batches of 30
        ListPartition<Integer> partition = new ListPartition<>(testInput, 30);

        // 100 partitioned into 30er batches: results in 3x30 and 1x10 batches
        Assertions.assertEquals(4, partition.size());
        Assertions.assertEquals(30, partition.getFirst().size());
        Assertions.assertEquals(30, partition.get(1).size());
        Assertions.assertEquals(30, partition.get(2).size());
        Assertions.assertEquals(10, partition.get(3).size());
    }

    /**
     * Asserts Util {@link ListPartition} keeps the input order.
     */
    @Test
    void testPartitionKeepsOrder() {
        // 100 elements list: (0..99: .range() end is exclusive)
        List<Integer> testInput = IntStream.range(0, 100).boxed().toList();

        // util under test: Partition into batches of 30
        ListPartition<Integer> partition = new ListPartition<>(testInput, 30);

        // 100 partitioned into 30er batches: results in 3x30 and 1x10 batches
        int i = 0;
        for (List<Integer> batch : partition) {
            for (Integer element : batch) {
                Assertions.assertEquals(testInput.get(i), element);
                i++;
            }
        }

    }

    /**
     * Asserts Util {@link ListPartition} does not throw exeptions on empty input.
     */
    @Test
    void testPartitionOnEmptyInput() {
        ListPartition<Integer> emptyPartition = new ListPartition<>(List.of(), 30);
        Assertions.assertEquals(0, emptyPartition.size());

        ListPartition<Integer> nullPartition = new ListPartition<>(null, 30);
        Assertions.assertEquals(0, nullPartition.size());
    }

}
