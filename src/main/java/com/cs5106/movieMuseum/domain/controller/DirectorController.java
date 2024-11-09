package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Director;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.DirectorRepository;
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

    public DirectorController(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
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

    @GetMapping("/director/{firstName}/{lastName}")
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

//    @PutMapping("/director/{firstName}/{lastName}")
//    public ResponseEntity<Director> updateDirector(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Director director) {
//        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
//        if (directorOpt.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
//        }
//
//        if (director.getFirstName().toLowerCase().equals(firstName) && director.getLastName().toLowerCase().equals(lastName)) {
//            director.setId(directorOpt.get().getId());
//            directorRepository.save(director);
//            return ResponseEntity.ok(director);
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Director %s %s does not match the path", firstName, lastName));
//        }
//    }

    @PutMapping("directors/put")
    @ResponseStatus(HttpStatus.OK)
    public void updateDirectors(@RequestBody List<Director> newDirectorData) {
        for (int i = 0; i < newDirectorData.size(); i++) {
            Optional<Director> directorOpt = directorRepository.findById(newDirectorData.get(i).getId());
            if (directorOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director with id %d not found", newDirectorData.get(i).getId()));
            }
            System.out.println("Updating director with id " + newDirectorData.get(i).getId());
            Director director = directorOpt.get();
            director.setFirstName(newDirectorData.get(i).getFirstName());
            director.setLastName(newDirectorData.get(i).getLastName());
            director.setAge(newDirectorData.get(i).getAge());
            directorRepository.save(director);
        }
    }

    @DeleteMapping("/directors/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDirectors(@RequestBody long[] directorIds) {
        for (long directorId : directorIds) {
            Optional<Director> directorOpt = directorRepository.findById(directorId);
            if (directorOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director with id %d not found", directorId));
            }
            System.out.println("Deleting director with id " + directorId);
            directorRepository.delete(directorOpt.get());
        }
    }
}
