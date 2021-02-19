package com.udacity.pricing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

/**
 * Implements the pricing service to generate prices for vehicles.
 */
@Component
public class PricingService {
    /**
     * Gets a random price to fill in for a given vehicle ID.
     * @return random price for a vehicle
     */
    public BigDecimal getRandomPrice() {
        return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
                .multiply(new BigDecimal(5000d)).setScale(2, RoundingMode.HALF_UP);
    }
}
