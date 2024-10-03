package com.cs5106.movieMuseum.domain.repository;

import com.cs5106.movieMuseum.domain.entity.Actor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ActorRepository extends CrudRepository<Actor, Long> {
    List<Actor> findByFirstName(String firstName);
    List<Actor> findByLastName(String lastName);
    Optional<Actor> findDistinctByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT a FROM Actor a WHERE a.firstName LIKE %:firstName%")
    List<Actor> findByFirstNameSubstring(String firstName);

    @Query("SELECT a FROM Actor a WHERE a.lastName LIKE %:lastName%")
    List<Actor> findByLastNameSubstring(String lastName);
}
