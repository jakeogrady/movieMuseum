package com.cs5106.movieMuseum.domain.service;

import com.cs5106.movieMuseum.domain.entity.Actor;
import com.cs5106.movieMuseum.domain.entity.Director;
import com.cs5106.movieMuseum.domain.entity.Genre;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.ActorRepository;
import com.cs5106.movieMuseum.domain.repository.DirectorRepository;
import com.cs5106.movieMuseum.domain.repository.GenreRepository;
import com.cs5106.movieMuseum.domain.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Movie> getMoviesByReleaseYear(@PathVariable int releaseYear) {
        List<Movie> movies = movieRepository.findByReleaseYear(releaseYear);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with release year %d not found", releaseYear));
        }
        return movies;
    }

    @GetMapping("/movies/imdbRating/{imdbRating}")
    public List<Movie> getMoviesByImdbRating(@PathVariable double imdbRating) {
        List<Movie> movies = movieRepository.findByImdbRating(imdbRating);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with imdb rating %f not found", imdbRating));
        }
        return movies;
    }

    @GetMapping("movies/imdbRating/between/{low}/{high}")
    public List<Movie> getMoviesByImdbRatingBetween(@PathVariable double low, @PathVariable double high) {
        List<Movie> movies = movieRepository.findByImdbRatingBetween(low, high);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with imdb rating between %f and %f not found", low, high));
        }
        return movies;
    }

    @GetMapping("/movies/title/substring/{title}")
    public List<Movie> getMoviesByTitleSubstring(@PathVariable String title) {
        List<Movie> movies = movieRepository.findByTitleSubstring(title);
        if (movies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with title containing %s not found", title));
        }
        return movies;
    }

    @GetMapping("/movie/{title}/actors")
    public Set<Actor> getMovieActors(@PathVariable String title) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        return movieOpt.get().getActors();
    }

    @GetMapping("/movie/{title}/director")
    public Director getMovieDirector(@PathVariable String title) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        return movieOpt.get().getDirector();
    }

    @GetMapping("/movie/{title}/genre")
    public Set<Genre> getMovieGenres(@PathVariable String title) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        return movieOpt.get().getGenres();
    }

    @PostMapping("/movie/post")
    public Movie addMovie(@RequestBody Movie movie) {
        if (movieRepository.findDistinctByTitle(movie.getTitle()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Movie %s already exists", movie.getTitle()));
        }
        return movieRepository.save(movie);
    }

//    @PutMapping("/movie/{title}")
//    public ResponseEntity<Movie> updateMovie(@PathVariable String title, @RequestBody Movie movie) {
//        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
//        if (movieOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
//        }
//
//        if (movie.getTitle().equals(title) ) {
//            movie.setId(movieOpt.get().getId());
//            movieRepository.save(movie);
//            return ResponseEntity.ok(movie);
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Movie %s does not match the path", title));
//        }
//    }

    @PutMapping("/movies/put")
    @ResponseStatus(HttpStatus.OK)
    public void updateMovies(@RequestBody List<Movie> newMovieData) {
        for (int i = 0; i < newMovieData.size(); i++) {
            Optional<Movie> movieOpt = movieRepository.findById(newMovieData.get(i).getId());
            if (movieOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie with id %d not found", newMovieData.get(i).getId()));
            }
            System.out.println("Updating movie with id " + newMovieData.get(i).getId());
            Movie movie = movieOpt.get();
            movie.setTitle(newMovieData.get(i).getTitle());
            movie.setImdbRating(newMovieData.get(i).getImdbRating());
            movie.setReleaseYear(newMovieData.get(i).getReleaseYear());
            movieRepository.save(movie);
        }
    }

    @PutMapping("/movie/{title}/addActor/{firstName}/{lastName}")
    public ResponseEntity<Set<Actor>> addActorToMovie(@PathVariable String title, @PathVariable String firstName, @PathVariable String lastName) {
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
        return ResponseEntity.ok(movieOpt.get().getActors());
    }

    @PutMapping("/movie/{title}/addGenre/{genre}")
    public ResponseEntity<Set<Genre>> addGenreToMovie(@PathVariable String title, @PathVariable String genre) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        Optional<Genre> genreOpt = genreRepository.findDistinctByGenreName(genre);
        if (genreOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genre));
        }

        movieOpt.get().addGenre(genreOpt.get());
        genreOpt.get().addMovie(movieOpt.get());
        movieRepository.save(movieOpt.get());
        genreRepository.save(genreOpt.get());
        return ResponseEntity.ok(movieOpt.get().getGenres());
    }

    @PutMapping("/movie/{title}/addDirector/{firstName}/{lastName}")
    public ResponseEntity<Director> addDirectorToMovie(@PathVariable String title, @PathVariable String firstName, @PathVariable String lastName) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }

        movieOpt.get().setDirector(directorOpt.get());
        directorOpt.get().addMovie(movieOpt.get());
        movieRepository.save(movieOpt.get());
        directorRepository.save(directorOpt.get());
        return ResponseEntity.ok(movieOpt.get().getDirector());
    }

    @PutMapping("/movie/{title}/removeActor/{firstName}/{lastName}")
    public ResponseEntity<Set<Actor>> removeActorFromMovie(@PathVariable String title, @PathVariable String firstName, @PathVariable String lastName) {
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
        return ResponseEntity.ok(movieOpt.get().getActors());
    }

    @PutMapping("/movie/{title}/removeGenre/{genre}")
    public ResponseEntity<Set<Genre>> removeGenreFromMovie(@PathVariable String title, @PathVariable String genre) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        Optional<Genre> genreOpt = genreRepository.findDistinctByGenreName(genre);
        if (genreOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genre));
        }

        movieOpt.get().removeGenre(genreOpt.get());
        genreOpt.get().removeMovie(movieOpt.get());
        movieRepository.save(movieOpt.get());
        genreRepository.save(genreOpt.get());
        return ResponseEntity.ok(movieOpt.get().getGenres());
    }

    @PutMapping("/movie/{title}/removeDirector/{firstName}/{lastName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeDirectorFromMovie(@PathVariable String title, @PathVariable String firstName, @PathVariable String lastName) {
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }
        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }

        movieOpt.get().setDirector(null);
        directorOpt.get().removeMovie(movieOpt.get());
        movieRepository.save(movieOpt.get());
        directorRepository.save(directorOpt.get());
    }

    @DeleteMapping("/movies/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovies(@RequestBody long[] movieIds) {
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