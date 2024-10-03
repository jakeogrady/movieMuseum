package com.cs5106.movieMuseum.domain.repository;

import com.cs5106.movieMuseum.domain.entity.Director;
import org.springframework.data.repository.CrudRepository;
import com.cs5106.movieMuseum.domain.entity.Movie;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findById(int Id);
    List<Movie> findByTitle(String title);
    List<Movie> findByReleaseYear(int releaseYear);
    List<Movie> findByImdbRating(double imdbRating);

    List<Movie> findByTitleAndImdbRatingOrderByImdbRatingDesc(String title, double imdbRating);
}
