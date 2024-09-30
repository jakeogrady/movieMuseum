package com.cs5106.movieMuseum.domain.repository;

import com.cs5106.movieMuseum.domain.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findByTitle(String title);
    List<Movie> findByDirector(String director);
    List<Movie> findByReleaseYear(int releaseYear);
    List<Movie> findByImdbRating(double imdbRating);

    List<Movie> findByTitleAndDirector(String title, String director);
    List<Movie> findByTitleAndImdbRatingOrderByImdbRatingDesc(String title, double imdbRating);

}
