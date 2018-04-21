package com.car.game.map.service;

import com.car.game.common.model.Map;

import java.util.List;

public interface MapService {
    List<Map> getAllMaps();
    void addNewMap(Map map);
    boolean isExist(String name);
    boolean startGame(String mapName);

}
