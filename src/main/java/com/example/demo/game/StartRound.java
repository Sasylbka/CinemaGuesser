package com.example.demo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
public class StartRound {
    private int id;
    private int score;
    private ArrayList<String> listOfAnswers;
}