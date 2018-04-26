package com.car.game.game;

import com.car.game.cars.dto.CarMove;
import com.car.game.common.model.MapGame;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public FieldlInformation getFieldInformationByCar(CarMove car) {
        MapInGame map = getMapByName(car.getMapName());
        if(map==null){
            return null;
        }
        FieldlInformation fieldlInformation = map.getMap()
                .entrySet()
                .stream()
                .filter(a->a.getValue().getCarName()==car.getName())
                .map(a->a.getValue())
                .findFirst()
                .get();
        return fieldlInformation;
    }

    public FieldPosition getCarPositionByCar(CarMove car) {
        MapInGame map = getMapByName(car.getMapName());
        if(map==null){
            return null;
        };
        FieldPosition fieldPosition = map.getMap()
                .entrySet()
                .stream()
                .filter(a->a.getValue().getCarName()==car.getName())
                .map(a->a.getKey())
                .findFirst()
                .get();
        return fieldPosition;
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
       int mapSize = (int)Math.sqrt(mapInListOfInt.size());
       for(int y = 0 ; y<mapSize;y++){
           int mapSizeX = mapSize;
           for(int x = 0 ; x<mapSizeX;x++){
               boolean isWall = mapInListOfInt.get((y*mapSize)+x)==1?true:false;
               map.getMap()
                       .put(new FieldPosition(x,y),
                               new FieldlInformation(isWall));
           }
       }
       actualMapsInGame.put(mapGame.getName(),map);
    }

    public void unloadMap(String name){
        actualMapsInGame.remove(name);
    }
}
