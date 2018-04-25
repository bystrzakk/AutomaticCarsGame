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
    public List<MapGame> getAllMaps() {
        return mapService.getAllMaps();
    }

    @PostMapping(value = "/map")
    @ApiOperation("Add map")
    @ResponseStatus(CREATED)
    public Boolean addNewMap(@RequestParam(value = "name") String name, @RequestParam(value = "body") String body) {
        return mapService.addNewMap(name,body);
    }

    @PostMapping(value = "/select")
    @ApiOperation("Select the map")
    @ResponseStatus(OK)
    public Boolean selectMap(@RequestParam(value = "name") String name) {
        return mapService.selectMap(name);
    }

    @PostMapping(value = "/unselect")
    @ApiOperation("Unselect the game")
    @ResponseStatus(OK)
    public void unselectMap(@RequestParam(value = "name") String name){
         mapService.unselectMap(name);
    }

    @DeleteMapping(value = "/map")
    @ApiOperation("Delete map")
    @ResponseStatus(NO_CONTENT)
    public Boolean deleteMap(@RequestParam(value = "name") String name) {
        return mapService.deleteMap(name);
    }
}
