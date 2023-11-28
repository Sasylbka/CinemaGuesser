package com.example.demo.game;

import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@AllArgsConstructor
@Getter
@Setter
public class Game {
    private String startClue;
    private ArrayList<ParameterType> clueTypes;
    private Movie movieData;
    private int score;
    private ArrayList<String> listOfAnswers;
}
