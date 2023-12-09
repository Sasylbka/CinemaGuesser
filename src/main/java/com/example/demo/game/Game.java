package com.example.demo.game;

import com.example.demo.movie.LevelType;
import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@AllArgsConstructor
@Getter
@Setter
public class Game {
    private int id;
    private ArrayList<ParameterType> clueTypes;
    private Movie movieData;
    private int score;
    private int scoreAll;
    private LevelType level;
    private int scoreStart;

    public Game(int id, LevelType level) {
        this.id = id;
        this.level = level;
        switch (level) {
            case EASY -> this.scoreStart = 100;
            case NORMAL -> this.scoreStart = 200;
            case HARD -> this.scoreStart = 400;
        }
    }

    public ArrayList<ParameterType> getParameters() {
        return clueTypes;
    }

    public String[] getParameter(ParameterType type) {
        if (clueTypes.indexOf(type) > 0) {
            clueTypes.remove(type);
            return movieData.info(type);
        }
        return new String[0];
    }

    public StartRound newRound(Movie startMovie) {
        this.scoreAll = this.scoreAll + this.score;
        this.score = this.scoreStart;
        ArrayList<String> listOfAnswers = new ArrayList<>(Arrays
                .stream(startMovie.similarMovie())
                .limit(4)
                .toList());
        listOfAnswers.add(startMovie.title());
        Collections.shuffle(listOfAnswers);

        this.clueTypes = new ArrayList<>(Arrays.asList(ParameterType.values()));

        return new StartRound(id, this.score, listOfAnswers);
    }
}