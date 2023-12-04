package com.example.demo.game;

import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@AllArgsConstructor
@Getter
@Setter
public class Game {
    private ArrayList<ParameterType> clueTypes;
    private Movie movieData;
    private int score;

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
}