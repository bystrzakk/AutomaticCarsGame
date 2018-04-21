package com.car.game.game;

import com.car.game.cars.dto.CarMove;
import com.car.game.common.model.Map;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ActualInformation {

    private static ActualInformation getActulaInformation = new ActualInformation();
    private static String  actualMapName = null;
    private static ConcurrentHashMap<Position,MapInformation> mapGame = null;

    private ActualInformation() {
    }

    public static ConcurrentHashMap<Position,MapInformation>  getMapGame(){
        return mapGame;
    }
    public static void setActualMapName(String actualMapName) {
        ActualInformation.actualMapName = actualMapName;
    }

    public static void setMapGame(ConcurrentHashMap<Position, MapInformation> mapGame) {
        ActualInformation.mapGame = mapGame;
    }

    public static ActualInformation getGetActulaInformation() {
        return getActulaInformation;
    }

    public void move(CarMove carMove){
        //trzeba dorobic logike
    }

    public  String getActualMapName() {
        return actualMapName;
    }

    public  void setActualMapName(String actualMapName, Map map) {
        ActualInformation.actualMapName = actualMapName;
        loadMapGame(map);
    }

    public  void loadMapGame(Map map) {
        mapGame = new ConcurrentHashMap<>();
        List<String> mapInListOfString = Arrays.asList(map.getMapBody().split(","));
        List<Integer> mapInListOfInt = mapInListOfString.
                stream().
                map(Integer::parseInt).
                collect(Collectors.toList());
       int mapSize = (int)Math.sqrt(mapInListOfInt.size());
       for(int y = 0 ; y<mapSize;y++){
           int mapSizeX = mapSize;
           for(int x = 0 ; x<mapSizeX;x++){
               convertToConcurentHashMap(x,y,mapInListOfInt.get((y*mapSize)+x));
           }
       }
    }

    public void convertToConcurentHashMap(int x,int y, int wall){
        Position position = new Position(x,y);

        Boolean isWall = false;
        if(wall==1){
            isWall = true;
        }
        MapInformation mapInformation = new MapInformation(isWall);
        mapGame.put(position,mapInformation);
    }

    public Boolean isWall(Position position){
        MapInformation mapInformation = mapGame.get(position);
        if(mapInformation==null){
            return true;
        }
        if(mapInformation.getIsWall()){
          return true;
        }
        return  false;
    }
}
