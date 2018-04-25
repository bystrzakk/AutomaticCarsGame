package com.car.game.map.service;

import com.car.game.common.model.MapGame;
import org.springframework.stereotype.Component;

@Component
public class MapAssembler {

    public MapGame getMapGame(String mapName, String body){
        MapGame mapGame =  MapGame.builder()
                .name(mapName)
                .mapBody(body)
                .used(false)
                .deleted(false)
                .build();
        return mapGame;
    }
}
