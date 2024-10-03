package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Actor;
import com.cs5106.movieMuseum.domain.entity.Genre;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.ActorRepository;
import com.cs5106.movieMuseum.domain.repository.DirectorRepository;
import com.cs5106.movieMuseum.domain.repository.GenreRepository;
import com.cs5106.movieMuseum.domain.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

@RestController
public class MovieController {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;

    public MovieController(MovieRepository movieRepository, ActorRepository actorRepository, GenreRepository genreRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
        this.directorRepository = directorRepository;
    }

    @GetMapping("/movies")
    public Iterable<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/movie/title/{title}")
    public Movie getMovieByTitle(@PathVariable String title) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        return movieOpt.get();
    }

    @GetMapping("/movies/releaseYear/{releaseYear}")
    public List<Movie> getMovieByReleaseYear(@PathVariable int releaseYear) {
        List<Movie> movies = movieRepository.findByReleaseYear(releaseYear);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with release year %d not found", releaseYear));
        }
        return movies;
    }

    @GetMapping("/movies/ImdbRating/{imdbRating}")
    public List<Movie> getMovieByImdbRating(@PathVariable double imdbRating) {
        List<Movie> movies = movieRepository.findByImdbRating(imdbRating);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with imdb rating %f not found", imdbRating));
        }
        return movies;
    }

    @GetMapping("/movies/title/substring/{title}")
    public List<Movie> getMovieByTitleSubstring(@PathVariable String title) {
        List<Movie> movies = movieRepository.findByTitleSubstring(title);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with title substring %s not found", title));
        }
        return movies;
    }

    @GetMapping("movies/imdbRating/between/{low}/{high}")
    public List<Movie> getMovieByImdbRatingBetween(@PathVariable double low, @PathVariable double high) {
        List<Movie> movies = movieRepository.findByImdbRatingBetween(low, high);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with imdb rating between %f and %f not found", low, high));
        }
        return movies;
    }

    @GetMapping("/movie/{title}/actors")
    public Set<Actor> getActorsByMovie(@PathVariable String title) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        return movieOpt.get().getActors();
    }

    @PostMapping("/movie")
    public Movie addMovie(@RequestBody Movie movie) {
        if (movieRepository.findDistinctByTitle(movie.getTitle()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Movie %s already exists", movie.getTitle()));
        }
        return movieRepository.save(movie);
    }

    @PutMapping("/movie/{title}")
    public void updateMovie(@PathVariable String title, @RequestBody Movie movie) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }

        if (movie.getTitle().equals(title) ) {
            movie.setId(movieOpt.get().getId());
            movieRepository.save(movie);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Movie %s does not match the path", title));
        }
    }

    @PutMapping("/movie/{title}/addActor/{firstName}/{lastName}")
    public void addActorToMovie(@PathVariable String title, @PathVariable String firstName, @PathVariable String lastName) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        Optional<Actor> actorOpt = actorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (actorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s %s not found", firstName, lastName));
        }

        movieOpt.get().addActor(actorOpt.get());
        actorOpt.get().addMovie(movieOpt.get());
        movieRepository.save(movieOpt.get());
        actorRepository.save(actorOpt.get());
    }

//    @PutMapping("/movie/{title}/addGenre/{genre}")
//    public void addGenreToMovie(@PathVariable String title, @PathVariable String genre) {
//        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
//        if (movieOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
//        }
//        Optional<Genre> genreOpt = genreRepository.findDistinctByGenre(genre);
//        if (genreOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genre));
//        }
//
//        movieOpt.get().addGenre(genreOpt.get());
//        genreOpt.get().addMovie(movieOpt.get());
//        movieRepository.save(movieOpt.get());
//        genreRepository.save(genreOpt.get());
//    }
//
//    @PutMapping("/movie/{title}/addDirector/{firstName}/{lastName}")
//    public void addDirectorToMovie(@PathVariable String title, @PathVariable String firstName, @PathVariable String lastName) {
//        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
//        if (movieOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
//        }
//        Optional<Actor> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
//        if (directorOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
//        }
//
//        movieOpt.get().addDirector(directorOpt.get());
//        directorOpt.get().addMovie(movieOpt.get());
//        movieRepository.save(movieOpt.get());
//        actorRepository.save(directorOpt.get());
//    }

    @PutMapping("/movie/{title}/removeActor/{firstName}/{lastName}")
    public void removeActorFromMovie(@PathVariable String title, @PathVariable String firstName, @PathVariable String lastName) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        Optional<Actor> actorOpt = actorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (actorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s %s not found", firstName, lastName));
        }

        movieOpt.get().removeActor(actorOpt.get());
        actorOpt.get().removeMovie(movieOpt.get());
        movieRepository.save(movieOpt.get());
        actorRepository.save(actorOpt.get());
    }

//    @PutMapping("/movie/{title}/removeGenre/{genre}")
//    public void removeGenreFromMovie(@PathVariable String title, @PathVariable String genre) {
//        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
//        if (movieOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
//        }
//        Optional<Genre> genreOpt = genreRepository.findDistinctByGenre(genre);
//        if (genreOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genre));
//        }
//
//        movieOpt.get().removeGenre(genreOpt.get());
//        genreOpt.get().removeMovie(movieOpt.get());
//        movieRepository.save(movieOpt.get());
//        genreRepository.save(genreOpt.get());
//    }
//
//    @PutMapping("/movie/{title}/removeDirector/{firstName}/{lastName}")
//    public void removeDirectorFromMovie(@PathVariable String title, @PathVariable String firstName, @PathVariable String lastName) {
//        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
//        if (movieOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
//        }
//        Optional<Actor> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
//        if (directorOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
//        }
//
//        movieOpt.get().removeDirector(directorOpt.get());
//        directorOpt.get().removeMovie(movieOpt.get());
//        movieRepository.save(movieOpt.get());
//        actorRepository.save(directorOpt.get());
//    }

    @DeleteMapping("/movie/{title}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable String title) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        movieRepository.delete(movieOpt.get());
    }
}