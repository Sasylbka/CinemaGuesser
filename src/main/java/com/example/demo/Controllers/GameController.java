package com.example.demo.Controllers;

import com.example.demo.Services.GameService;
import com.example.demo.game.StartGame;
import com.example.demo.movie.LevelType;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;
import java.util.ArrayList;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/game")
public class GameController {
    GameService service;
    @GetMapping(path="/start")
    public StartGame gameStart(LevelType level, ParameterType type){
        return service.startGame(level,type);
    }

    @GetMapping(path="/getAvailableParameters")
    public ArrayList<ParameterType> getAvailableParameters(Integer id){
        return service.getParameters(id);
    }

    @GetMapping(path="/getParameter")
    public String[] getParameter(Integer id, ParameterType type){
        return service.getParameter(id, type);
    }

    @GetMapping(path="/setAnswer")
    public Integer setAnswer(Integer id, String answer){
        return service.setAnswer(id, answer);
    }

    @GetMapping(path="/roundEnd")
    public void roundEnd(Integer id){
        service.roundEnd(id);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public String handleException(InvalidParameterException e) {
        return e.getMessage();
    }

}
