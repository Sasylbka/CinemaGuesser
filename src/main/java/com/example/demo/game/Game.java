package com.example.demo.game;

import com.example.demo.movie.LevelType;
import com.example.demo.movie.Movie;
import com.example.demo.movie.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.InvalidParameterException;
import java.text.Collator;
import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@Setter
public class Game {
    private int id;
    private ArrayList<ParameterType> clueTypes;
    private Movie movieData;
    private int score=0;
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
        if (clueTypes.contains(type)) {
            clueTypes.remove(type);
            return movieData.info(type);
        }
        else {throw new InvalidParameterException("Такая подсказка уже была дана.");
        }
    }

    public StartRound newRound(Movie startMovie) {
        this.scoreAll = this.scoreAll + this.score;
        this.score = this.scoreStart;
        Comparator<String> customComparator = (s1, s2) -> {
            Locale russianLocale = new Locale("ru", "RU");
            Collator collator = Collator.getInstance(russianLocale);

            // Compare strings using locale-sensitive collation
            return collator.compare(s1, s2);
        };
        List<String> listOfAnswers = new ArrayList<>(Arrays
                .stream(startMovie.similarMovie())
                .toList());
        listOfAnswers=filterRussianAndEnglishStrings(listOfAnswers);
        listOfAnswers.sort(customComparator);
        Stream <String> stream = listOfAnswers.stream();
        ArrayList<String> temp = new ArrayList<>(stream.limit(4).toList());
        temp.add(startMovie.title());
        Collections.shuffle(temp);

        this.clueTypes = new ArrayList<>(Arrays.asList(ParameterType.values()));

        return new StartRound(id, this.score, temp);
    }
    private static List<String> filterRussianAndEnglishStrings(List<String> inputList) {
        List<String> filteredList = new ArrayList<>();
        for (String word : inputList) {
            if (isRussianOrEnglish(word)) {
                filteredList.add(word);
            }
        }
        return filteredList;
    }
    private static boolean isRussianOrEnglish(String word) {
        return word.matches("[а-яА-Яa-zA-Z0-9]+");
    }
}