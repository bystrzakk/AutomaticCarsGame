package com.car.game.map;

import com.car.game.common.model.Map;
import com.car.game.map.service.MapService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MapController {

    private MapService mapService;

    @GetMapping(value = "/all-maps")
    @ApiOperation("Get all maps Controller")
    public List<Map> getAllMaps(){
        return mapService.getAllMaps();
    }

    @PostMapping(value = "/add/{id}/{name}")
    @ApiOperation("Add map Controller")
    public Boolean addNewMap(@PathVariable(value = "id") long id,
                   @PathVariable(value = "name") String name){
        boolean isExistMap = mapService.isExist(name);
        if(isExistMap){
            mapService.addNewMap(new Map(id, name, "{1,0,1},{0,1,0},{0,1,0}",false));
            return true;
        }
        return false;
    }

}
