package com.car.game.cars.service;


import com.car.game.cars.dto.CarInformation;
import com.car.game.common.enums.CarType;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarHistory;
import com.car.game.common.repository.CarHistoryRepository;
import com.car.game.common.repository.CarRepository;
import com.car.game.game.ActualInformation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CarServiceTest {


    private CarService carService;
    private CarRepository carRepository;
    private CarHistoryRepository carHistoryRepository;
    private ActualInformation actualInformation = ActualInformation.getActualInformation();
    private SimpMessageSendingOperations messageTemplate;

    @Before
    public void setUp() throws Exception {
        carRepository = mock(CarRepository.class);
        carService = new CarService(carRepository, carHistoryRepository, messageTemplate);
    }

    @Test
    public void shouldReturnAllCars() throws Exception{
        when(carRepository.findAll()).thenReturn(getCars());
        final List<Car> carList = carService.getCars();
        assertThat(carList.get(0).getName()).isEqualTo(getCar(false).getName());
    }

    @Test
    public void shouldReturnTrueWhenAddingNewCar() throws Exception{
        final boolean isCarAdded = carService.addCar(getCarDto());

        assertThat(isCarAdded).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenAddingExistingCar() throws Exception{
        when(carRepository.findCarByName((any()))).thenReturn(getCar(false));
        final boolean isCarAdded = carService.addCar(getCarDto());

        assertThat(isCarAdded).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenRemovingExistingCar() throws Exception{
        when(carRepository.existsById(any())).thenReturn(true);
        when(carRepository.findCarByName(anyString())).thenReturn(getCar(false));
        final boolean deleteCar = carService.deleteCar(getCarDto());

        assertThat(deleteCar).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenRemovingNotExistingCar() throws Exception{
        when(carRepository.existsById(any())).thenReturn(false);
        final boolean deleteCar = carService.deleteCar(getCarDto());

        assertThat(deleteCar).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenRepairCarIsNotCrashed() throws Exception{
        when(carRepository.findCarByName(anyString())).thenReturn(getCar(false));
        final boolean repairCar = carService.repairCar("testCarName");

        assertThat(repairCar).isFalse();
    }

    @Test
    public void shouldReturnTrueWhenRepairCarIsCrashed() throws Exception{
        when(carRepository.findCarByName(anyString())).thenReturn(getCar(true));
        final boolean repairCar = carService.repairCar("testCarName");

        assertThat(repairCar).isTrue();
    }

    private CarInformation getCarDto(){
        return new CarInformation("testCarName",CarType.NORMAL);
    }

    private Car getCar(boolean isCrashed){
        return new Car("testCarName",CarType.NORMAL,"testMapName",isCrashed, Arrays.asList(new CarHistory()));
    }

    private List<CarHistory> getCarMovements(){
        return Arrays.asList(new CarHistory(1l, getCar(false), Move.FORWARD));
    }

    private List<Car> getCars(){
        return Arrays.asList(getCar(false));
    }
}