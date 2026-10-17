// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.web;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Used by Docker HEALTHCHECK, Ansible health check and the Jenkins smoke test. */
@RestController
public class HealthController {
    @Value("${app.environment}")
    private String environment;

    @Value("${app.version}")
    private String version;

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "environment", environment, "version", version);
    }
}
