package com.myproject.knowledge.metrics;


import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Component
public class SearchMetrics {

    private static final String SEARCH_TIMER_NAME = "search.latency";

    private final MeterRegistry meterRegistry;

    public SearchMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * Start measuring search latency.
     *
     * @return Timer.Sample used to stop the timer
     */
    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * Stop the timer and record the duration.
     *
     * @param sample timer sample returned by startTimer()
     */
    public void stopTimer(Timer.Sample sample) {
        if (sample != null) {
            sample.stop(
                Timer.builder(SEARCH_TIMER_NAME)
                     .description("Latency of document search operations")
                     .publishPercentiles(0.50, 0.90, 0.95, 0.99)
                     .publishPercentileHistogram()
                     .register(meterRegistry)
            );
        }
    }

    /**
     * Optional helper for manual timing (used in benchmarks or tests).
     *
     * @param durationMillis elapsed time in milliseconds
     */
    public void recordManual(long durationMillis) {
        Timer.builder(SEARCH_TIMER_NAME)
             .description("Latency of document search operations")
             .register(meterRegistry)
             .record(durationMillis, TimeUnit.MILLISECONDS);
    }
}