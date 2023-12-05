package com.example.demo.Services;

import com.example.demo.game.Answer;
import com.example.demo.game.Game;
import com.example.demo.game.Parameter;
import com.example.demo.game.StartRound;
import com.example.demo.movie.LevelType;
import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class GameService {
    IMDbService service;
    private final Map<Integer, Game> games = new HashMap<>();
    private static int id = 0;

    public StartRound startGame(LevelType level) {
        Movie startMovie = service.getMovie(level);
        id++;
        Game game = new Game(id, level);
        games.put(id, game);
        return game.newRound(startMovie);
    }

    public ArrayList<ParameterType> getParameters(Integer id) {
        return games.get(id).getParameters();
    }

    public Parameter getParameter(Integer id, ParameterType type) {
        Game game = games.get(id);
        int score = game.getScore();
        int cost = 0;
        int size = game.getParameters().size();
        if (size != ParameterType.values().length) {
            int costParameter = 80;
            cost = costParameter / size * (game.getScoreStart() / 100);
        }
        if (score - cost < 0) {
            games.remove(id);
            return new Parameter(0, new String[0],false);
        }
        games.get(id).setScore(score - cost);
        return new Parameter(score - cost, games.get(id).getParameter(type), true);
    }

    public Answer setAnswer(Integer id, String answer) {
        Game game = games.get(id);
        int score = game.getScore();
        int cost = 0;
        switch (game.getLevel()) {
            case EASY -> cost = 30;
            case NORMAL -> cost = 15;
            case HARD -> cost = 10;
        }
        if (games.get(id).getMovieData().title().equals(answer)) {
            Movie startMovie = service.getMovie(game.getLevel());
            return new Answer( true, true, game.newRound(startMovie));
        }
        if (score - cost < 0) {
            games.remove(id);
            return new Answer(false, false, null);
        }
        games.get(id).setScore(score - cost);
        return new Answer(false, true, null);
    }

    public void roundEnd(Integer id) {
        games.remove(id);
    }
}
