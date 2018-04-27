package com.car.game.cars;

import com.car.exception.RemoveCarException;
import com.car.game.cars.dto.CarInformation;
import com.car.game.cars.dto.CarMove;
import com.car.game.cars.dto.CarSetup;
import com.car.game.cars.service.CarService;
import com.car.game.common.enums.Move;
import com.car.game.common.model.Car;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarController {

    private CarService carService;

    @PostMapping(value = "/car")
    @ApiOperation("Add new car")
    @ResponseStatus(CREATED)
    public boolean addCar(@RequestBody CarInformation carInformation){
        return carService.addCar(carInformation);
    }

    @DeleteMapping(value = "/car")
    @ApiOperation("Remove car")
    public ResponseEntity<String> deleteCar(@RequestBody CarInformation carInformation){
        final boolean isCarDeleted = carService.deleteCar(carInformation);
        if (isCarDeleted==false){
            throw new RemoveCarException("Remove car exception");
        }
        return new ResponseEntity<>("Car is removed", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/cars")
    @ApiOperation("Get all cars")
    @ResponseStatus(OK)
    public List<Car> getCars(){
        return carService.getCars() ;
    }

    @PostMapping(value = "/car-first-setup")
    @ApiOperation("Add car to game controller")
    @ResponseStatus(OK)
    public boolean putCarInMap(@RequestBody CarSetup carSetup){
        return carService.addCarToMap(carSetup);
    }

    @PostMapping(value = "/car-move")
    @ApiOperation("Move car on game map controller")
    @ResponseStatus(OK)
    public void moveCarOnMap(@RequestBody CarMove carMove){
        carService.moveCar(carMove);
    }

    @GetMapping(value = "/car-history")
    @ApiOperation("Get movements history for given car")
    @ResponseStatus(OK)
    public List<Move> getCarHistory(@RequestParam(value = "carName") String carName){
        return carService.getCarHistory(carName);
    }

    @DeleteMapping(value = "/car-remove-from-game")
    @ApiOperation("Delete car from the game controller")
    public ResponseEntity<String> deleteCarFromMap(@RequestParam(value = "carName") String carName){
        carService.deleteCarFromMap(carName);
        return new ResponseEntity<>("Car is removed",HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/car-repair")
    @ApiOperation("Repair car controller")
    public ResponseEntity<String> repairCar(@RequestParam(value = "carName") String carName){
        carService.repairCar(carName);
        return new ResponseEntity<>("Car repaired", HttpStatus.OK);
    }
}
