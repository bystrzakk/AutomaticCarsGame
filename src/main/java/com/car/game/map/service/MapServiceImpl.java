package com.car.game.map.service;

import com.car.game.common.model.MapGame;
import com.car.game.common.repository.MapRepository;
import com.car.game.game.ActualInformation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Log
@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MapServiceImpl implements MapService {

    private MapRepository mapRepository;

    @Override
    public List<MapGame> getAllMaps() {
        return mapRepository.findAll();
    }

    @Override
    public boolean addNewMap(String name, String body) {
        if (!this.isMapExist(name)) {
            MapGame mapGame = new MapGame();
            mapGame.setName(name);
            //todo: zmienić na body
            mapGame.setMapBody("1,0,1,0,1,0,0,1,0");
            mapGame.setUsed(false);
            mapGame.setDeleted(false);
            mapRepository.save(mapGame);

            log.info("New mapGame was added to DB");
            return true;
        }

        log.info("The map is curently added");
        return false;
    }

    @Override
    public boolean isMapExist(String name) {
        MapGame mapGame = mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(name);

        if (mapGame == null) {
            return false;
        }

        return true;
    }

    public boolean startGame(String mapName){
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        if(actualInformation.getActualMapName()!=null){
            log.info("nie mozna uruchomic nowej gry, obecnie jest aktywna mapa :"+ actualInformation.getActualMapName());
            return false;
        }

        MapGame mapGame = mapRepository.findByNameAndUsedIsFalseAndDeletedIsFalse(mapName);

        if ( mapGame ==null){
            log.info("bark mapy");
            return false;
        }

        actualInformation.setActualMapName(mapName, mapGame);
        System.out.println(actualInformation.getConcuretnHashMapGame());
        mapGame.setUsed(true);
        mapRepository.save(mapGame);
        return true;
    }

    @Override
    public void stopGame() {
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        actualInformation.setActualMapName(null);
        actualInformation.setConcuretnHashMapGame(null);
    }

    @Override
    public boolean deleteMap(String name) {
        MapGame mapGame = mapRepository.findByName(name);

        if (mapGame == null) {
            log.info("The map was not found.");
            return false;
        }

        ActualInformation actualInformation = ActualInformation.getActualInformation();

        if (actualInformation.getActualMapName() == name) {
            log.info("You can't delete the map. Is currently in use.");
            return false;
        }

        if (mapGame.isUsed()) {
            mapGame.setDeleted(true);
            mapRepository.save(mapGame);
        } else {
            mapRepository.delete(mapGame);
        }
        log.info("Map was deleted.");
        return true;
    }
}
