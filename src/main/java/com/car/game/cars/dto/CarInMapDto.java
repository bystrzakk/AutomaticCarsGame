package com.car.game.cars.dto;

import com.car.game.common.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarInMapDto {
    private CarsDto car;
    private String mapName;
    private Integer X;
    private Integer Y;
    private Direction direction;
}
