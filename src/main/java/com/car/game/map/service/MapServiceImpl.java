package com.car.game.map.service;

import com.car.game.common.model.Map;
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
    public List<Map> getAllMaps() {
        return mapRepository.findAll();
    }

    @Override
    public void addNewMap(Map map) {
        mapRepository.save(map);
        log.info("New map was added to DB");
    }

    @Override
    public boolean isExist(String name) {
        Map map = mapRepository.findByNameAndUsedIsFalse(name);
        if(map!=null){
            return false;
        }

        return true;
    }

    public boolean startGame(String mapName){
        ActualInformation actualInformation = ActualInformation.getGetActulaInformation();
        if(actualInformation.getActualMapName()!=null){
            log.info("nie mozna uruchomic nowej gry, obecnie jest aktywna mapa :"+ actualInformation.getActualMapName());
            return false;
        }

        Map map = mapRepository.findByNameAndUsedIsFalse(mapName);

        if ( map ==null){
            log.info("bark mapy");
            return false;
        }

        actualInformation.setActualMapName(mapName,map);
        System.out.println(ActualInformation.getMapGame());
        map.setUsed(true);
        mapRepository.save(map);
        return true;
    }

    @Override
    public void stopGame() {
        ActualInformation actualInformation = ActualInformation.getGetActulaInformation();
        actualInformation.setActualMapName(null);
        actualInformation.setMapGame(null);
    }
}
