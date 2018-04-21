package com.car.game.Map.Service;

import com.car.game.Common.Model.Map;
import com.car.game.Common.Repository.MapRepository;
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
