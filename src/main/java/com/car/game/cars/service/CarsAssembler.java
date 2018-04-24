package com.car.game.cars.service;

import com.car.game.cars.dto.CarDto;
import com.car.game.common.enums.CarType;
import com.car.game.common.model.CarPk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CarsAssembler {

    public CarPk getCarPk(CarDto carDto) {
        CarPk carPk =  CarPk
                .builder()
                .name(carDto.getName())
                .type(carDto.getType())
                .build();
        return carPk;
    }

    public CarPk getCarPk(String name , CarType carType) {
        CarPk carPk =  CarPk
                .builder()
                .name(name)
                .type(carType)
                .build();
        return carPk;
    }
}
