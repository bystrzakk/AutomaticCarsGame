package com.car.game.Game.Service;

import com.car.game.Common.Repository.MapRepository;
import org.springframework.stereotype.Component;

@Component
public class GameService {
    private MapRepository mapRepository;

    public GameService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }
}
