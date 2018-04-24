package com.car.game.map;

import com.car.game.common.model.MapGame;
import com.car.game.map.service.MapService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MapController {

    private MapService mapService;

    @GetMapping(value = "/maps")
    @ApiOperation("Get all maps")
    public List<MapGame> getAllMaps(){
        return mapService.getAllMaps();
    }

    @PostMapping(value = "/map")
    @ApiOperation("Add map")
    public Boolean addNewMap(@RequestParam(value = "name") String name,
                             @RequestParam(value = "body") @ApiParam(value = "Map body in CSV format") String body){
        return mapService.addNewMap(name,body);
    }

    @PostMapping(value = "/start")
    @ApiOperation("Start game ")
    public Boolean startGame(@RequestParam(value = "name") String name){
        return mapService.startGame(name);
    }

    @PostMapping(value = "/stop")
    @ApiOperation("Stop game")
    public void stopGame(){
         mapService.stopGame();
    }

    @PostMapping(value = "/map/delete")
    @ApiOperation("Delete map Controller")
    public Boolean deleteMap(@RequestParam(value = "name") String name){
        return mapService.deleteMap(name);
    }
}
