package com.car.game.game;

import com.car.game.common.model.Map;
import com.car.game.game.service.MapInformation;
import com.car.game.game.service.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ActualInformation {

    private static ActualInformation getActulaInformation = new ActualInformation();
    private static String  actualMapName = null;
    private static ConcurrentHashMap<Position,MapInformation> mapGame = null;
    private ActualInformation() {
    }

    public static ActualInformation getGetActulaInformation() {
        return getActulaInformation;
    }

    public  String getActualMapName() {
        return actualMapName;
    }

    public  void setActualMapName(String actualMapName, Map map) {
        ActualInformation.actualMapName = actualMapName;
        loadMapGame(actualMapName,map);
    }

    public  void loadMapGame(String mapName,Map map) {
        mapGame = new ConcurrentHashMap<>();
        List<String> mapInList = new ArrayList<String>(Arrays.asList(map.getMapBody().split(" ; ")));

        mapInList.stream().forEach(
              a->  convertToConcurentHashMap(a)
        );

    }

    public void convertToConcurentHashMap(String mapBlock){
        String[] mapDataTable = mapBlock.split(",");
        Position position = new Position(
                Integer.valueOf(mapDataTable[0]),
                Integer.valueOf(mapDataTable[1]));

        Boolean isWall = false;
        if(Integer.valueOf(mapDataTable[2])==1){
            isWall = true;
        }
        MapInformation mapInformation = new MapInformation(isWall);
        mapGame.put(position,mapInformation);
    }

}
