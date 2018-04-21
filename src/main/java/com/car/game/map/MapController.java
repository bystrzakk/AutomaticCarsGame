package com.car.game.map;

import com.car.game.common.model.Map;
import com.car.game.game.ActualInformation;
import com.car.game.map.service.MapService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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
            Map map = new Map();
            map.setName(name);
            map.setMapBody("1,0,1;0,1,0;0,1,0");
            map.setUsed(false);
            mapService.addNewMap(map);
            return true;
        }
        return false;
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
