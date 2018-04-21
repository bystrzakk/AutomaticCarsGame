package com.car.game.game;

import com.car.game.game.service.GameService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GameController {

    private GameService gameService;

    @PostMapping(value = "/start")
    @ApiOperation("Add map Controller")
    public Boolean startGame(@PathVariable(value = "name") @NotNull String name){
        return gameService.startGame(name);
    }
}
