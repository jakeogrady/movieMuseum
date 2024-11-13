package com.cs5106.movieMuseum.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenreDTO {
    private Long id;
    private String name;

    public GenreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}