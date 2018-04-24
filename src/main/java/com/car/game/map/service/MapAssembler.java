package com.car.game.map.service;

import com.car.game.common.model.MapGame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
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
