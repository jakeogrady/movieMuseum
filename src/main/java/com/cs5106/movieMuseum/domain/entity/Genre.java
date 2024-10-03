package com.cs5106.movieMuseum.domain.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class  Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String genreName;
    private String genreDescription;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    public Genre() {
        super();
    }

    public Genre(String genreName) {
        super();
        this.genreName = genreName;
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
        //movie.getGenres().add(this);
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        //movie.getGenres().remove(this);
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public Long getId() {
        return id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
