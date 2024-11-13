package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Director;
import com.cs5106.movieMuseum.domain.service.DirectorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DirectorController {
    private final DirectorService service;

    public DirectorController(DirectorService directorService) {
        this.service = directorService;
    }

    @GetMapping("/directors")
    public Iterable<Director> getDirectors() {
        return service.getDirectors();
    }

    @GetMapping("/directors/firstName/{firstName}")
    public List<Director> getDirectorsByFirstName(@PathVariable String firstName) {
        return service.getDirectorsByFirstName(firstName);
    }

    @GetMapping("/directors/lastName/{lastName}")
    public List<Director> getDirectorsByLastName(@PathVariable String lastName) {
        return service.getDirectorsByLastName(lastName);
    }

    @GetMapping("/director/{firstName}/{lastName}")
    public Director getDirectorByFirstNameAndLastName(@PathVariable String firstName, @PathVariable String lastName) {
        return service.getDirectorByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping("/directors/firstName/substring/{firstName}")
    public List<Director> getDirectorsByFirstNameSubstring(@PathVariable String firstName) {
        return service.getDirectorsByFirstNameSubstring(firstName);
    }

    @GetMapping("/directors/lastName/substring/{lastName}")
    public List<Director> getDirectorsByLastNameSubstring(@PathVariable String lastName) {
        return service.getDirectorsByLastNameSubstring(lastName);
    }

    @PostMapping("/director/post")
    public Director addDirector(@RequestBody Director director) {
        return service.addDirector(director);
    }

    @PutMapping("directors/put")
    @ResponseStatus(HttpStatus.OK)
    public void updateDirectors(@RequestBody List<Director> newDirectorData) {
        service.updateDirectors(newDirectorData);
    }

    @DeleteMapping("/directors/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDirectors(@RequestBody long[] directorIds) {
        service.deleteDirectors(directorIds);
    }
}
