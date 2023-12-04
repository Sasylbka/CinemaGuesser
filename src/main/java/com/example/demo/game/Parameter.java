package com.example.demo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Parameter {
    private int score;
    private String[] parameter;
    private boolean alive;
}
