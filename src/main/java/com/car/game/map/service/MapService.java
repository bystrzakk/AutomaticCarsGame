package com.car.game.map.service;

import com.car.game.common.model.MapGame;

import java.util.List;

public interface MapService {
    List<MapGame> getAllMaps();
    boolean addNewMap(String name, String body);
    boolean isMapExist(String name);
    boolean startGame(String mapName);
    void stopGame();
    boolean deleteMap(String name);
}
