package com.example.demo.movie;

public record Movie(
        int id,
        String title,
        String[] similarMovie,
        String[] genres,
        String[] keywords,
        String[] actors,
        String[] directors,
        String[] countries,
        String year,
        Float rating,
        String[] images) {

    public String[] info(ParameterType type) {
        switch (type) {
            case ACTOR -> {
                return this.actors;
            }
            case GENRE -> {
                return this.genres;
            }
            case DIRECTOR -> {
                return this.directors;
            }
            case KEYWORD -> {
                return this.keywords;
            }
            case IMAGES -> {
                return this.images;
            }
            case YEARS -> {
                return new String[] {this.year};
            }
            case RATING -> {
                return new String[] {this.rating.toString()};
            }
            case COUNTRIES -> {
                return this.countries;
            }
        }
        return null;
    }
}

