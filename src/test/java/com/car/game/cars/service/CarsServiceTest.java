package com.car.game.cars.service;


import com.car.game.cars.dto.CarDto;
import com.car.game.common.enums.CarType;
import com.car.game.common.enums.Direction;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarPk;
import com.car.game.common.repository.CarRepository;
import com.car.game.game.MapInformation;
import com.car.game.game.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class CarsServiceTest {

    private CarRepository repository;
    private CarsService carsService;
    private CarsAssembler carsAssembler;

    @Before
    public void setUp() throws Exception {
        repository = mock(CarRepository.class);
        carsService = new CarsService(repository,carsAssembler);
    }

    @Test
    public void shouldReturnAllCars() throws Exception{
        when(repository.findAll()).thenReturn(getCars());
        final List<Car> carList = carsService.getCars();

        assertThat(carList.get(0).getCarPk().getName()).isEqualTo(getCars().get(0).getCarPk().getName());
    }

    @Test
    public void shouldReturnTrueWhenAddingNewCar() throws Exception{
        final boolean isCarAdded = carsService.addCar(getCarDto());

        assertThat(isCarAdded).isEqualTo(true);
    }

    @Test
    public void shouldReturnFalseWhenAddingExistingCar() throws Exception{
        when(repository.findCarByCarPk(any())).thenReturn(getCar());
        final boolean isCarAdded = carsService.addCar(getCarDto());

        assertThat(isCarAdded).isEqualTo(false);
    }

    @Test
    public void shouldReturnNullForNoCriteria() throws Exception{

        final Position futurePosition = carsService.checkFuturePosition(new MapInformation(), new Position(0, 0));

        assertThat(futurePosition).isEqualTo(null);
    }

    @Test
    public void shouldReturnTrueWhenRemovingExistingCar() throws Exception{
        when(repository.existsById(any())).thenReturn(true);
        final boolean deleteCar = carsService.deleteCar(getCarDto());

        assertThat(deleteCar).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenRemovingNotExistingCar() throws Exception{
        when(repository.existsById(any())).thenReturn(false);
        final boolean deleteCar = carsService.deleteCar(getCarDto());

        assertThat(deleteCar).isFalse();
    }

    private CarDto getCarDto(){
        return new CarDto("testCarName",CarType.NORMAL);
    }

    private Car getCar(){
        return new Car(getCarPk(),"testMapName",false);
    }

    private CarPk getCarPk(){
        return new CarPk("testCarName", CarType.NORMAL);
    }

    private List<Car> getCars(){
        return Arrays.asList(getCar());
    }
}