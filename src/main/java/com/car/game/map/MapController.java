package com.car.game.map;

import com.car.game.common.model.MapGame;
import com.car.game.map.service.MapServiceImpl;
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

    private MapServiceImpl mapService;

    @GetMapping(value = "/maps")
    @ApiOperation("Pobierz wszytskie mapy")
    public List<MapGame> getAllMaps(){
        return mapService.getAllMaps();
    }

    @PostMapping(value = "/map")
    @ApiOperation("Dodaj mape")
    public Boolean addNewMap(@RequestParam(value = "name") String name,
                             @RequestParam(value = "body") String body){
        return mapService.addNewMap(name,body);
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
