package com.car.game.Service;

import com.car.game.Model.Map;
import com.car.game.Repository.MapRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.util.List;

@Log
@Service
public class MapServiceImpl implements MapService {

    private MapRepository mapRepository;

    public MapServiceImpl(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @Override
    public List<Map> getAllMaps() {
        return mapRepository.findAll();
    }

    @Override
    public void addNewMap(Map map) {
        mapRepository.save(map);
        log.info("New map was added to DB");
    }
}
