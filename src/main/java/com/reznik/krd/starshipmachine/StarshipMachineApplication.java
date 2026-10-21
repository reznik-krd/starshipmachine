package com.reznik.krd.starshipmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.reznik.krd.starshipmachine.entity",
        "org.springframework.statemachine.data.jpa"
})
public class StarshipMachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarshipMachineApplication.class, args);
    }

}
