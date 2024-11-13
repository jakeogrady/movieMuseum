package com.cs5106.movieMuseum.domain.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private int age;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "actors")
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    public Actor(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
        movie.addActor(this);
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.removeActor(this);
    }

    @PreRemove
    public void removeRelationships(){
        movies.forEach(m -> m.getActors().remove(this));
    }

}
