package com.car.game.game;

import com.car.game.cars.dto.CarMove;
import com.car.game.common.model.MapGame;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.util.*;
import java.util.stream.Collectors;

@Log
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActualInformation {

    private static ActualInformation getActualInformation = new ActualInformation();
    private static Map<String,MapInGame> actualMapsInGame = new TreeMap<>();

    public static ActualInformation getActualInformation() {
        return getActualInformation;
    }

    public MapInGame getMapByName(String name){
        return actualMapsInGame.get(name);
    }

    public FieldlInformation getFieldInformationByCar(CarMove car, String mapName) {
        MapInGame map = getMapByName(mapName);
        if(map==null){
            return null;
        }
        Optional<FieldlInformation> fieldlInformation = map.getMap()
                .entrySet()
                .stream()
                .filter(a->car.getCarName().equals(a.getValue().getCarName()))
                .map(a->a.getValue())
                .findAny();
        if(!fieldlInformation.isPresent()){
            return null;
        }
        return fieldlInformation.get();
    }

    public FieldPosition getCarPositionByCar(CarMove car, String mapName) {
        MapInGame map = getMapByName(mapName);
        if(map==null){
            return null;
        };
        Optional<FieldPosition> fieldPosition = map.getMap()
                .entrySet()
                .stream()
                .filter(a->car.getCarName().equals(a.getValue().getCarName()))
                .map(a->a.getKey())
                .findAny();
        if(!fieldPosition.isPresent()){
            return null;
        }
        return fieldPosition.get();
    }

    public FieldlInformation getMapInformation(FieldPosition fieldPosition,String mapName) {
        MapInGame map = actualMapsInGame.get(mapName);
        if(map!=null){
            return map.getMap().get(fieldPosition);
        }
        return null;
    }

    public void loadMapToGame(MapGame mapGame) {
        MapInGame map = new MapInGame();

        List<String> mapInListOfString = Arrays.asList(mapGame.getMapBody().split(",|;"));
        List<Integer> mapInListOfInt = mapInListOfString.
                stream().
                map(Integer::parseInt).
                collect(Collectors.toList());
        map.setSize((int)Math.sqrt(mapInListOfInt.size()));
       for(int y = 0 ; y<map.getSize();y++){

           for(int x = 0 ; x<map.getSize();x++){
               boolean isWall = mapInListOfInt.get((y*map.getSize())+x)==1?true:false;
               map.getMap()
                       .put(new FieldPosition(x,y),
                               new FieldlInformation(isWall));
           }
       }
       actualMapsInGame.put(mapGame.getName(),map);
    }

    public void unloadMap(String name){
        actualMapsInGame.remove(name);
        log.info("Unselect map"+ name);
    }
}
