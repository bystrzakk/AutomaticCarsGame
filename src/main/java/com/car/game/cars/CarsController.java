package com.car.game.cars;

import com.car.game.cars.dto.CarDto;
import com.car.game.cars.dto.CarMoveDto;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.service.CarsService;
import com.car.game.common.model.Car;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarsController {

    private CarsService carsService;

    @PostMapping(value = "/car")
    @ApiOperation("Add new car")
    @ResponseStatus(CREATED)
    public boolean addCar(@RequestBody CarDto carDto){
        return carsService.addCar(carDto);
    }

    @DeleteMapping(value = "/car")
    @ApiOperation("Remove car")
    @ResponseStatus(NO_CONTENT)
    public void deleteCar(@RequestBody CarDto carsDto){
        carsService.deleteCar(carsDto);
    }

    @GetMapping(value = "/cars")
    @ApiOperation("Get all cars")
    @ResponseStatus(OK)
    public List<Car> getCars(){
        return carsService.getCars() ;
    }

    @PostMapping(value = "/car/first/setup")
    @ApiOperation("Add car to game controller")
    @ResponseStatus(OK)
    public boolean putCarInMap(@RequestBody CarSetup carSetup){

        return carsService.addCarToMap(carSetup);
    }

    @PostMapping(value = "/car/move")
    @ApiOperation("Move car on game map controller")
    @ResponseStatus(OK)
    public void moveCarOnMap(@RequestBody CarMoveDto carMove){
        carsService.moveCar(carMove);
    }
}
