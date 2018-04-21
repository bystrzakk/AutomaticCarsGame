package com.car.game.cars.dto;

import com.car.game.common.enums.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarMove {
     private CarsDto car;
     private Move move;
}
