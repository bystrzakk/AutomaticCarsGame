package com.car.game.map.service;

import com.car.game.common.model.MapGame;
import com.car.game.common.repository.MapRepository;
import com.car.game.game.ActualInformation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Log
@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MapService {

    private MapRepository mapRepository;

    public List<MapGame> getAllMaps() {
        return mapRepository.findAll();
    }

    public boolean addNewMap(String mapName, String body) {
        if (!isMapExist(mapName) && correctMapBodyFormat(body)) {
            MapGame mapGame = new MapGame();
            mapGame.setName(name);
            //todo: zmienić na body
            mapGame.setMapBody("1,0,1,0,1,0,0,1,0");
            mapGame.setUsed(false);
            mapGame.setDeleted(false);
            mapRepository.save(mapGame);

            log.info("New map `" + mapGame.getName() + "` was stored in Database");
            return true;
        }

        log.warning("The map `" + mapName + "` is curently added");
        return false;
    }

    private boolean isMapExist(String mapName) {
        return mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(mapName) == null ?  false : true;
    }

    public boolean selectMap(String mapName) {
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        if (actualInformation.getActualMapName() != null) {
            log.warning("You can't launch a new game, the selected map: " + actualInformation.getActualMapName() + " is currently in use.");
            return false;
        }

        MapGame mapGame = mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(mapName);

        if (mapGame == null) {
            log.info("The map " + mapName + " does not exist.");
            return false;
        }

        actualInformation.setActualMapName(mapName, mapGame);
        System.out.println(actualInformation.getConcuretnHashMapGame());
        mapGame.setUsed(true);
        mapRepository.save(mapGame);
        return true;
    }

    public void unselectMap() {
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        actualInformation.setActualMapName(null);
        actualInformation.setConcuretnHashMapGame(null);
    }

    public boolean deleteMap(String mapName) {
        MapGame mapGame = mapRepository.findByName(mapName);

        if (mapGame == null) {
            log.warning("The map was not found.");
            return false;
        }

        ActualInformation actualInformation = ActualInformation.getActualInformation();

        if (actualInformation.getActualMapName() == mapName) {
            log.warning("You can't delete the map: " + mapName + ". Is currently in use.");
            return false;
        }

        if (mapGame.isUsed()) {
            mapGame.setDeleted(true);
            mapRepository.save(mapGame);
        } else {
            mapRepository.delete(mapGame);
        }
        log.info("Map " + mapName + " was deleted.");
        return true;
    }
    
    private boolean correctMapBodyFormat(String mapBody){
        Integer splitedNumbers[] = Arrays.stream(mapBody.split(";|,")).map(Integer::parseInt).toArray(Integer[]::new);
        double squareOfSize = Math.sqrt(splitedNumbers.length);

        return (squareOfSize - Math.floor(squareOfSize)) == 0 ? true : false;
    }
}
