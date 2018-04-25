package com.car.game.common.model;

import com.car.game.common.enums.CarType;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    private String name;
    private CarType type;
    private boolean isCrashed = false;
    private String mapName = "";

    public Car(String name){
        this.name = name;
    }
}
