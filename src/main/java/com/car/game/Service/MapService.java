package com.car.game.Service;

import com.car.game.Model.Map;

import java.util.List;

public interface MapService {
    List<Map> getAllMaps();
    void addNewMap(Map map);

}
