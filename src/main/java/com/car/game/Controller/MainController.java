package com.car.game.Controller;

import com.car.game.Model.Map;
import com.car.game.Service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private MapService mapService;

    @GetMapping(value = "/all-maps")
    List<Map> getAllMaps(){
        return mapService.getAllMaps();
    }

    @PostMapping(value = "/add/{id}/{name}")
    void addNewMap(@PathVariable(value = "id") long id, @PathVariable(value = "name") String name){
        mapService.addNewMap(new Map(id, name, "{1,0,1},{0,1,0}"));
    }

}
