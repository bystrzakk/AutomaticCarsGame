package com.car.game.game.service;

import com.car.game.common.model.Map;
import com.car.game.common.repository.MapRepository;
import com.car.game.map.ActualInformation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log
@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GameService {

    private MapRepository mapRepository;

    public Boolean startGame(String mapName){
        ActualInformation actualInformation = ActualInformation.getGetActulaInformation();
        if(actualInformation.getActualMapName()!=null){
            log.info("nie mozna uruchomic nowej gry, obecnie jest aktywna mapa :"+ actualInformation.getActualMapName());
            return false;
        }

        Map map = mapRepository.findByNameAndAndUsedIsFalse(mapName);

        if ( map ==null){
            log.info("bark mapy");
            return false;
        }

        actualInformation.setActualMapName(mapName);
        map.setUsed(true);
        mapRepository.save(map);
        return true;
    }
}
