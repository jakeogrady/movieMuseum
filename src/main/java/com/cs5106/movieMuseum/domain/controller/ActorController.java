package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Actor;
import com.cs5106.movieMuseum.domain.repository.ActorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping("/actors/{id}")
    public ResponseEntity<Actor> getActor(@PathVariable Long id) {
        Optional<Actor> optionalActor = actorRepository.findById(id);

        if (optionalActor.isPresent()) {
            Actor actor = optionalActor.get();
            return ResponseEntity.ok(actor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/actors")
    public Actor createActor(@RequestBody Actor actor) {
        return actorRepository.save(actor);
    }

    @PutMapping("/actors/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable Long id, @RequestBody Actor actorDetails) {
        Optional<Actor> optionalActor = actorRepository.findById(id);

        if (optionalActor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Actor actor = optionalActor.get();
        actor.setFirstName(actorDetails.getFirstName());
        actor.setLastName(actorDetails.getLastName());
        Actor updatedActor = actorRepository.save(actor);
        return ResponseEntity.ok(updatedActor);
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<Actor> deleteActor(@PathVariable Long id) {
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if (optionalActor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Actor actor = optionalActor.get();
        actorRepository.delete(actor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actors/firstName/{firstName}")
    public Iterable<Actor> getActorsByFirstName(@PathVariable String firstName) {
        return actorRepository.findByFirstName(firstName);
    }

    @GetMapping("/actors/firstName/{lastName}")
    public Iterable<Actor> getActorsByLastName(@PathVariable String lastName) {
        return actorRepository.findByLastName(lastName);
    }

    @GetMapping("/actors/firstName/{firstName}/lastName/{lastName}")
    public Iterable<Actor> getActorsByFirstNameAndLastName(@PathVariable String firstName, @PathVariable String lastName) {
        return actorRepository.findByFirstNameAndLastName(firstName, lastName);
    }

}
