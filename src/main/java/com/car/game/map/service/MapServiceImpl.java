package com.car.game.map.service;

import com.car.game.common.model.Map;
import com.car.game.common.repository.MapRepository;
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
        Map map = mapRepository.findByNameAndAndUsedIsFalse(name);
        if(map!=null){
            return false;
        }

        return true;
    }
}
