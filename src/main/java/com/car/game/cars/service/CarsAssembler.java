package com.car.game.cars.service;

import com.car.game.cars.dto.CarDto;
import com.car.game.common.model.CarPk;
import org.springframework.stereotype.Component;

@Component
public class CarsAssembler {

    public CarPk getCarPk(CarDto carDto) {
        CarPk carPk =  CarPk
                .builder()
                .name(carDto.getName())
                .type(carDto.getType())
                .build();
        return carPk;
    }
}
