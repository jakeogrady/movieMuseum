package com.cs5106.movieMuseum.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    List<Genre> findAllByGenreName(String genreName);
}
