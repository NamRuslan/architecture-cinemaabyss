package cinema.abyss.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
@RequestMapping("/api/movies")
public class MoviesGateway {
	@Value("${load_balance.movies.migration_percent:50}")
	private int moviesMigrationPercent;
	@Value("${load_balance.movies.url}")
	private String moviesServiceUrl;
	@Value("${load_balance.monolith.url}")
	private String monolithUrl;

	Logger log = LoggerFactory.getLogger(MoviesGateway.class);

	private final Random random = new Random();

	@GetMapping
	public Mono<ResponseEntity<String>> getAllMovies() {
		int rand = random.nextInt(100);
		boolean toMicroservice = rand < moviesMigrationPercent;
		String targetUrl = toMicroservice ? moviesServiceUrl : monolithUrl;

		return WebClient.create(targetUrl).get().uri("/api/movies").retrieve().toEntity(String.class);
	}
}
