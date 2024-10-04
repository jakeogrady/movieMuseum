package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Actor;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.ActorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

@RestController
public class ActorController {
    private final ActorRepository actorRepository;

    public ActorController(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @GetMapping("/actors")
    public Iterable<Actor> getActors() {
        return actorRepository.findAll();
    }

    @GetMapping("/actors/firstName/{firstName}")
    public List<Actor> getActorsByFirstName(@PathVariable String firstName) {
        List<Actor> actors = actorRepository.findByFirstName(firstName);
        if (actors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s not found", firstName));
        }
        return actors;
    }

    @GetMapping("/actors/lastName/{lastName}")
    public List<Actor> getActorsByLastName(@PathVariable String lastName) {
        List<Actor> actors = actorRepository.findByLastName(lastName);
        if (actors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s not found", lastName));
        }
        return actors;
    }

    @GetMapping("/actor/{firstName}/{lastName}")
    public Actor getActorByFirstNameAndLastName(@PathVariable String firstName, @PathVariable String lastName) {
        Optional<Actor> actorOpt = actorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (actorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s %s not found", firstName, lastName));
        }
        return actorOpt.get();
    }

    @GetMapping("/actors/firstName/substring/{firstName}")
    public List<Actor> getActorsByFirstNameSubstring(@PathVariable String firstName) {
        List<Actor> actors = actorRepository.findByFirstNameSubstring(firstName);
        if (actors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor with first name containing %s not found", firstName));
        }
        return actors;
    }

    @GetMapping("/actors/lastName/substring/{lastName}")
    public List<Actor> getActorsByLastNameSubstring(@PathVariable String lastName) {
        List<Actor> actors = actorRepository.findByLastNameSubstring(lastName);
        if (actors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor with last name containing %s not found", lastName));
        }
        return actors;
    }

    @GetMapping("/actor/{firstName}/{lastName}/movies")
    public Set<Movie> getMoviesByActor(@PathVariable String firstName, @PathVariable String lastName) {
        Optional<Actor> actorOpt = actorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (actorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s %s not found", firstName, lastName));
        }
        return actorOpt.get().getMovies();
    }

    @PostMapping("/actors")
    public Actor addActor(@RequestBody Actor actor) {
        if (actorRepository.findDistinctByFirstNameAndLastName(actor.getFirstName(), actor.getLastName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Actor %s %s already exists", actor.getFirstName(), actor.getLastName()));
        }
        return actorRepository.save(actor);
    }

    @PutMapping("/actor/{firstName}/{lastName}")
    public ResponseEntity<Actor> updateActor(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Actor actor) {
        Optional<Actor> actorOpt = actorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (actorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s %s not found", firstName, lastName));
        }

        if (actor.getFirstName().equals(firstName) && actor.getLastName().equals(lastName)) {
            actor.setId(actorOpt.get().getId());
            actorRepository.save(actor);
            return ResponseEntity.ok(actor);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, format("Actor %s %s does not match the path", firstName, lastName));
        }
    }

    @DeleteMapping("/actor/{firstName}/{lastName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(@PathVariable String firstName, @PathVariable String lastName) {
        Optional<Actor> actor = actorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (actor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s %s not found", firstName, lastName));
        }
        actorRepository.delete(actor.get());
    }


}