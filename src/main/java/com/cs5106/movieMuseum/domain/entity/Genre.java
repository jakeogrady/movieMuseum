package com.cs5106.movieMuseum.domain.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String genreName;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    public Genre(String genreName) {
        super();
        this.genreName = genreName;
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
        movie.getGenres().add(this);
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getGenres().remove(this);
    }

}
