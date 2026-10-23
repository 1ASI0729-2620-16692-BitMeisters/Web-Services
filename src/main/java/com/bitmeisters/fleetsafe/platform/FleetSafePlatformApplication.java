package com.bitmeisters.fleetsafe.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FleetSafePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetSafePlatformApplication.class, args);
    }

}
