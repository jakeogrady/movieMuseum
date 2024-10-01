package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Director;
import com.cs5106.movieMuseum.domain.repository.DirectorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
}
