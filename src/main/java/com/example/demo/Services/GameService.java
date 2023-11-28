package com.example.demo.Services;

import com.example.demo.game.Game;
import com.example.demo.movie.LevelType;
import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GameService {
    IMDbService service;
    public Game startGame(LevelType level,ParameterType type){
        Movie startMovie = service.getMovie(level);
        int startScore=0;
        switch (level){
            case EASY -> startScore=100;
            case NORMAL -> startScore=200;
            case HARD -> startScore=300;
        }
        ArrayList<String> listOfAnswers=new ArrayList<>();
        while (listOfAnswers.size()<3){
            int i = (int) (Math.random()*startMovie.similarMovie().length);
            if(!listOfAnswers.contains(startMovie.similarMovie()[i]))
                listOfAnswers.add(startMovie.similarMovie()[i]);
        }
        String startClue=startMovie.info(type);
        ArrayList<ParameterType> parameterTypes=new ArrayList<>();
        parameterTypes.add(ParameterType.GENRE);
        parameterTypes.add(ParameterType.YEARS);
        parameterTypes.add(ParameterType.ACTOR);
        parameterTypes.add(ParameterType.DIRECTOR);
        parameterTypes.add(ParameterType.RATING);
        parameterTypes.add(ParameterType.COUNTRIES);
        parameterTypes.add(ParameterType.IMAGES);
        parameterTypes.add(ParameterType.KEYWORD);
        parameterTypes.remove(type);
        return new Game(startClue,parameterTypes,startMovie,startScore,listOfAnswers);
    }
}
