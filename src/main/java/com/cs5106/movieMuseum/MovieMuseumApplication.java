package com.cs5106.movieMuseum;

import com.cs5106.movieMuseum.domain.Movie;
import com.cs5106.movieMuseum.domain.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieMuseumApplication implements CommandLineRunner {
	private final MovieRepository movieRepository;
	private static final Logger logger = LoggerFactory.getLogger(MovieMuseumApplication.class);

    public MovieMuseumApplication(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(MovieMuseumApplication.class, args);
		logger.info("Application started successfully.");
	}

	@Override
	public void run(String... args) throws Exception {
		movieRepository.save(new Movie("The Shawshank Redemption", "Frank Darabont", 1994, 9.3));
		movieRepository.save(new Movie("The Godfather", "Francis Ford Coppola", 1972, 9.2));
		movieRepository.save(new Movie("The Dark Knight", "Christopher Nolan", 2008, 9.0));
		logger.info("Movies saved to database.");

		for (Movie movie : movieRepository.findAll()) {
			logger.info("Title: {}, Director: {}, Release Year: {}, IMDB Rating: {}", movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getImdbRating());
		}
	}
}
