package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Actor;
import com.cs5106.movieMuseum.domain.entity.Director;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.DirectorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
public class DirectorController {
    private final DirectorRepository directorRepository;

    public DirectorController(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @GetMapping("/directors")
    public Iterable<Director> getDirectors() {
        return directorRepository.findAll();
    }

    @GetMapping("/directors/{id}")
    public ResponseEntity<Director> getDirector(@PathVariable Long id) {
        Optional<Director> optionalDirector = directorRepository.findById(id);

        if (optionalDirector.isPresent()) {
            Director director = optionalDirector.get();
            return ResponseEntity.ok(director);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/directors")
    public Director createDirector(@RequestBody Director director) {
        return directorRepository.save(director);
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

    @DeleteMapping("/directors/{id}")
    public ResponseEntity<Director> deleteDirector(@PathVariable Long id) {
        Optional<Director> optionalDirector = directorRepository.findById(id);
        if (optionalDirector.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Director director = optionalDirector.get();
        directorRepository.delete(director);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/directors/firstName/{firstName}")
    public Iterable<Director> getDirectorsByFirstName(@PathVariable String firstName) {
        return directorRepository.findByFirstName(firstName);
    }

    @GetMapping("/directors/firstName/{lastName}")
    public Iterable<Director> getDirectorsByLastName(@PathVariable String lastName) {
        return directorRepository.findByLastName(lastName);
    }

    @GetMapping("/directors/firstName/{firstName}/lastName/{lastName}")
    public Iterable<Director> getDirectorsByFirstNameAndLastName(@PathVariable String firstName, @PathVariable String lastName) {
        return directorRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping("/directors/firstName/substring/{firstName}")
    public Iterable<Director> getDirectorsByFirstNameSubstring(@PathVariable String firstName) {
        return directorRepository.findByFirstNameSubstring(firstName);
    }

    @GetMapping("/directors/lastName/substring/{lastName}")
    public Iterable<Director> getDirectorsByLastNameSubstring(@PathVariable String lastName) {
        return directorRepository.findByLastNameSubstring(lastName);
    }

    @GetMapping("/directors/{id}/movies")
    public ResponseEntity<Set<Movie>> getMoviesByDirector(@PathVariable Long id) {
        Optional<Director> optionalDirector = directorRepository.findById(id);
        if (optionalDirector.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Director director = optionalDirector.get();
        Set<Movie> movies = director.getMovies();
        return ResponseEntity.ok(movies);
    }

}
