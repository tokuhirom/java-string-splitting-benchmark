package com.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.regex.Pattern;

public class StringSplittingBenchmark {
    private static final String text = "The quick brown fox jumps over the lazy dog.";

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({" ", "\\s", "[ \\t\\n\\x0B\\f\\r]", " |\\t|\\n|\\x0B|\\f|\\r"})
        private String raw;
        private Pattern pattern;

        @Setup
        public void setup() {
            pattern = Pattern.compile(raw);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Benchmark
    public static void patternSplit(BenchmarkState state) {
        state.pattern.split(text);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Benchmark
    public static void stringSplit(BenchmarkState state) {
        text.split(state.raw);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StringSplittingBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
