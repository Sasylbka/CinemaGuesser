package com.example.demo.Controllers;

import com.example.demo.Services.GameService;
import com.example.demo.game.Game;
import com.example.demo.movie.LevelType;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/game")
public class GameController {
    GameService service;
    @GetMapping(path="/start")
    public Game gameStart(LevelType level, ParameterType type){
        return service.startGame(level,type);
    }
    @ExceptionHandler(InvalidParameterException.class)
    public String handleException(InvalidParameterException e) {
        return e.getMessage();
    }

}
