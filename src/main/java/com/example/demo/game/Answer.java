package com.example.demo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Answer {
    private boolean right;
    private boolean alive;
    private StartRound round;
}
