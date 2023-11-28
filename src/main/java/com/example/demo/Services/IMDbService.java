package com.example.demo.Services;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.ProductionCountry;
import info.movito.themoviedbapi.model.keywords.Keyword;
import info.movito.themoviedbapi.model.people.Person;
import org.springframework.stereotype.Service;

import com.example.demo.movie.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IMDbService {

    private static final String TOKEN = "bf091303eeaba7e2778d6e0f8296a4c8";
    private static final String PICTURE_BASE_URL = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2";
    private static final String MEDIUM_PICTURE_BASE_URL = "https://www.themoviedb.org/t/p/w300_and_h450_bestv2";
    private static final String SMALL_PICTURE_BASE_URL = "https://www.themoviedb.org/t/p/w150_and_h225_bestv2";
    private static final String DEPARTMENT_ACTOR = "Acting";
    private static final String DEPARTMENT_DIRECTOR = "Directing";
    private static final String EN_US = "ru";

    private static final int easyTopMovie = 500;
    private static final int normalTopMovie = 2500;
    private static final int hardTopMovie = 10000;
    private static final int easyCountSimilarMovie = 6;
    private static final int normalCountSimilarMovie = 15;
    private static final int hardCountSimilarMovie = 20;

    private static final int perPage = 20;

    private final TmdbApi client;

    public IMDbService() {
        this.client = new TmdbApi(TOKEN);
    }

    public Movie getMovie(LevelType type) {
        return switch (type) {
            case EASY -> movie(easyTopMovie, easyCountSimilarMovie);
            case NORMAL -> movie(normalTopMovie, normalCountSimilarMovie);
            case HARD -> movie(hardTopMovie, hardCountSimilarMovie);
        };
    }

    private Movie movie(int topMovie, int countSimilarMovie) {
        int top = (int)(Math.random()*(topMovie+1));
        int page = top / perPage;
        int count = top % perPage;
        MovieDb randomMovie = client.getMovies().getPopularMovies(EN_US, page).getResults().get(count);
        int movieId = randomMovie.getId();
        MovieDb movie = client.getMovies().getMovie(movieId, EN_US, TmdbMovies.MovieMethod.credits, TmdbMovies.MovieMethod.similar, TmdbMovies.MovieMethod.recommendations, TmdbMovies.MovieMethod.keywords, TmdbMovies.MovieMethod.top_rated, TmdbMovies.MovieMethod.images);
        String title = movie.getTitle();
        String[] similarMovie = getSimilarMovie(movie);
        String[] genres = getGenres(movie);
        String[] keywords = getKeywords(movie);
        String[] actors = getActors(movie);
        String[] directors = getDirectors(movie);
        String[] countries = getCountries(movie);
        String year = movie.getReleaseDate();
        Float rating = movie.getUserRating();
        String[] images = getImages(movie, countSimilarMovie);
        return new Movie(movieId, title, similarMovie, genres, keywords, actors, directors, countries, year, rating, images);
    }

    private String[] getImages(MovieDb movie, int countSimilarMovie) {
        List<String> imagesMovie = new ArrayList<>();
        var images = movie.getImages();
        for (Artwork image : images) {
            var url = image.getFilePath();
            if (url != null && !url.isEmpty() && countSimilarMovie > 0) {
                imagesMovie.add(PICTURE_BASE_URL + url);
                countSimilarMovie--;
            }
        }
        return imagesMovie.toArray(new String[0]);
    }

    private String[] getSimilarMovie(MovieDb movie) {
        List<String> similarMovie = new ArrayList<>();
        var similars = movie.getSimilarMovies();
        for (MovieDb similar : similars) {
            var name = similar.getTitle();
            if (name != null && !name.isEmpty()) {
                similarMovie.add(name);
            }
        }
        return similarMovie.toArray(new String[0]);
    }

    private String[] getCountries(MovieDb movie) {
        List<String> countriesMovie = new ArrayList<>();
        var countries = movie.getProductionCountries();
        for (ProductionCountry country : countries) {
            var name = country.getName();
            if (name != null && !name.isEmpty()) {
                countriesMovie.add(name);
            }
        }
        return countriesMovie.toArray(new String[0]);
    }

    private String[] getGenres(MovieDb movie) {
        List<String> genresName = new ArrayList<>();
        var genres = movie.getGenres();
        for (Genre genre : genres) {
            var name = genre.getName();
            if (name != null && !name.isEmpty()) {
                genresName.add(name);
            }
        }
        return genresName.toArray(new String[0]);
    }

    private String[] getActors(MovieDb movie) {
        List<String> names = new ArrayList<>();
        var persons = movie.getCast();
        for (Person person : persons) {
            var personInfo = client.getPeople().getPersonInfo(person.getId());
            if (DEPARTMENT_ACTOR.equals(personInfo.getKnownForDepartment())) {
                var name = person.getName();
                if (name != null && !name.isEmpty()) {
                    names.add(name);
                }
            }
        }
        return names.toArray(new String[0]);
    }


    private String[] getDirectors(MovieDb movie) {
        List<String> names = new ArrayList<>();
        var persons = movie.getCast();
        for (Person person : persons) {
            var personInfo = client.getPeople().getPersonInfo(person.getId());
            if (DEPARTMENT_DIRECTOR.equals(personInfo.getKnownForDepartment())) {
                var name = person.getName();
                if (name != null && !name.isEmpty()) {
                    names.add(name);
                }
            }
        }
        return names.toArray(new String[0]);
    }

    private String[] getKeywords(MovieDb movie) {
        List<String> res = new ArrayList<>();
        var keywords = movie.getKeywords();
        for (Keyword keyword : keywords) {
            var name = keyword.getName();
            if (name != null && !name.isEmpty()) {
                res.add(name);
            }
        }
        return res.toArray(new String[0]);
    }
}