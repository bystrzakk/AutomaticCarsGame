package com.car.game.cars.dto;

import com.car.game.game.FieldPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarSetup {
    private @NotNull CarDto car;
    private @NotNull String mapName;
    private @NotNull FieldPosition fieldPosition;
}
