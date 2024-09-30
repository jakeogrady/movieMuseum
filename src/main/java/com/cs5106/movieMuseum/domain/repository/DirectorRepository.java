package com.cs5106.movieMuseum.domain.repository;

import com.cs5106.movieMuseum.domain.entity.Director;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DirectorRepository extends CrudRepository<Director, Long> {
    List<Director> findByFirstName(String firstName);
    List<Director> findByLastName(String lastName);
    List<Director> findByFirstNameAndLastName(String firstName, String lastName);
}
