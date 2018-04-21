package com.car.game.game.service;

import com.car.game.common.enums.Direction;
import com.car.game.common.model.CarPk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MapInformation {

    private Boolean isWall;
    private CarPk car = null;
    private Direction direction;

    public MapInformation(Boolean isWall ){
        this.isWall = isWall;
    }
}
