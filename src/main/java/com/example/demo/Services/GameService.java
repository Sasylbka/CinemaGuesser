package com.example.demo.Services;

import com.example.demo.game.Answer;
import com.example.demo.game.Game;
import com.example.demo.game.Parameter;
import com.example.demo.game.StartGame;
import com.example.demo.movie.LevelType;
import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class GameService {
    IMDbService service;
    Map<Integer, Game> games;
    private int id = 0;
    private int mistake = 10;

    public StartGame startGame(LevelType level, ParameterType type) {
        Movie startMovie = service.getMovie(level);
        int startScore = 0;
        switch (level) {
            case EASY -> startScore = 100;
            case NORMAL -> startScore = 200;
            case HARD -> startScore = 300;
        }
        ArrayList<String> listOfAnswers = new ArrayList<>(Arrays.stream(startMovie.similarMovie()).toList());
        listOfAnswers.add(startMovie.title());
        Collections.shuffle(listOfAnswers);

        String[] startClue = startMovie.info(type);
        ArrayList<ParameterType> parameterTypes = new ArrayList<>();
        parameterTypes.add(ParameterType.GENRE);
        parameterTypes.add(ParameterType.YEARS);
        parameterTypes.add(ParameterType.ACTOR);
        parameterTypes.add(ParameterType.DIRECTOR);
        parameterTypes.add(ParameterType.RATING);
        parameterTypes.add(ParameterType.COUNTRIES);
        parameterTypes.add(ParameterType.IMAGES);
        parameterTypes.add(ParameterType.KEYWORD);
        parameterTypes.remove(type);
        id++;
        games.put(id, new Game(parameterTypes, startMovie, startScore, level));
        return new StartGame(id, startClue, startScore, listOfAnswers);
    }

    public ArrayList<ParameterType> getParameters(Integer id) {
        return games.get(id).getParameters();
    }

    public Parameter getParameter(Integer id, ParameterType type) {
        int score = games.get(id).getScore();
        if (score - mistake < 0) {
            games.remove(id);
            return new Parameter(0, new String[0],false);
        }
        games.get(id).setScore(score - mistake);
        return new Parameter(score - mistake, games.get(id).getParameter(type), true);
    }

    public Answer setAnswer(Integer id, String answer) {
        int score = games.get(id).getScore();
        if (games.get(id).getMovieData().title().equals(answer)) {
            return new Answer(score, true, true);
        }
        if (score - mistake < 0) {
            games.remove(id);
            return new Answer(0, false, false);
        }
        games.get(id).setScore(score - mistake);
        return new Answer(score - mistake, false, true);
    }

    public void roundEnd(Integer id) {
        games.remove(id);
    }
}
