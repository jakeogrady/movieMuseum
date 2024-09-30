package com.cs5106.movieMuseum;

import com.cs5106.movieMuseum.domain.*;
import com.cs5106.movieMuseum.domain.Genre;
import com.cs5106.movieMuseum.domain.Movie;
import com.cs5106.movieMuseum.domain.entity.*;
import com.cs5106.movieMuseum.domain.repository.*;
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
		Movie shawshank = new Movie("The Shawshank Redemption", "Frank Darabont", 1994, 9.3);
		Movie godfather = new Movie("The Godfather", "Francis Ford Coppola", 1972, 9.2);
		Movie darkKnight = new Movie("The Dark Knight", "Christopher Nolan", 2008, 9.0);

		movieRepository.save(shawshank);
		movieRepository.save(godfather);
		movieRepository.save(darkKnight);

		// Create genres
		Genre drama = new Genre("Drama", "Dramatic movies");
		Genre crime = new Genre("Crime", "Crime movies");
		Genre action = new Genre("Action", "Action movies");

		// Add genres to movies
		shawshank.addGenre(drama);
		godfather.addGenre(drama);
		godfather.addGenre(crime);
		darkKnight.addGenre(action);
		darkKnight.addGenre(crime);

		// Save genres
		genreRepository.save(drama);
		genreRepository.save(crime);
		genreRepository.save(action);

		// Save movies again to update the relationships
		movieRepository.save(shawshank);
		movieRepository.save(godfather);
		movieRepository.save(darkKnight);

		logger.info("Movies and genres saved to database.");

		for (Movie movie : movieRepository.findAll()) {
			logger.info("Title: {}, Director: {}, Release Year: {}, IMDB Rating: {}",
					movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getImdbRating());
		}


	}
}
