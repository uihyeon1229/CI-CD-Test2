//package com.project.sangil_be.securtiy;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    private final long MAX_AGE_SECS = 3600;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("https://kopite.shop")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .exposedHeaders(HttpHeaders.AUTHORIZATION)
////                .allowCredentials(true)
//                .allowCredentials(false)
//                .maxAge(this.MAX_AGE_SECS);
//    }
//}