package com.project.sangil_be.CICD;


import org.springframework.web.bind.annotation.*;

@RestController
class TestController2 {

    @GetMapping("/")
    public String version() {
        return "Test2";
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "healthy";
    }
//테스트17
}