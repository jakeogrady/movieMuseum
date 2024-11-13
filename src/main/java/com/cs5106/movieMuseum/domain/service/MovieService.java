package com.cs5106.movieMuseum.domain.service;

import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Iterable<Movie> getMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieByTitle(String title) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        return movieOpt.get();
    }

    public List<Movie> getMoviesByReleaseYear(int releaseYear) {
        List<Movie> movies = movieRepository.findByReleaseYear(releaseYear);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with release year %d not found", releaseYear));
        }
        return movies;
    }

    public List<Movie> getMoviesByImdbRating(double imdbRating) {
        List<Movie> movies = movieRepository.findByImdbRating(imdbRating);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with imdb rating %f not found", imdbRating));
        }
        return movies;
    }

    public List<Movie> getMoviesByImdbRatingBetween(double low, double high) {
        List<Movie> movies = movieRepository.findByImdbRatingBetween(low, high);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with imdb rating between %f and %f not found", low, high));
        }
        return movies;
    }

    public List<Movie> getMoviesByTitleSubstring(String title) {
        List<Movie> movies = movieRepository.findByTitleSubstring(title);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with title containing %s not found", title));
        }
        return movies;
    }

    public Movie addMovie(Movie movie) {
        if (movieRepository.findDistinctByTitle(movie.getTitle()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Movie %s already exists", movie.getTitle()));
        }
        return movieRepository.save(movie);
    }

    public void updateMovies(List<Movie> newMovieData) {
        for (Movie newMovieDatum : newMovieData) {
            Optional<Movie> movieOpt = movieRepository.findById(newMovieDatum.getId());
            if (movieOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with id %d not found", newMovieDatum.getId()));
            }
            System.out.println("Updating movie with id " + newMovieDatum.getId());
            Movie movie = movieOpt.get();
            movie.setTitle(newMovieDatum.getTitle());
            movie.setImdbRating(newMovieDatum.getImdbRating());
            movie.setReleaseYear(newMovieDatum.getReleaseYear());
            movieRepository.save(movie);
        }
    }

    public void deleteMovies(long[] movieIds) {
        for (long movieId : movieIds) {
            Optional<Movie> movieOpt = movieRepository.findById(movieId);
            if (movieOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with id %d not found", movieId));
            }
            System.out.println("Deleting movie with id " + movieId);
            movieRepository.delete(movieOpt.get());
        }
    }
}