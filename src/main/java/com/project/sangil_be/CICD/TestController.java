package com.project.sangil_be.CICD;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${logging-module.version}")
    private String version;

    @GetMapping("/")
    public String version() {
        return "Test";
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "healthy";
    }

}