package com.car.game.cars.dto;

import com.car.game.common.enums.CarType;
import com.car.game.common.enums.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarMove {
     private String name;
     private CarType type;
     private Move move;
     private String mapName;
}
