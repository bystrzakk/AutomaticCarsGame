package com.car.game.map;

import com.car.game.common.model.MapGame;
import com.car.game.map.service.MapService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MapController {

    private MapService mapService;

    @GetMapping(value = "/all-maps")
    @ApiOperation("Get all maps Controller")
    public List<MapGame> getAllMaps(){
        return mapService.getAllMaps();
    }

    @PostMapping(value = "/map")
    @ApiOperation("Add map Controller")
    public String addNewMap(@RequestParam(value = "name") String name, @RequestParam(value = "body") String body){
        return mapService.addNewMap(name, body);
    }

    @PostMapping(value = "/start")
    @ApiOperation("Start Game  Controller")
    public Boolean startGame(@RequestParam(value = "name") String name){
        return mapService.startGame(name);

    }

    @PostMapping(value = "/stop")
    @ApiOperation("Start Game  Controller")
    public void stopGame(){
         mapService.stopGame();
    }

    @PostMapping(value = "/map/delete")
    @ApiOperation("Delete map Controller")
    public Boolean deleteMap(@RequestParam(value = "name") String name){
        return mapService.deleteMap(name);
    }
}
