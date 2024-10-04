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

		Movie shawshank = new Movie("The Shawshank Redemption", 1994, 9.3);
		Movie godfather = new Movie("The Godfather", 1972, 9.2);
		Movie darkKnight = new Movie("The Dark Knight", 2008, 9.0);
		Movie secondMovieTest = new Movie("Second Movie Test", 2021, 8.0);

		movieRepository.save(shawshank);
		movieRepository.save(godfather);
		movieRepository.save(darkKnight);
		movieRepository.save(secondMovieTest);

		Genre drama = new Genre("Drama");
		Genre crime = new Genre("Crime");
		Genre action = new Genre("Action");

		Actor mattDamon = new Actor("Matt","Damon", 50);
		Actor bradPitt = new Actor("Brad","Pitt", 57);
		Actor emmaStone = new Actor("Emma","Stone", 32);
		Actor emmaTest = new Actor("Emma", "Test", 69);

		actorRepository.save(mattDamon);
		actorRepository.save(bradPitt);
		actorRepository.save(emmaStone);
		actorRepository.save(emmaTest);

		Director frankDarabont = new Director("Frank", "Darabont", 64);
		Director francisFordCoppola = new Director("Francis Ford", "Coppola", 85);
		Director christopherNolan = new Director("Christopher", "Nolan", 54);

		shawshank.addGenre(drama);
		godfather.addGenre(drama);
		godfather.addGenre(crime);
		darkKnight.addGenre(action);
		darkKnight.addGenre(crime);
		secondMovieTest.addGenre(action);
		secondMovieTest.addGenre(drama);

		shawshank.setDirector(frankDarabont);
		godfather.setDirector(francisFordCoppola);
		darkKnight.setDirector(christopherNolan);
		secondMovieTest.setDirector(christopherNolan);

		shawshank.addActor(mattDamon);
		godfather.addActor(bradPitt);
		darkKnight.addActor(emmaStone);
		darkKnight.addActor(emmaTest);
		secondMovieTest.addActor(emmaTest);
		secondMovieTest.addActor(emmaStone);

		genreRepository.save(drama);
		genreRepository.save(crime);
		genreRepository.save(action);

		directorRepository.save(frankDarabont);
		directorRepository.save(francisFordCoppola);
		directorRepository.save(christopherNolan);

		// Save movies again to update relationships
		movieRepository.save(shawshank);
		movieRepository.save(godfather);
		movieRepository.save(darkKnight);
		movieRepository.save(secondMovieTest);

		logger.info("Movies saved to database.");
		for (Movie movie : movieRepository.findAll()) {
			logger.info("Title: {}, Director: {}, Release Year: {}, IMDB Rating: {}",
					movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getImdbRating());
		}

		logger.info("Genres saved to database.");
		for (Genre genre : genreRepository.findAll()) {
			logger.info("Genre: {}", genre.getGenreName());
		}

		logger.info("Actors saved to database.");
		for (Actor actor: actorRepository.findAll()){
			logger.info("First Name: {}, Last Name: {}, Age: {}", actor.getFirstName(), actor.getLastName(), actor.getAge());
		}

		logger.info("Directors saved to database.");
		for (Director director: directorRepository.findAll()){
			logger.info("First Name: {}, Last Name: {}, Age: {}", director.getFirstName(), director.getLastName(), director.getAge());
		}
	}
}
