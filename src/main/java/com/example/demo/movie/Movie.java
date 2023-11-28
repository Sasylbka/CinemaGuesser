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

    public String info(ParameterType type) {
        switch (type) {
            case ACTOR -> {
                return this.actors[(int) (Math.random() * (this.actors.length + 1))];
            }
            case GENRE -> {
                return this.genres[(int) (Math.random() * (this.genres.length + 1))];
            }
            case DIRECTOR -> {
                return this.directors[(int) (Math.random() * (this.directors.length + 1))];
            }
            case KEYWORD -> {
                return this.keywords[(int) (Math.random() * (this.keywords.length + 1))];
            }
            case IMAGES -> {
                return this.images[(int) (Math.random() * (this.images.length + 1))];
            }
            case YEARS -> {
                return this.year;
            }
            case RATING -> {
                return this.rating.toString();
            }
            case COUNTRIES -> {
                return this.countries[(int) (Math.random() * (this.countries.length + 1))];
            }
        }
        return null;
    }
}

