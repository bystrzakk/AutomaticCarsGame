package com.car.game.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @EmbeddedId
    private CarPk carPk;
    private String mapName;
    @OneToMany(mappedBy = "car")
    private List<CarHistory> movements;
    private boolean isCrashed;
}
