package com.cs5106.movieMuseum.domain.service;

import com.cs5106.movieMuseum.domain.entity.Director;
import com.cs5106.movieMuseum.domain.repository.DirectorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class DirectorService {
    private final DirectorRepository directorRepository;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public Iterable<Director> getDirectors() {
        return directorRepository.findAll();
    }

    public List<Director> getDirectorsByFirstName(String firstName) {
        List<Director> directors = directorRepository.findByFirstName(firstName);
        if (directors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s not found", firstName));
        }
        return directors;
    }

    public List<Director> getDirectorsByLastName(String lastName) {
        List<Director> directors = directorRepository.findByLastName(lastName);
        if (directors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s not found", lastName));
        }
        return directors;
    }

    public Director getDirectorByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Director> directorOpt = directorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (directorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director %s %s not found", firstName, lastName));
        }
        return directorOpt.get();
    }

    public List<Director> getDirectorsByFirstNameSubstring(String firstName) {
        List<Director> directors = directorRepository.findByFirstNameSubstring(firstName);
        if (directors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director with first name containing %s not found", firstName));
        }
        return directors;
    }

    public List<Director> getDirectorsByLastNameSubstring(String lastName) {
        List<Director> directors = directorRepository.findByLastNameSubstring(lastName);
        if (directors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director with last name containing %s not found", lastName));
        }
        return directors;
    }

    public Director addDirector(Director director) {
        if (directorRepository.findDistinctByFirstNameAndLastName(director.getFirstName(), director.getLastName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Director %s %s already exists", director.getFirstName(), director.getLastName()));
        }
        return directorRepository.save(director);
    }

    public void updateDirectors(List<Director> newDirectorData) {
        for (Director newDirectorDatum : newDirectorData) {
            Optional<Director> directorOpt = directorRepository.findById(newDirectorDatum.getId());
            if (directorOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director with id %d not found", newDirectorDatum.getId()));
            }
            System.out.println("Updating director with id " + newDirectorDatum.getId());
            Director director = directorOpt.get();
            director.setFirstName(newDirectorDatum.getFirstName());
            director.setLastName(newDirectorDatum.getLastName());
            director.setAge(newDirectorDatum.getAge());
            directorRepository.save(director);
        }
    }

    public void deleteDirectors(long[] directorIds) {
        for (long directorId : directorIds) {
            Optional<Director> directorOpt = directorRepository.findById(directorId);
            if (directorOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Director with id %d not found", directorId));
            }
            System.out.println("Deleting director with id " + directorId);
            directorRepository.delete(directorOpt.get());
        }
    }
}
