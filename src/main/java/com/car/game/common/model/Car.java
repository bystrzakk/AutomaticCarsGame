package com.car.game.common.model;

import com.car.game.common.enums.CarType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String mapName = "";
    private boolean isCrashed;

    @OneToMany(mappedBy = "car",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonBackReference
    private List<CarHistory> carHistory = new ArrayList<>();
}
