package com.car.game.configuration;

import com.car.game.common.enums.Direction;
import com.car.game.game.FieldlInformation;
import com.car.game.game.FieldPosition;

import java.util.concurrent.ConcurrentHashMap;

public class MockMethod {

    protected ConcurrentHashMap<FieldPosition, FieldlInformation> getMap(){
        ConcurrentHashMap<FieldPosition, FieldlInformation> map = new ConcurrentHashMap<>();
        map.put(new FieldPosition(0,0), getMapInformation());
        return map;
    }

    protected FieldlInformation getMapInformation(){
        FieldlInformation fieldlInformation = new FieldlInformation();
        fieldlInformation.setIsWall(Boolean.FALSE);
        fieldlInformation.setDirection(Direction.N);
        fieldlInformation.setIsCrashed(Boolean.FALSE);
        return fieldlInformation;
    }
}
