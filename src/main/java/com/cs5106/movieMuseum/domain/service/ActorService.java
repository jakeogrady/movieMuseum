package com.cs5106.movieMuseum.domain.service;

import com.cs5106.movieMuseum.domain.entity.Actor;
import com.cs5106.movieMuseum.domain.repository.ActorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Iterable<Actor> getActors() {
        System.out.println("Getting all actors");
        return actorRepository.findAll();
    }

    public List<Actor> getActorsByFirstName(String firstName) {
        List<Actor> actors = actorRepository.findByFirstName(firstName);
        if (actors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s not found", firstName));
        }
        return actors;
    }

    public List<Actor> getActorsByLastName(String lastName) {
        List<Actor> actors = actorRepository.findByLastName(lastName);
        if (actors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s not found", lastName));
        }
        return actors;
    }

    public Actor getActorByFirstNameAndLastName(String firstName, String lastName) {
        Optional<Actor> actorOpt = actorRepository.findDistinctByFirstNameAndLastName(firstName, lastName);
        if (actorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor %s %s not found", firstName, lastName));
        }
        return actorOpt.get();
    }

    public List<Actor> getActorsByFirstNameSubstring(String firstName) {
        List<Actor> actors = actorRepository.findByFirstNameSubstring(firstName);
        if (actors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor with first name containing %s not found", firstName));
        }
        return actors;
    }

    public List<Actor> getActorsByLastNameSubstring(String lastName) {
        List<Actor> actors = actorRepository.findByLastNameSubstring(lastName);
        if (actors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor with last name containing %s not found", lastName));
        }
        return actors;
    }

    public Actor addActor(Actor actor) {
        System.out.println("Adding actor " + actor.getFirstName() + " " + actor.getLastName());
        if (actorRepository.findDistinctByFirstNameAndLastName(actor.getFirstName(), actor.getLastName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, format("Actor %s %s already exists", actor.getFirstName(), actor.getLastName()));
        }
        return actorRepository.save(actor);
    }

    public void updateActors(List<Actor> newActorData) {
        for (Actor newActorDatum : newActorData) {
            Optional<Actor> actorOpt = actorRepository.findById(newActorDatum.getId());
            if (actorOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor with id %d not found", newActorDatum.getId()));
            }
            System.out.println("Updating actor with id " + newActorDatum.getId());
            Actor actor = actorOpt.get();
            actor.setFirstName(newActorDatum.getFirstName());
            actor.setLastName(newActorDatum.getLastName());
            actor.setAge(newActorDatum.getAge());
            actorRepository.save(actor);
        }
    }

    public void deleteActors(long[] actorIds) {
        for (long actorId : actorIds) {
            Optional<Actor> actorOpt = actorRepository.findById(actorId);
            if (actorOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, format("Actor with id %d not found", actorId));
            }
            System.out.println("Deleting actor with id " + actorId);
            actorRepository.delete(actorOpt.get());
        }
    }
}