package com.car.game.common.model;

import com.car.game.common.enums.CarType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class CarPk  implements Serializable {
    private String name;
    @Enumerated(EnumType.STRING)
    private CarType type;
}

