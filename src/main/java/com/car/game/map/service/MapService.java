package com.car.game.map.service;

import com.car.game.common.model.MapGame;

import java.util.List;

public interface MapService {
    List<MapGame> getAllMaps();
    void addNewMap(MapGame mapGame);
    boolean isExist(String name);
    boolean startGame(String mapName);
    void stopGame();
}
