package com.car.game.cars.service;


import com.car.game.cars.dto.CarInformation;
import com.car.game.common.enums.CarType;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarHistory;
import com.car.game.common.repository.CarHistoryrepository;
import com.car.game.common.repository.CarRepository;
import com.car.game.game.FieldPosition;
import com.car.game.game.FieldlInformation;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CarsServiceTest {

    private CarRepository carRepository;
    private CarsService carsService;
    private CarHistoryrepository carHistoryrepository;
    private CarsAssembler carsAssembler;

    @Before
    public void setUp() throws Exception {
        carRepository = mock(CarRepository.class);
        carsService = new CarsService(carRepository, carsAssembler, carHistoryrepository);
    }

    @Test
    public void shouldReturnAllCars() throws Exception{
        when(carRepository.findAll()).thenReturn(getCars());
        final List<Car> carList = carsService.getCars();

        //assertThat(carList.get(0).getCarPk().getName()).isEqualTo(getCars().get(0).getCarPk().getName());
    }

    @Test
    public void shouldReturnTrueWhenAddingNewCar() throws Exception{
        final boolean isCarAdded = carsService.addCar(getCarDto());

        assertThat(isCarAdded).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenAddingExistingCar() throws Exception{
        //when(repository.findCarByCarPk(any())).thenReturn(getCar());
        final boolean isCarAdded = carsService.addCar(getCarDto());

        assertThat(isCarAdded).isFalse();
    }

    @Test
    public void shouldReturnNullForNoCriteria() throws Exception{

        //final FieldPosition futureFieldPosition = carsService.checkFuturePosition(new FieldlInformation(), new FieldPosition(0, 0));

        //assertThat(futureFieldPosition).isEqualTo(null);
    }

    @Test
    public void shouldReturnTrueWhenRemovingExistingCar() throws Exception{
        when(carRepository.existsById(any())).thenReturn(true);
        final boolean deleteCar = carsService.deleteCar(getCarDto());

        assertThat(deleteCar).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenRemovingNotExistingCar() throws Exception{
        when(carRepository.existsById(any())).thenReturn(false);
        final boolean deleteCar = carsService.deleteCar(getCarDto());

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