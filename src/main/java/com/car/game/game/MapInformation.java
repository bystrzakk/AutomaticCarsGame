package com.car.game.game;

import com.car.game.common.enums.Direction;
import com.car.game.common.model.CarPk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.car.game.common.enums.Direction.N;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MapInformation {

    private Boolean isWall;
    private CarPk car ;
    private Direction direction = N;
    private Boolean isCrashed;

    public MapInformation(Boolean isWall ) {
        this.isWall = isWall;
    }
}
