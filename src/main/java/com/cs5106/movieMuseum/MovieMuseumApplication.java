package com.cs5106.movieMuseum;

import com.cs5106.movieMuseum.domain.entity.Director;
import com.cs5106.movieMuseum.domain.entity.Actor;
import com.cs5106.movieMuseum.domain.entity.Genre;
import com.cs5106.movieMuseum.domain.entity.Movie;
import com.cs5106.movieMuseum.domain.repository.ActorRepository;
import com.cs5106.movieMuseum.domain.repository.DirectorRepository;
import com.cs5106.movieMuseum.domain.repository.GenreRepository;
import com.cs5106.movieMuseum.domain.repository.MovieRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieMuseumApplication implements CommandLineRunner {
	private final MovieRepository movieRepository;
	private final GenreRepository genreRepository;
	private final ActorRepository actorRepository;
	private final DirectorRepository directorRepository;
	private static final Logger logger = LoggerFactory.getLogger(MovieMuseumApplication.class);

    public MovieMuseumApplication(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
		this.genreRepository = genreRepository;
		this.actorRepository = actorRepository;
		this.directorRepository = directorRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(MovieMuseumApplication.class, args);
		logger.info("Application started successfully.");
	}

	@Override
	public void run(String... args) throws Exception {
		// Joey & Alex
		// Create and save movies
		Movie shawshank = new Movie("The Shawshank Redemption", 1994, 9.3);
		Movie godfather = new Movie("The Godfather", 1972, 9.2);
		Movie darkKnight = new Movie("The Dark Knight", 2008, 9.0);

		movieRepository.save(shawshank);
		movieRepository.save(godfather);
		movieRepository.save(darkKnight);

		// Create genres
		Genre drama = new Genre("Drama");
		Genre crime = new Genre("Crime");
		Genre action = new Genre("Action");

		// Create Actors
		Actor mattDamon = new Actor("Matt","Damon");
		Actor bradPitt = new Actor("Brad","Pitt");
		Actor emmaStone = new Actor("Emma","Stone");

		actorRepository.save(mattDamon);
		actorRepository.save(bradPitt);
		actorRepository.save(emmaStone);

		Director frankDarabont = new Director("Frank", "Darabont");
		Director francisFordCoppola = new Director("Francis Ford", "Coppola");
		Director christopherNolan = new Director("Christopher", "Nolan");

		// Add genres to movies
		shawshank.addGenre(drama);
		godfather.addGenre(drama);
		godfather.addGenre(crime);
		darkKnight.addGenre(action);
		darkKnight.addGenre(crime);

		// Add director to movies
		shawshank.setDirector(frankDarabont);
		godfather.setDirector(francisFordCoppola);
		darkKnight.setDirector(christopherNolan);

		// Save genres
		genreRepository.save(drama);
		genreRepository.save(crime);
		genreRepository.save(action);

		// Save directors
		directorRepository.save(frankDarabont);
		directorRepository.save(francisFordCoppola);
		directorRepository.save(christopherNolan);

		// Save movies again to update the relationships
		movieRepository.save(shawshank);
		movieRepository.save(godfather);
		movieRepository.save(darkKnight);

		logger.info("Movies and genres saved to database.");
		logger.info("MovieRepository {}", movieRepository.findAll());
		for (Movie movie : movieRepository.findAll()) {
			logger.info("Title: {}, Director: {}, Release Year: {}, IMDB Rating: {}",
					movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getImdbRating());
		}

		for (Actor actor: actorRepository.findAll()){
			logger.info("First Name {}, Last Name {}", actor.getFirstName(), actor.getLastName());
		}


	}
}
