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
public class MapService {

    private MapAssembler mapAssembler;
    private MapRepository mapRepository;
    private ActualInformation actualInformation = ActualInformation.getActualInformation();

    public MapService(MapAssembler mapAssembler, MapRepository mapRepository) {
        this.mapAssembler = mapAssembler;
        this.mapRepository = mapRepository;
    }

    public List<MapGame> getAllMaps() {
        return mapRepository.findAll();
    }

    public boolean addNewMap(String mapName, String body) {
        if (!isMapExist(mapName) && correctMapBodyFormat(body)) {
            mapRepository.save(mapAssembler.getMapGame(mapName,body));
            log.info("New map `" + mapName + "` was stored in Database");
            return true;
        }
        log.warning("The map `" + mapName + "` is curently added");
        return false;
    }

    protected boolean isMapExist(String mapName) {
        return mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(mapName) == null ?  false : true;
    }

    public boolean selectMap(String mapName) {
        if (actualInformation.getMapByName(mapName) != null) {
            log.warning("You can't launch a new game, the selected map: " + mapName+ " is currently in use.");
            return false;
        }
        MapGame mapGame = mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(mapName);
        if (mapGame == null) {
            log.info("The map " + mapName + " does not exist.");
            return false;
        }

        mapGame.setUsed(true);
        mapRepository.save(mapGame);
        actualInformation.loadMapToGame(mapGame);
        return true;
    }

    public void unselectMap(String name) {
        actualInformation.unloadMap(name);
    }

    public boolean deleteMap(String mapName) {
        MapGame mapGame = mapRepository.findByName(mapName);

        if (mapGame == null) {
            log.warning("The map was not found.");
            return false;
        }


        if (actualInformation.getMapByName(mapName)==null) {
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
