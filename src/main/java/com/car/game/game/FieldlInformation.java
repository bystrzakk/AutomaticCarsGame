package com.car.game.game;

import com.car.game.common.enums.CarType;
import com.car.game.common.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.car.game.common.enums.Direction.N;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldlInformation {

    private Boolean isWall;
    private String carName;
    private CarType type;
    private Direction direction = N;
    private Boolean isCrashed;

    public FieldlInformation(Boolean isWall ) {
        this.isWall = isWall;
    }
}
