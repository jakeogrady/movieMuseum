package com.cs5106.movieMuseum.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cs5106.movieMuseum.domain.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String genreName);

    @Query("SELECT g FROM Genre g WHERE g.genreName LIKE %:genreName%")
    List<Genre> findGenresByGenreNameSubstring(String genreName);
    
}
