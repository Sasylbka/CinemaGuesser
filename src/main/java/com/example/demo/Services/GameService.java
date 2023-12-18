package com.example.demo.Services;

import com.example.demo.game.Answer;
import com.example.demo.game.Game;
import com.example.demo.game.Parameter;
import com.example.demo.game.StartRound;
import com.example.demo.movie.LevelType;
import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import info.movito.themoviedbapi.tools.MovieDbException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class GameService {
    IMDbService service;
    private final Map<Integer, Game> games = new HashMap<>();
    private static int id = 0;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public StartRound startGame(LevelType level) {
        try {
            Movie startMovie = service.getMovie(level);
            id++;
            Game game = new Game(id, level);
            StartRound temp = game.newRound(startMovie);
            while (startMovie.similarMovie().length<4 || isRussianOrEnglish(startMovie.title()) || temp.getListOfAnswers().size()<=4){
                startMovie=service.getMovie(level);
                temp = game.newRound(startMovie);
            }
            game.setMovieData(startMovie);
            games.put(id, game);
            logger.info("New game was started (id: " + id + ", movie: " + startMovie.title() + ")");
            return temp;
        }
        catch (MovieDbException e){
            throw new MovieDbException("Ошибка получения данных о фильме, попробуйте ещё раз");
        }
    }
    private static boolean isRussianOrEnglish(String word) {
        return word.matches("[а-яА-Яa-zA-Z0-9]+");
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
        logger.info("A hint was requested (id: " + id + ", hint type: " + type.name() + ", cost: " + cost + ", level: " + game.getLevel() + ")");
        if (score - cost < 0) {
            logger.info("Game was lost (id: " + id + ")");
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
            return new Answer( true, true, game.newRound(startMovie),score);
        }
        logger.info("Incorrect answer was received (id: " + id + ", answer: " + answer + ")");
        if (score - cost < 0) {
            logger.info("Game was lost (id: " + id + ")");
            games.remove(id);
            return new Answer(false, false, null,0);
        }
        games.get(id).setScore(score - cost);
        return new Answer(false, true, null,score - cost);
    }

    public void gameEnd(Integer id) {
        logger.info("Game was ended (id: " + id + ", points: " + games.get(id).getScoreAll() + ")");
        games.remove(id);
    }
}
