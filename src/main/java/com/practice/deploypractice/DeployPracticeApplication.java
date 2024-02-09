package com.practice.deploypractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing  // JPA Auditing 기능 활성화. -> 삭제
@SpringBootApplication
public class DeployPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeployPracticeApplication.class, args);
    }

}
