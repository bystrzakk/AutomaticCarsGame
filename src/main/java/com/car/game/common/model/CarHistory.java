package com.car.game.common.model;

import com.car.game.common.enums.Move;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CAR_HISTORY")
public class CarHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "name")
    @JsonManagedReference
    private Car car;
    @Enumerated(EnumType.STRING)
    private Move move;

}
