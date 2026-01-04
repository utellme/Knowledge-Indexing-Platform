package com.myproject.knowledge.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HealthProbeController {

    private final long start = System.currentTimeMillis();

    @GetMapping("/api/v1/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/api/v1/metrics")
    public Map<String, Object> metrics() {
        return Map.of(
            "uptimeSeconds", (System.currentTimeMillis() - start) / 1000
        );
    }
}