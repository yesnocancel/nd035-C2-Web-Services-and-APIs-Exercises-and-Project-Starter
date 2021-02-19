package com.udacity.pricing;

import com.udacity.pricing.entity.Price;
import com.udacity.pricing.repository.PriceRepository;
import com.udacity.pricing.service.PricingService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PricingServiceUnitTests {
	@Autowired
	private PriceRepository priceRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void getPriceForVehicleFromInitializedDB() {
		Optional<Price> optionalPrice = priceRepository.findById(12L);
		assertTrue(optionalPrice.isPresent());

		Price price = optionalPrice.get();
		assertEquals("USD", price.getCurrency());
		assertNotNull(price.getPrice());
		assertTrue(price.getPrice().compareTo(new BigDecimal(5000.0)) > 0);
		assertTrue(price.getPrice().compareTo(new BigDecimal(25000.0)) < 0);
	}

	@Test
	public void updateAndGetPriceForVehicle() {
		Long vehicleId = 42L;
		String currency = "EUR";
		BigDecimal priceValue = new BigDecimal(1234.56).setScale(2, RoundingMode.HALF_UP);

		Price priceToSave = new Price(vehicleId, currency, priceValue);
		priceRepository.save(priceToSave);
		Optional<Price> optionalRetrievedPrice = priceRepository.findById(vehicleId);
		assertTrue(optionalRetrievedPrice.isPresent());
		Price retrievedPrice = optionalRetrievedPrice.get();

		assertNotNull(retrievedPrice.getVehicleId());
		assertEquals(currency, retrievedPrice.getCurrency());
		assertEquals(vehicleId, retrievedPrice.getVehicleId());
		assertTrue(retrievedPrice.getPrice().compareTo(priceValue) == 0);
	}
}
