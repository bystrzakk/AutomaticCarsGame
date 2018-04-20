package com.car.game.Service;

import com.car.game.Model.Map;
import com.car.game.Repository.MapRepository;
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
}
