package com.example.demo.Services;

import com.example.demo.game.Answer;
import com.example.demo.game.Game;
import com.example.demo.game.Parameter;
import com.example.demo.game.StartRound;
import com.example.demo.movie.LevelType;
import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;

@Service
@AllArgsConstructor
@SessionScope
public class GameService {
    IMDbService service;
    private static Game game;

    public StartRound startGame(LevelType level) {
        Movie startMovie = service.getMovie(level);
        game = new Game(level);
        game.setMovieData(startMovie);
        return game.newRound(startMovie);
    }

    public ArrayList<ParameterType> getParameters() {
        return game.getParameters();
    }

    public Parameter getParameter(ParameterType type) {
        int score = game.getScore();
        int cost = 0;
        int size = game.getParameters().size();
        if (size != ParameterType.values().length) {
            int costParameter = 80;
            cost = costParameter / size * (game.getScoreStart() / 100);
        }
        if (score - cost < 0) {
            return new Parameter(0, new String[0],false);
        }
        game.setScore(score - cost);
        return new Parameter(score - cost, game.getParameter(type), true);
    }

    public Answer setAnswer(String answer) {
        int score = game.getScore();
        int cost = 0;
        switch (game.getLevel()) {
            case EASY -> cost = 30;
            case NORMAL -> cost = 15;
            case HARD -> cost = 10;
        }
        if (game.getMovieData().title().equals(answer)) {
            Movie startMovie = service.getMovie(game.getLevel());
            return new Answer( true, true, game.newRound(startMovie));
        }
        if (score - cost < 0) {
            return new Answer(false, false, null);
        }
        game.setScore(score - cost);
        return new Answer(false, true, null);
    }
}
