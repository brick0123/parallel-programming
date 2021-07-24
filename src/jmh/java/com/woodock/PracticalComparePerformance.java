package com.woodock;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms1G", "-Xmx4G"})
@Warmup(iterations = 2)
@Measurement(iterations = 3)
public class PracticalComparePerformance {

    private static final List<Product> products;

    static {
        final int length = 50_000;
        final Product[] list = new Product[length];

        for (int i = 0; i < length; i++) {
            list[i] = new Product((long) i, "Product" + i, BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(20, 40)));
        }

        products = List.of(list);
    }

    private BigDecimal imperativeSum(final List<Product> products, final Predicate<Product> predicate) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Product product : products) {
            if (predicate.test(product)) {
                sum = sum.add(product.getPrice());
            }
        }
        return sum;
    }

    private BigDecimal streamSum(final List<Product> products, final Predicate<Product> predicate) {
        return products.stream()
            .filter(predicate)
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal parallelSum(final List<Product> products, final Predicate<Product> predicate) {
        return products.stream()
            .parallel()
            .filter(predicate)
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Benchmark
    public BigDecimal imperativeTest() {
        return imperativeSum(products, product -> product.getPrice().compareTo(BigDecimal.valueOf(30)) >= 0);
    }

    @Benchmark
    public BigDecimal streamTest() {
        return streamSum(products, product -> product.getPrice().compareTo(BigDecimal.valueOf(30)) >= 0);
    }

    @Benchmark
    public BigDecimal parallelStreamTest() {
        return parallelSum(products, product -> product.getPrice().compareTo(BigDecimal.valueOf(30)) >= 0);
    }


    static class Product {
        private Long id;
        private String name;
        private BigDecimal price;

        public BigDecimal getPrice() {
            return price;
        }

        public Product(final Long id, final String name, final BigDecimal price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }
}
