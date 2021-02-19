package com.udacity.pricing;

import com.udacity.pricing.entity.Price;
import com.udacity.pricing.repository.PriceRepository;
import com.udacity.pricing.service.PricingService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * Creates a Spring Boot Application to run the Pricing Service.
 * TODO: Convert the application from a REST API to a microservice.
 */
@SpringBootApplication
@EnableEurekaClient
public class PricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }

    /**
     * Creates {ID: Price} initial pairings in the repository (current implementation allows for 20 vehicles)
     */
    @Bean
    public CommandLineRunner initializeRepository(PricingService pricingService, PriceRepository priceRepository) {
        List<Price> initialPrices = LongStream
                .range(1, 20)
                .mapToObj(i -> new Price(i, "USD", pricingService.getRandomPrice()))
                .collect(Collectors.toList());

        return args -> {
            priceRepository.saveAll(initialPrices);
        };
    }

}
