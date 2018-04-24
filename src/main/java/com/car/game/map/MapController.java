package com.car.game.map;

import com.car.game.common.model.MapGame;
import com.car.game.map.service.MapService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

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
    @ResponseStatus(CREATED)
    public boolean addNewMap(@RequestParam(value = "name") String name,
                             @RequestParam(value = "body") String body){
        return mapService.addNewMap(name,body);
    }

    @PostMapping(value = "/start")
    @ApiOperation("Start game ")
    @ResponseStatus(OK)
    public Boolean startGame(@RequestParam(value = "name") String name){
        return mapService.startGame(name);

    }

    @PostMapping(value = "/stop")
    @ApiOperation("Stop game")
    @ResponseStatus(OK)
    public void stopGame(){
         mapService.stopGame();
    }

    @PostMapping(value = "/map/delete")
    @ApiOperation("Delete map Controller")
    @ResponseStatus(NO_CONTENT)
    public Boolean deleteMap(@RequestParam(value = "name") String name){
        return mapService.deleteMap(name);
    }
}
