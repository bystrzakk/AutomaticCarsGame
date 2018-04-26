package com.car.game.common.model;

import com.car.game.common.enums.CarType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    private String name;
    @Enumerated(EnumType.STRING)
    private CarType type;
    private String mapName;
    @OneToMany(mappedBy = "car")
    private List<CarHistory> movements;
    private boolean isCrashed;
}
