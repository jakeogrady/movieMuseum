package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Director;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.DirectorRepository;
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
public class DirectorController {
    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;

    public DirectorController(DirectorRepository directorRepository, MovieRepository movieRepository) {
        this.directorRepository = directorRepository;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/directors")
    public Iterable<Director> getDirectors() {
        return directorRepository.findAll();
    }

    @GetMapping("/directors/firstName/{firstName}")
    public List<Director> getDirectorsByFirstName(@PathVariable String firstName) {
        List<Director> directors = directorRepository.findByFirstName(firstName);
        if (directors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s not found", firstName));
        }
        return directors;
    }

    @GetMapping("/directors/lastName/{lastName}")
    public List<Director> getDirectorsByLastName(@PathVariable String lastName) {
        List<Director> directors = directorRepository.findByLastName(lastName);
        if (directors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s not found", lastName));
        }
        return directors;
    }

    @GetMapping("/directors/{firstName}/{lastName}")
    public Director getDirectorByFirstNameAndLastName(@PathVariable String firstName, @PathVariable String lastName) {
        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }
        return directorOpt.get();
    }

    @GetMapping("/directors/firstName/substring/{firstName}")
    public List<Director> getDirectorsByFirstNameSubstring(@PathVariable String firstName) {
        List<Director> directors = directorRepository.findByFirstNameSubstring(firstName);
        if (directors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director with first name containing %s not found", firstName));
        }
        return directors;
    }

    @GetMapping("/directors/lastName/substring/{lastName}")
    public List<Director> getDirectorsByLastNameSubstring(@PathVariable String lastName) {
        List<Director> directors = directorRepository.findByLastNameSubstring(lastName);
        if (directors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director with last name containing %s not found", lastName));
        }
        return directors;
    }

    @PutMapping("/directors/{id}")
    public ResponseEntity<Director> updateDirector(@PathVariable Long id, @RequestBody Director directorDetails) {
        Optional<Director> optionalDirector = directorRepository.findById(id);

        if (optionalDirector.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Director director = optionalDirector.get();
        director.setFirstName(directorDetails.getFirstName());
        director.setLastName(directorDetails.getLastName());
        Director updatedDirector = directorRepository.save(director);
        return ResponseEntity.ok(updatedDirector);
    }

    @GetMapping("/director/{firstName}/{lastName}/movies")
    public Set<Movie> getMoviesByDirector(@PathVariable String firstName, @PathVariable String lastName) {
        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }
        return directorOpt.get().getMovies();
    }

    @PostMapping("/directors")
    public Director addDirector(@RequestBody Director director) {
        if (directorRepository.findDistinctByFirstNameAndLastName(director.getFirstName(), director.getLastName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Director %s %s already exists", director.getFirstName(), director.getLastName()));
        }
        return directorRepository.save(director);
    }

    @PutMapping("/director/{firstName}/{lastName}")
    public ResponseEntity<Director> updateDirector(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Director director) {
        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }

        if (director.getFirstName().toLowerCase().equals(firstName) && director.getLastName().toLowerCase().equals(lastName)) {
            director.setId(directorOpt.get().getId());
            directorRepository.save(director);
            return ResponseEntity.ok(director);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Director %s %s does not match the path", firstName, lastName));
        }
    }

    @PutMapping("/director/{firstName}/{lastName}/addMovie/{title}")
    public ResponseEntity<Set<Movie>> addMovieToDirector(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String title) {

        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }

        directorOpt.get().addMovie(movieOpt.get());
        directorRepository.save(directorOpt.get());
        movieRepository.save(movieOpt.get());
        return ResponseEntity.ok(directorOpt.get().getMovies());
    }

    @PutMapping("/director/{firstName}/{lastName}/removeMovie/{title}")
    public ResponseEntity<Set<Movie>> removeMovieFromDirector(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String title) {

        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }
        Optional<Movie> movieOpt = movieRepository.findDistinctByTitle(title);
        if (movieOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Movie %s not found", title));
        }

        directorOpt.get().removeMovie(movieOpt.get());
        directorRepository.save(directorOpt.get());
        return ResponseEntity.ok(directorOpt.get().getMovies());
    }

    @DeleteMapping("/director/{firstName}/{lastName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDirector(@PathVariable String firstName, @PathVariable String lastName) {
        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }
        directorRepository.delete(directorOpt.get());
    }
}
