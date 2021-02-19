package com.udacity.pricing.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Price {

    @Id
    private Long vehicleId;

    private String currency;
    private BigDecimal price;
}
