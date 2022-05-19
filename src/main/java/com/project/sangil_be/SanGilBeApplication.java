package com.project.sangil_be;

import com.project.sangil_be.repository.CompletedRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableJpaAuditing
public class SanGilBeApplication {

    public static void main(String[] args) {

        SpringApplication.run(SanGilBeApplication.class, args);
    }
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

}
