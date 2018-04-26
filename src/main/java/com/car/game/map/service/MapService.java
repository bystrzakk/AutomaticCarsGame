package com.car.game.map.service;

import com.car.game.common.model.MapGame;
import com.car.game.common.repository.MapRepository;
import com.car.game.game.ActualInformation;
import com.car.game.map.dto.MapRequestDto;

import lombok.extern.java.Log;

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

    public boolean addNewMap(MapRequestDto mapRequestDto) {
        if (!isMapExist(mapRequestDto.getName()) && correctMapBodyFormat(mapRequestDto.getBody())) {
            mapRepository.save(mapAssembler.getMapGame(mapRequestDto.getName(), mapRequestDto.getBody()));
            log.info("New map `" + mapRequestDto.getName() + "` was stored in database");
            return true;
        }
        log.warning("Map `" + mapRequestDto.getName() + "` is currently added");
        return false;
    }

    protected boolean isMapExist(String mapName) {
        return mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(mapName) == null ?  false : true;
    }

    public boolean selectMap(String mapName) {
        if (actualInformation.getMapByName(mapName) != null) {
            log.warning("You can't launch a new game, selected map: " + mapName+ " is currently in use.");
            return false;
        }
        MapGame mapGame = mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(mapName);
        if (mapGame == null) {
            log.info("Map " + mapName + " does not exist.");
            return false;
        }

        mapGame.setUsed(true);
        mapRepository.save(mapGame);
        actualInformation.loadMapToGame(mapGame);
        log.info("Select map :"+ mapName);
        return true;
    }

    public void unselectMap(String name) {
        actualInformation.unloadMap(name);
    }

    public boolean deleteMap(String mapName) {
        MapGame mapGame = mapRepository.findByName(mapName);

        if (mapGame == null) {
            log.warning("Map was not found.");
            return false;
        }


        if (actualInformation.getMapByName(mapName)!=null) {
            log.warning("You can't delete the map: " + mapName + ". Currently in use.");
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
