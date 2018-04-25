package com.car.game.common.model;

import com.car.game.common.enums.CarType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    private String name;
    private CarType type;
    private String mapName;
    @OneToMany(mappedBy = "car")
    private List<CarHistory> movements;
    private boolean isCrashed;
}
