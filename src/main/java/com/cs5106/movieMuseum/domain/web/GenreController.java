package com.cs5106.movieMuseum.domain.web;

import com.cs5106.movieMuseum.domain.Genre;
import com.cs5106.movieMuseum.domain.repository.GenreRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class GenreController {
    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping(value = "/genres")
    public Iterable<Genre> getGenres() {
        return genreRepository.findAll();
    }
}