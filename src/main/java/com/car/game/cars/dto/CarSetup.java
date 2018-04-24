package com.car.game.cars.dto;

import com.car.game.game.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarSetup {
    private CarDto car;
    private String mapName;
    private Position position;
}
