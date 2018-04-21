package com.car.game.Map.Service;

import com.car.game.Common.Model.Map;

import java.util.List;

public interface MapService {
    List<Map> getAllMaps();
    void addNewMap(Map map);

}
