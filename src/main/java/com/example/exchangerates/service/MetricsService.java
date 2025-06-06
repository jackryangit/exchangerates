package com.example.exchangerates.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MetricsService {

    private final AtomicLong totalQueries = new AtomicLong(0);
    private final Map<String, ApiMetrics> apiMetrics = new ConcurrentHashMap<>();

    public void incrementTotalQueries() {
        totalQueries.incrementAndGet();
    }

    public void incrementApiRequests(String apiName) {
        apiMetrics.computeIfAbsent(apiName, k -> new ApiMetrics()).incrementRequests();
    }

    public void incrementApiResponses(String apiName) {
        apiMetrics.computeIfAbsent(apiName, k -> new ApiMetrics()).incrementResponses();
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new ConcurrentHashMap<>();
        metrics.put("totalQueries", totalQueries.get());
        metrics.put("apis", apiMetrics.entrySet().stream()
                .map(entry -> Map.of(
                        "name", entry.getKey(),
                        "metrics", Map.of(
                                "totalRequests", entry.getValue().getRequests(),
                                "totalResponses", entry.getValue().getResponses()
                        )
                ))
                .toList());
        return metrics;
    }

    private static class ApiMetrics {
        private final AtomicLong requests = new AtomicLong(0);
        private final AtomicLong responses = new AtomicLong(0);

        void incrementRequests() {
            requests.incrementAndGet();
        }

        void incrementResponses() {
            responses.incrementAndGet();
        }

        long getRequests() {
            return requests.get();
        }

        long getResponses() {
            return responses.get();
        }
    }
}