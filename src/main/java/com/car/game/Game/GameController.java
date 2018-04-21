package com.car.game.Game;

import com.car.game.Game.Service.GameService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(value = "/start")
    @ApiOperation("Add map Controller")
    public Boolean startGame(@PathVariable(value = "name") String name){

        //mapService.addNewMap(new Map(id, name, "{1,0,1},{0,1,0},{0,1,0}"));
        return true;
    }
}
