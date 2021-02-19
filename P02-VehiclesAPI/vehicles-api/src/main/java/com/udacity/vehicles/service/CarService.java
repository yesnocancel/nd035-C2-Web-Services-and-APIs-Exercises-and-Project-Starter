package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository carRepository;
    private final PriceClient priceClient;
    private final MapsClient mapsClient;

    public CarService(CarRepository carRepository, PriceClient priceClient, MapsClient mapsClient) {
        this.carRepository = carRepository;
        this.priceClient = priceClient;
        this.mapsClient = mapsClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        List<Car> carList = carRepository.findAll();
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            fillInPriceAndLocation(car);
            carList.set(i, car);
        }
        return carList;
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) throws CarNotFoundException {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isEmpty()) {
            throw new CarNotFoundException("Car not found in DB");
        }
        Car car = optionalCar.get();
        fillInPriceAndLocation(car);

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            // update
            Optional<Car> optionalCarToBeUpdated = carRepository.findById(car.getId());
            if (optionalCarToBeUpdated.isEmpty()) {
                throw new CarNotFoundException("Car not found in DB");
            }
            Car carToBeUpdated = optionalCarToBeUpdated.get();
            carToBeUpdated.setCondition(car.getCondition());
            carToBeUpdated.setDetails(car.getDetails());
            carToBeUpdated.setLocation(car.getLocation());
            carToBeUpdated.setModifiedAt(LocalDateTime.now());

            car = carToBeUpdated;
        } else {
            // create new car
            car.setCreatedAt(LocalDateTime.now());
            car.setModifiedAt(LocalDateTime.now());
        }

        try {
            return carRepository.save(car);
        } catch (DataIntegrityViolationException ex) {
            throw new ManufacturerNotFoundException("Invalid Manufacturer code: " + car.getDetails().getManufacturer().getCode().toString());
        }
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) throws CarNotFoundException {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isEmpty()) {
            throw new CarNotFoundException("Car not found in DB");
        }
        Car car = optionalCar.get();

        carRepository.deleteById(car.getId());
    }

    private void fillInPriceAndLocation(Car car) {
        String price = priceClient.getPrice(car.getId());
        car.setPrice(price);

        Location fullLocation = mapsClient.getAddress(car.getLocation());
        car.setLocation(fullLocation);
    }
}
