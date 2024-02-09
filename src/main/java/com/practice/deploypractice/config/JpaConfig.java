package com.practice.deploypractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // @SpringBootApplication과 분리. JPA Auditing 기능 활성화.
public class JpaConfig {
}
