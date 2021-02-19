package com.udacity.vehicles;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VehiclesApiIntegrationTest {
    @LocalServerPort
    private int port;

    private Long currentCarId;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarService carService;

    @Before
    public void beforeEachTest() {
        Car car = carService.save(getCar());
        currentCarId = car.getId();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void getAllCars() {
        ResponseEntity<Object> response =
                restTemplate.getForEntity("http://localhost:" + port + "/cars", Object.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getCarById() {
        ResponseEntity<Car> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/cars/" + currentCarId, Car.class);

        Car originalCar = getCar();
        Car responseCar = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // check some attributes
        assertEquals(originalCar.getCondition(), responseCar.getCondition());
        assertEquals(originalCar.getDetails().getModelYear(), responseCar.getDetails().getModelYear());
        assertEquals(originalCar.getDetails().getNumberOfDoors(), responseCar.getDetails().getNumberOfDoors());

        // check if price service call worked
        assertTrue(responseCar.getPrice().contains("USD"));

        // check if location service call worked
        assertFalse(responseCar.getLocation().getAddress().isEmpty());
        assertFalse(null == responseCar.getLocation().getAddress());
    }

    @Test
    public void getCarByIdThatDoesNotExist404() {
        ResponseEntity<Car> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/cars/999999", Car.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void createCar() {
        ResponseEntity<Car> response =
                this.restTemplate.postForEntity("http://localhost:" + port + "/cars", getCar(), Car.class);

        Car originalCar = getCar();
        Car responseCar = response.getBody();
        assertEquals(originalCar.getCondition(), responseCar.getCondition());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void updateCar() {
        Car originalCar = getCar();
        Car updatedCar = originalCar;
        updatedCar.setCondition(Condition.NEW);
        updatedCar.getDetails().setNumberOfDoors(8);

        HttpEntity<Car> entity = new HttpEntity<Car>(updatedCar);
        ResponseEntity<Car> response = restTemplate.exchange("http://localhost:" + port + "/cars/{id}", HttpMethod.PUT,
                                                                entity, Car.class, currentCarId);

        Car responseCar = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCar.getCondition(), responseCar.getCondition());
        assertEquals(updatedCar.getDetails().getNumberOfDoors(), responseCar.getDetails().getNumberOfDoors());
    }

    @Test
    public void updateCarThatDoesNotExist404() {
        HttpEntity<Car> entity = new HttpEntity<Car>(getCar());
        ResponseEntity<Car> response = restTemplate.exchange("http://localhost:" + port + "/cars/{id}", HttpMethod.PUT,
                                                             entity, Car.class, 9999999L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}
