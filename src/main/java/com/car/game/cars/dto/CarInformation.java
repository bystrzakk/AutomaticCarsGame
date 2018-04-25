package com.car.game.cars.dto;

import com.car.game.common.enums.CarType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarInformation {
    private @NotNull String name;
    private CarType type;
}
