package com.car.game.cars;

import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.dto.CarsDto;
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
    @ApiOperation("Adding new car")
    public boolean addCar(@RequestBody CarsDto carsDto){
         return  carsService.addCar(carsDto);
    }

    @DeleteMapping(value = "/car")
    @ApiOperation("Removing car")
    public void deleteCar(@RequestBody CarsDto carsDto){
        carsService.deleteCar(carsDto);
    }

    @GetMapping(value = "/cars")
    @ApiOperation("Getting all cars")
    public List<Car> getCars(){
        return carsService.getCars() ;
    }

    @PostMapping(value = "/car/first/setup")
    @ApiOperation("Add car to game controller")
    public void putCarInMap(@RequestBody CarSetup carSetup){
        carsService.addCarToMap(carSetup);
    }

    @PostMapping(value = "/car/move")
    @ApiOperation("Move car on game map controller")
    public void moveCarOnMap(@RequestBody CarMove carMove){
        carsService.moveCar(carMove);
    }
}
