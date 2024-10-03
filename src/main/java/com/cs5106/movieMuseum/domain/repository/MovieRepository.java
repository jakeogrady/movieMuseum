package com.cs5106.movieMuseum.domain.repository;

import com.cs5106.movieMuseum.domain.entity.Director;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.cs5106.movieMuseum.domain.entity.Movie;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findById(int id);
    List<Movie> findByTitle(String title);
    List<Movie> findByReleaseYear(int releaseYear);
    List<Movie> findByImdbRating(double imdbRating);
    Optional<Movie> findDistinctByTitle(String title);

    List<Movie> findByTitleAndImdbRatingOrderByImdbRatingDesc(String title, double imdbRating);
    List<Movie> findByTitleAndReleaseYear(String title, int releaseYear);

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:titleSubstring%")
    List<Movie> findByTitleSubstring(String titleSubstring);

    @Query("SELECT m from Movie m WHERE m.imdbRating between ?1 and ?2")
    List<Movie> findByImdbRatingBetween(@Param("low") double lower, @Param("high") double upper);
}
