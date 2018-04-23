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
    public String addNewMap(@RequestParam(value = "name") String name,
                             @RequestParam(value = "body") String body
                             ){
        boolean isExistMap = mapService.isExist(name);
        if(isExistMap){
            MapGame mapGame = new MapGame();
            mapGame.setName(name);
            mapGame.setMapBody("1,0,1,0,1,0,0,1,0");
            mapGame.setUsed(false);
            mapService.addNewMap(mapGame);
            return "Success";
        }
        return "Map already exist";
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

}
