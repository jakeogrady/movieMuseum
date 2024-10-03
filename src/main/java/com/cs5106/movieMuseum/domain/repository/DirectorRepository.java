package com.cs5106.movieMuseum.domain.repository;

import com.cs5106.movieMuseum.domain.entity.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository extends CrudRepository<Director, Long> {
    List<Director> findByFirstName(String firstName);
    List<Director> findByLastName(String lastName);
    List<Director> findByFirstNameAndLastName(String firstName, String lastName);
    //Optional<Director> findDistinctByFirstNameAndLastName(String firstName, String lastName);


    @Query("SELECT a FROM Director a WHERE a.firstName LIKE %:firstName%")
    List<Director> findByFirstNameSubstring(String firstName);

    @Query("SELECT a FROM Director a WHERE a.lastName LIKE %:lastName%")
    List<Director> findByLastNameSubstring(String lastName);
    
}
