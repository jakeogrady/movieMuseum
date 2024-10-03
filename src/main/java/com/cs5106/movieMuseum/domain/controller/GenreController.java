package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Genre;
import com.cs5106.movieMuseum.domain.repository.GenreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class GenreController {
    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping("/genres")
    public Iterable<Genre> getGenres() {
        return genreRepository.findAll();
    }

    // Doesn't work
    @PostMapping("/genres")
    public Genre addGenre(@RequestBody Genre genre) {
        return genreRepository.save(genre);
    }

    // Doesn't work
    @PutMapping("/genres/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre genreBody) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);

        if (optionalGenre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Genre genre = optionalGenre.get();
        genre.setGenreName(genreBody.getGenreName());
        Genre updatedGenre = genreRepository.save(genre);
        return ResponseEntity.ok(updatedGenre);
    }

    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Genre> deleteGenre(@PathVariable Long id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if (optionalGenre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Genre genre = optionalGenre.get();
        genreRepository.delete(genre);
        return ResponseEntity.ok(genre);
    }
}