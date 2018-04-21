package com.car.game.Map;

import com.car.game.Common.Model.Map;
import com.car.game.Map.Service.MapService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapController {

    @Autowired
    private MapService mapService;

    @GetMapping(value = "/all-maps")
    @ApiOperation("Get all maps Controller")
    public List<Map> getAllMaps(){
        return mapService.getAllMaps();
    }

    @PostMapping(value = "/add/{id}/{name}")
    @ApiOperation("Add map Controller")
    public void addNewMap(@PathVariable(value = "id") long id,
                   @PathVariable(value = "name") String name){
        mapService.addNewMap(new Map(id, name, "{1,0,1},{0,1,0},{0,1,0}"));
    }

}
