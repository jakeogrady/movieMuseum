package com.cs5106.movieMuseum.domain.controller;

import com.cs5106.movieMuseum.domain.entity.Actor;
import com.cs5106.movieMuseum.domain.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActorController {
    private final ActorService service;

    public ActorController(ActorService service) {
        this.service = service;
    }

    @GetMapping("/actors")
    public Iterable<Actor> getActors() {
        return service.getActors();
    }

    @GetMapping("/actors/firstName/{firstName}")
    public List<Actor> getActorsByFirstName(@PathVariable String firstName) {
        return service.getActorsByFirstName(firstName);
    }

    @GetMapping("/actors/lastName/{lastName}")
    public List<Actor> getActorsByLastName(@PathVariable String lastName) {
        return service.getActorsByLastName(lastName);
    }

    @GetMapping("/actor/{firstName}/{lastName}")
    public Actor getActorByFirstNameAndLastName(@PathVariable String firstName, @PathVariable String lastName) {
        return service.getActorByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping("/actors/firstName/substring/{firstName}")
    public List<Actor> getActorsByFirstNameSubstring(@PathVariable String firstName) {
        return service.getActorsByFirstNameSubstring(firstName);
    }

    @GetMapping("/actors/lastName/substring/{lastName}")
    public List<Actor> getActorsByLastNameSubstring(@PathVariable String lastName) {

        return service.getActorsByLastNameSubstring(lastName);
    }

    @PostMapping("/actor/post")
    public Actor addActor(@RequestBody Actor actor) {
        return service.addActor(actor);
    }

    @PostMapping("/actors/post")
    public void addActors(@RequestBody List<Actor> actor) {
        actor.forEach(this::addActor);
    }

    @PutMapping("/actors/put")
    @ResponseStatus(HttpStatus.OK)
    public void updateActors(@RequestBody List<Actor> newActorData) {
        service.updateActors(newActorData);
    }

    @DeleteMapping("/actors/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActors(@RequestBody long[] actorIds) {
        service.deleteActors(actorIds);
    }
}