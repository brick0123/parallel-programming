package com.woodock;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

@Slf4j
public class CorePerformance {

    public static void main(String[] args) {
        System.out.println("stream (16 elements) ");
        final StopWatch stopWatch1 = new StopWatch();
        stopWatch1.start();
        IntStream.rangeClosed(1, 16)
            .peek(i -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            })
            .forEach(i -> log.info("i = {}", i));
        stopWatch1.stop();
        log.info(">>> stream takes [{}]ms", stopWatch1.getTotalTimeMillis());

        System.out.println("==== parallel stream (16 elements) ====");
        final StopWatch stopWatch2 = new StopWatch();
        stopWatch2.start();
        IntStream.rangeClosed(1, 16)
            .parallel()
            .peek(i -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            })
            .forEach(i -> log.info("i = {}", i));
        stopWatch2.stop();
        log.info(">>> parallel stream takes [{}]ms", stopWatch2.getTotalTimeMillis());
    }

}
