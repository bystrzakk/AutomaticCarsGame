package com.car.game.common.model;

import com.car.game.common.enums.CarType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // jelsi klasa Car uzywa  @EmbeddedId tutaj  @Embeddable jest zbedne 
public class CarPk  implements Serializable {
    private String name;
    @Enumerated(EnumType.STRING)
    private CarType type;
}

