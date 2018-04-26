package com.car.game.cars;

import com.car.game.cars.dto.CarInformation;
import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.service.CarsService;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarsController {

    private CarsService carsService;

    @PostMapping(value = "/car")
    @ApiOperation("Add new car")
    @ResponseStatus(CREATED)
    public boolean addCar(@RequestBody CarInformation carInformation){
        return carsService.addCar(carInformation);
    }

    @DeleteMapping(value = "/car")
    @ApiOperation("Remove car")
    @ResponseStatus(NO_CONTENT)
    public void deleteCar(@RequestBody CarInformation carsDto){
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
    public void moveCarOnMap(@RequestBody CarMove carMove){
        carsService.moveCar(carMove);
    }

    @GetMapping(value = "/car/history")
    @ApiOperation("Get movements history for given car")
    @ResponseStatus(OK)
    public List<Move> getCarHistory(@RequestParam(value = "carName") String carName){
        return carsService.getCarHistory(carName);
    }

    @DeleteMapping(value = "/car/delete-from-game")
    @ApiOperation("Delete car from the game controller")
    @ResponseStatus(OK)
    public boolean deleteCarFromMap(@RequestParam(value = "carName") String carName){
        return carsService.deleteCarFromMap(carName);
    }

    @PostMapping(value = "/car/repair")
    @ApiOperation("Repair car controller")
    @ResponseStatus(OK)
    public boolean repairCar(@RequestParam(value = "carName") String carName){
        return carsService.repairCar(carName);
    }
}
