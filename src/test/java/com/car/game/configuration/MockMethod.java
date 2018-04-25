package com.car.game.configuration;

import com.car.game.common.enums.Direction;
import com.car.game.game.MapInformation;
import com.car.game.game.Position;

import java.util.concurrent.ConcurrentHashMap;

public class MockMethod {

    protected ConcurrentHashMap<Position, MapInformation> getMap(){
        ConcurrentHashMap<Position, MapInformation> map = new ConcurrentHashMap<>();
        map.put(new Position(0,0), getMapInformation());
        return map;
    }

    protected MapInformation getMapInformation(){
        MapInformation mapInformation = new MapInformation();
        mapInformation.setIsWall(Boolean.FALSE);
        mapInformation.setDirection(Direction.N);
        mapInformation.setIsCrashed(Boolean.FALSE);
        return mapInformation;
    }
}
