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
public class MapService {

    private MapRepository mapRepository;

    public List<MapGame> getAllMaps() {
        return mapRepository.findAll();
    }

    public Boolean addNewMap(String mapName,String body) {
        if(isExist(mapName)){
            MapGame mapGame = new MapGame();
            mapGame.setName(mapName);
            mapGame.setMapBody(body);
            mapGame.setUsed(false);
            mapRepository.save(mapGame);
            log.info("Add new mao to DB");
            return true;
        }
        log.info("Map alredy exist");
        return false;
    }

    public boolean isExist(String name) {
        MapGame mapGame = mapRepository.findByNameAndUsedIsFalse(name);
        if(mapGame !=null){
            return false;
        }

        return true;
    }

    public boolean startGame(String mapName){
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        if(actualInformation.getActualMapName()!=null){
            log.info("Can not run map, map alredy runed :"+ actualInformation.getActualMapName());
            return false;
        }

        MapGame mapGame = mapRepository.findByNameAndUsedIsFalse(mapName);

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

    public void stopGame() {
        ActualInformation actualInformation = ActualInformation.getActualInformation();
        actualInformation.setActualMapName(null);
        actualInformation.setConcuretnHashMapGame(null);
    }
}
