package com.car.game.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MapInGame {
    private ConcurrentHashMap<FieldPosition,FieldlInformation> map = new ConcurrentHashMap<>();
    private int size = 0;
}
