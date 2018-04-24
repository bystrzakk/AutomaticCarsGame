package com.car.game.game;

import com.car.game.common.model.CarPk;
import com.car.game.common.model.MapGame;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log
public class ActualInformation {

    private static ActualInformation getActulaInformation = new ActualInformation();
    private static String  actualMapName = null;
    private static ConcurrentHashMap<Position,MapInformation> concuretnHashMapGame = null;
    private static int mapSize;

    private ActualInformation() {
    }

    public  int getMapSize() {
        return mapSize;
    }

    public  void setMapSize(int mapSize) {
        ActualInformation.mapSize = mapSize;
    }
    public  ConcurrentHashMap<Position,MapInformation> getConcuretnHashMapGame(){
        return concuretnHashMapGame;
    }
    public static void setActualMapName(String actualMapName) {
        ActualInformation.actualMapName = actualMapName;
    }

    public static void setConcuretnHashMapGame(ConcurrentHashMap<Position, MapInformation> concuretnHashMapGame) {
        ActualInformation.concuretnHashMapGame = concuretnHashMapGame;
    }

    public static ActualInformation getActualInformation() {
        return getActulaInformation;
    }

    public void updateConcurentHashMap( Position position,MapInformation mapInformation){
        ConcurrentHashMap<Position,MapInformation>  map = getConcuretnHashMapGame();
        map.replace(position,mapInformation);
    }

    public MapInformation getMapInformationByCar(CarPk carPk){
        ConcurrentHashMap<Position,MapInformation>  map = getConcuretnHashMapGame();
        MapInformation mapInformation = map
                .entrySet()
                .stream()
                .filter(a->a.getValue().getCar()==carPk)
                .map(a->a.getValue())
                .findFirst()
                .get();
        return mapInformation;
    }

    public Position getCarPositionByCar(CarPk carPk){
        ConcurrentHashMap<Position,MapInformation>  map = getConcuretnHashMapGame();
        Position position = map
                .entrySet()
                .stream()
                .filter(a->a.getValue().getCar()==carPk)
                .map(a->a.getKey())
                .findFirst()
                .get();
        return position;
    }

    public  String getActualMapName() {
        return actualMapName;
    }

    public  void setActualMapName(String actualMapName, MapGame mapGame) {
        ActualInformation.actualMapName = actualMapName;
        loadMapGame(mapGame);
    }

    public MapInformation getMapInformation(Position position){
        return concuretnHashMapGame.get(position);
    }

    public void loadCSVMapGame(MapGame map){
        String splitedFirstMapBody[] = map.getMapBody().split(";");
        int finalMapBody[][] = new int[splitedFirstMapBody.length][splitedFirstMapBody.length];

        for(int i = 0; i<splitedFirstMapBody.length; i++){
            String splitedSecondMapBody[] = splitedFirstMapBody[i].split(",");
            for(int j=0; j<splitedFirstMapBody.length; j++){
                finalMapBody[i][j] = Integer.parseInt(splitedSecondMapBody[j]);
            }
        }
    }

    public  void loadMapGame(MapGame mapGame) {
        concuretnHashMapGame = new ConcurrentHashMap<>();
        List<String> mapInListOfString = Arrays.asList(mapGame.getMapBody().split(","));
        List<Integer> mapInListOfInt = mapInListOfString.
                stream().
                map(Integer::parseInt).
                collect(Collectors.toList());
       int mapSize = (int)Math.sqrt(mapInListOfInt.size());
       setMapSize(mapSize);
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
        concuretnHashMapGame.put(position,mapInformation);
    }


    public Boolean isFree(Position position){
        MapInformation mapInformation = concuretnHashMapGame.get(position);
        Boolean isWall = isWall(mapInformation);
        Boolean isCar = isCar(mapInformation);
        return isWall&&isCar;
    }

    public Boolean isCar(MapInformation mapInformation){
        if(mapInformation.getCar()==null){
            return false;
        }
        return true;
    }

    public Boolean isWall(MapInformation mapInformation){
        if(mapInformation==null){
            log.info("Błedne miejsce");
            return false;
        }
        return mapInformation.getIsWall();
    }

}
