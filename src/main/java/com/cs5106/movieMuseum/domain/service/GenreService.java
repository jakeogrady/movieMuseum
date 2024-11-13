package com.cs5106.movieMuseum.domain.service;

import com.cs5106.movieMuseum.domain.entity.Genre;
import com.cs5106.movieMuseum.domain.repository.GenreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Iterable<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreByGenreName(String genreName) {
        Optional<Genre> genreOptional = genreRepository.findDistinctByGenreName(genreName);
        if(genreOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genreName));
        }
        return genreOptional.get();
    }

    public List<Genre> getGenresByGenreNameSubstring(String genreName) {
        List<Genre> genres = genreRepository.findGenresByGenreNameSubstring(genreName);
        if (genres.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre %s not found", genreName));
        }
        return genres;
    }

    public Genre addGenre(Genre genre) {
        if (genreRepository.findDistinctByGenreName(genre.getGenreName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Genre %s already exists", genre.getGenreName()));
        }
        return genreRepository.save(genre);
    }

    public void updateGenres(List<Genre> newGenreData) {
        for (Genre newGenreDatum : newGenreData) {
            Optional<Genre> genreOpt = genreRepository.findById(newGenreDatum.getId());
            if (genreOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre with id %d not found", newGenreDatum.getId()));
            }
            System.out.println("Updating genre with id " + newGenreDatum.getId());
            Genre genre = genreOpt.get();
            genre.setGenreName(newGenreDatum.getGenreName());
            genreRepository.save(genre);
        }
    }

    public void deleteGenres(long[] genreIds) {
        for (long genreId : genreIds) {
            Optional<Genre> genreOpt = genreRepository.findById(genreId);
            if (genreOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Genre with id %d not found", genreId));
            }
            System.out.println("Deleting genre with id " + genreId);
            genreRepository.delete(genreOpt.get());
        }
    }
}
