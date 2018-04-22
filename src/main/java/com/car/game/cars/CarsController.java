package com.car.game.cars;

import com.car.game.cars.dto.CarMoveDto;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.dto.CarDto;
import com.car.game.cars.service.CarsService;
import com.car.game.common.model.Car;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarsController {

    private CarsService carsService;

    @PostMapping(value = "/car")
    @ApiOperation("Dodanie samochodu ")
    public boolean addCar(@RequestBody CarDto carDto){
         return  carsService.addCar(carDto);
    }

    @DeleteMapping(value = "/car")
    @ApiOperation("usuniecie samochodu ")
    public void deleteCar(@RequestBody CarDto carDto){
        carsService.deleteCar(carDto);
    }

    @GetMapping(value = "/cars")
    @ApiOperation("Pobranie wszyttsich dostepnych samochodów ")
    public List<Car> getCars(){
        return carsService.getCars() ;
    }

    @PostMapping(value = "/car/first/setup")
    @ApiOperation("Add car to game Controller")
    public void putCarInMap(@RequestBody CarSetup carSetup){
        carsService.addCarToMap(carSetup);
    }

    @PostMapping(value = "/car/move")
    @ApiOperation("Move car on MapGame Controller")
    public void moveCarOnMap(@RequestBody CarMoveDto carMoveDto){
        carsService.moveCar(carMoveDto);
    }
}
