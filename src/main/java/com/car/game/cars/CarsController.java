package com.car.game.cars;

import com.car.game.cars.service.CarsDto;
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
    public boolean addCar(@RequestBody CarsDto carsDto){
         return  carsService.addCar(carsDto);
    }

    @GetMapping(value = "/cars")
    @ApiOperation("Pobranie wszyttsich dostepnych samochodów ")
    public List<Car> getCars(){
        return carsService.getCars() ;
    }

    @DeleteMapping(value = "/car")
    @ApiOperation("usuniecie samochodu ")
    public void deleteCar(@RequestBody CarsDto carsDto){
        carsService.deleteCar(carsDto);
    }
}
