package com.example.demo.Controllers;

import com.example.demo.Services.GameService;
import com.example.demo.game.*;
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
    public StartRound gameStart(LevelType level){
        return service.startGame(level);
    }

    @GetMapping(path="/getAvailableParameters")
    public ArrayList<ParameterType> getAvailableParameters(){
        return service.getParameters();
    }

    @GetMapping(path="/getParameter")
    public Parameter getParameter(ParameterType type){
        return service.getParameter(type);
    }

    @GetMapping(path="/setAnswer")
    public Answer setAnswer(String answer){
        return service.setAnswer(answer);
    }


    @ExceptionHandler(InvalidParameterException.class)
    public String handleException(InvalidParameterException e) {
        return e.getMessage();
    }

}
