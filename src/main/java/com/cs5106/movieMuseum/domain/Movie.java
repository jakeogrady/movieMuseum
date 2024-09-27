package com.cs5106.movieMuseum.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String director;
    private int releaseYear;
    private double imdbRating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "movie_genre", // movie_genre is the name of the table that will be created
        joinColumns = @JoinColumn(name = "movie_id"),  // movie_id is the column name in the movie_genre table
        inverseJoinColumns = @JoinColumn(name = "genre_id")) // genre_id is the column name in the movie_genre table

    private Set<Genre> genres = new HashSet<>(); // genres is the name of the column in the movie table

    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getMovies().add(this);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getMovies().remove(this);
    }


    public Movie() {
        super();
    }

    public Movie(String title, String director, int releaseYear, double imdbRating) {
        super();
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.imdbRating = imdbRating;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int year) {
        this.releaseYear = year;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Long getId() {
        return id;
    }
}
