package com.example.demo.Controllers;

import com.example.demo.Services.GameService;
import com.example.demo.game.*;
import com.example.demo.movie.LevelType;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ArrayList<ParameterType> getAvailableParameters(Integer id){
        return service.getParameters(id);
    }

    @GetMapping(path="/getParameter")
    public Parameter getParameter(Integer id, ParameterType type){
        return service.getParameter(id, type);
    }

    @GetMapping(path="/setAnswer")
    public Answer setAnswer(Integer id, String answer){
        return service.setAnswer(id, answer);
    }

    @GetMapping(path="/gameEnd")
    public ResponseEntity<?> roundEnd(Integer id){
        service.gameEnd(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleException(InvalidParameterException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
