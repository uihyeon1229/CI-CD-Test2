package com.project.sangil_be.CICD;


import org.springframework.web.bind.annotation.*;

@RestController
class TestController2 {

    @GetMapping("/")
    public String version() {
        return "Test";
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "healthy2";
    }
//테스트16
}