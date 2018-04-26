package com.car.game.cars.service;


import com.car.game.cars.dto.CarInformation;
import com.car.game.common.enums.CarType;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarHistory;
import com.car.game.common.repository.CarHistoryRepository;
import com.car.game.common.repository.CarRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CarServiceTest {

    private CarRepository carRepository;
    private CarService carService;
    private CarHistoryRepository carHistoryRepository;
    private CarAssembler carsAssembler;

    @Before
    public void setUp() throws Exception {
        carRepository = mock(CarRepository.class);
        carService = new CarService(carRepository, carsAssembler, carHistoryRepository);
    }

    @Test
    public void shouldReturnAllCars() throws Exception{
        when(carRepository.findAll()).thenReturn(getCars());
        final List<Car> carList = carService.getCars();

        //assertThat(carList.get(0).getCarPk().getName()).isEqualTo(getCars().get(0).getCarPk().getName());
    }

    @Test
    public void shouldReturnTrueWhenAddingNewCar() throws Exception{
        final boolean isCarAdded = carService.addCar(getCarDto());

        assertThat(isCarAdded).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenAddingExistingCar() throws Exception{
        //when(repository.findCarByCarPk(any())).thenReturn(getCar());
        final boolean isCarAdded = carService.addCar(getCarDto());

        assertThat(isCarAdded).isFalse();
    }

    @Test
    public void shouldReturnNullForNoCriteria() throws Exception{

        //final FieldPosition futureFieldPosition = carService.checkFuturePosition(new FieldlInformation(), new FieldPosition(0, 0));

        //assertThat(futureFieldPosition).isEqualTo(null);
    }

    @Test
    public void shouldReturnTrueWhenRemovingExistingCar() throws Exception{
        when(carRepository.existsById(any())).thenReturn(true);
        final boolean deleteCar = carService.deleteCar(getCarDto());

        assertThat(deleteCar).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenRemovingNotExistingCar() throws Exception{
        when(carRepository.existsById(any())).thenReturn(false);
        final boolean deleteCar = carService.deleteCar(getCarDto());

        assertThat(deleteCar).isFalse();
    }

    private CarInformation getCarDto(){
        return new CarInformation("testCarName",CarType.NORMAL);
    }

    private Car getCar(){
        return new Car();
    }



    private List<CarHistory> getCarMovements(){
        return Arrays.asList(new CarHistory(1l, getCar(), Move.FORWARD));
    }

    private List<Car> getCars(){
        return Arrays.asList(getCar());
    }
}