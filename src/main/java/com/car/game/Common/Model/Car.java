package com.car.game.Common.Model;

import com.car.game.Common.Enum.CarType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    private long id;
    private String name;
    private CarType type;
}
