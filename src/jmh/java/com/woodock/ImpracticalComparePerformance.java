package com.woodock;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Slf4j
@State(value = Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms1G", "-Xmx4G"})
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class ImpracticalComparePerformance {

    private static final int MAX_VALUE = 1_000;

    private static void slowDown() {
        try {
            TimeUnit.MICROSECONDS.sleep(5L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public  long iterativeSum() {
        long result = 0;
        for (int i = 1; i <= MAX_VALUE; i++) {
            result += i;
            slowDown();
        }
        return result;
    }

    @Benchmark
    public long sequentialSum() {
        return Stream.iterate(1L, i -> i + 1)
            .limit(MAX_VALUE)
            .peek(i -> slowDown())
            .reduce(Long::sum)
            .get();
    }

    @Benchmark
    public long parallelSum() {
        return Stream.iterate(1L, i -> i + 1)
            .limit(MAX_VALUE)
            .parallel()
            .peek(i -> slowDown())
            .reduce(Long::sum)
            .get();
    }

    @Benchmark
    public long parallelRangedSum() {
        return LongStream.rangeClosed(1, MAX_VALUE)
            .parallel()
            .peek(i -> slowDown())
            .reduce(Long::sum)
            .getAsLong();
    }
}
