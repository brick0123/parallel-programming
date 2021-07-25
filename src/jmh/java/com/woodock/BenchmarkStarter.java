package com.woodock;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkStarter {

    public static void main(String[] args) throws RunnerException {
         Options opt = new OptionsBuilder().include(PracticalComparePerformance.class.getSimpleName())
            .build();

        new Runner(opt).run();
    }

}
