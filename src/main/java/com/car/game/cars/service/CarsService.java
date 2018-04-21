package com.car.game.cars.service;

import com.car.game.cars.dto.CarInMapDto;
import com.car.game.cars.dto.CarsDto;
import com.car.game.common.enums.CarType;
import com.car.game.common.model.Car;
import com.car.game.common.model.CarPk;
import com.car.game.common.repository.CarRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log
@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarsService {

    private CarRepository carRepository;

    @Transactional
    public boolean addCar(CarsDto carsDto){
        if( !isExist(carsDto)){
            Car car = new Car();
            CarPk carPk = getCarPk(carsDto);
            car.setCarPk(carPk);
            carRepository.save(car);
            log.info("Dodano nowy samochod ");
            return true;
        }
        log.info("Istnieje juz taki samochód");
        return false;

    }

    private boolean isExist(CarsDto carsDto){
        CarPk carPk = getCarPk(carsDto);
        Car car = carRepository.findCarByCarPk(carPk);
        if(car==null){
            return false;
        }
        return true;
    }

    public List<Car> getCars(){
        return carRepository.findAll();
    }

    public void deleteCar(CarsDto carsDto) {

        CarPk carPk = getCarPk(carsDto);
        boolean exist = carRepository.existsById(carPk);
        if(exist){
            carRepository.deleteById(carPk);
        }
    }


    private CarPk getCarPk(CarsDto carsDto) {
        CarPk carPk = new CarPk();
        carPk.setName(carsDto.getName());
        carPk.setType(carsDto.getType());
        return carPk;
    }
    private CarPk getCarPk(String name , CarType carType) {
        CarPk carPk = new CarPk();
        carPk.setName(name);
        carPk.setType(carType);
        return carPk;
    }


    @Transactional
    public void updateCars(CarInMapDto carInMapDto) {
        if( !isExist(carInMapDto.getCar())){
            Car car = new Car();
            CarPk carPk = getCarPk(carInMapDto.getCar().getName(),carInMapDto.getCar().getType());
            car.setCarPk(carPk);
            car.setMapName(carInMapDto.getMapName());
            carRepository.save(car);
            log.info("Dodano  samochod  do gry");
        }
        log.info("Nie ma takiego samochodu");
    }
}
