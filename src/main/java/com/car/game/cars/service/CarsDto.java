package com.car.game.cars.service;

import com.car.game.common.enums.CarType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarsDto {
    private @NotNull String name;
    private CarType type;
}
