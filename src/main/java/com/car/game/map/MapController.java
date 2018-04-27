package com.car.game.map;

import com.car.exception.IncorrectFormatMapException;
import com.car.exception.NoMapException;
import com.car.exception.SelectMapException;
import com.car.game.common.model.MapGame;
import com.car.game.map.dto.MapRequestDto;
import com.car.game.map.service.MapService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> addNewMap(@RequestBody MapRequestDto mapRequestDto) {
        mapService.addNewMap(mapRequestDto);
        return new ResponseEntity<>("Map is added", HttpStatus.CREATED);
    }

    @PostMapping(value = "/selected-map")
    @ApiOperation("Select the map")
    public ResponseEntity<String> selectMap(@RequestParam(value = "name") String name) {
        final boolean isMapSelected = mapService.selectMap(name);
        if(isMapSelected == false){
            throw new SelectMapException("Problem with selecting map");
        }
        return new ResponseEntity("Map is selected", HttpStatus.OK);
    }

    @PostMapping(value = "/unselected-map")
    @ApiOperation("Unselect the game")
    public ResponseEntity<String> unselectMap(@RequestParam(value = "name") String name){
        mapService.unselectMap(name);
        return new ResponseEntity("Map is unselected", HttpStatus.OK);
    }

    @DeleteMapping(value = "/map")
    @ApiOperation("Delete map")
    public ResponseEntity<String> deleteMap(@RequestParam(value = "name") String name) {
        final boolean isMapDeleted = mapService.deleteMap(name);
        if(isMapDeleted == false){
            throw new NoMapException("Delete map exception");
        }
        return new ResponseEntity("Map is deleted",HttpStatus.NO_CONTENT);
    }
}
