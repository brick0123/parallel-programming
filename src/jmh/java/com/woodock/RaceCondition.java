package com.woodock;

import java.util.stream.IntStream;

public class RaceCondition {

    public static void main(String[] args) {

        final int[] sum = {0};
        IntStream.range(0, 101)
            .forEach(i -> sum[0] += i);

        System.out.println("stream sum = " + sum[0]);

        final int[] sum2 = {0};
        IntStream.range(0, 101)
            .parallel()
            .forEach(i -> sum2[0] += i);

        System.out.println("parallel stream race condition = " + sum2[0]);

        System.out.println("parallel stream no race condition = " + IntStream.range(0, 101)
            .parallel()
            .sum());
    }
}
