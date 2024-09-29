package com.cs5106.movieMuseum.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActorRepository extends CrudRepository<Actor, Long> {
    List<Actor> findByFirstName(String firstName);
    List<Actor> findByLastName(String lastName);
    List<Actor> findByFirstNameAndLastName(String firstName, String lastName);
}