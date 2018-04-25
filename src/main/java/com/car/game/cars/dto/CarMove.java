package com.car.game.cars.dto;

import com.car.game.common.enums.Move;
import com.car.game.common.model.CarPk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarMove {
     private CarPk car;
     private Move move;
     private String mapName;
}
