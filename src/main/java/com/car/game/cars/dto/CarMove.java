package com.car.game.cars.dto;

import com.car.game.common.enums.Move;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CarMove {
     private String carName;
     private Move move;
}
