package hh.fernuni.rentamovie.movie.domain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Year;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MovieRepositoryImpl implements MovieRepository {
	private static final Logger LOG = LoggerFactory.getLogger(MovieRepositoryImpl.class);
	private static final MovieRepositoryImpl INSTANCE = new MovieRepositoryImpl();
	private static final String DELIMITER = ",";
	private final Path path = Paths.get("./movie.db");
	private final Map<Long, Movie> repo = new ConcurrentHashMap<>();

	private MovieRepositoryImpl() {
		try {
			if (Files.exists(path)) {
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				for (String domainClassAsText : lines) {
					Movie domainClass = fromText(domainClassAsText.split(DELIMITER));
					repo.put(domainClass.getId(), domainClass);
				}
			} else {
				Files.createFile(path);
			}
		} catch (IOException e) {
			LOG.error("Error working with file in {}.", path.toString());
		}
	}

	public static MovieRepository getInstance() {
		return INSTANCE;
	}

	@Override
	public void save(Movie movie) {
		Movie put = repo.put(movie.getId(), movie);
		if (put == null) {
			try {
				Files.write(path, Collections.singletonList(toText(movie)), StandardCharsets.UTF_8,
						StandardOpenOption.APPEND);
			} catch (IOException e) {
				LOG.error("Error writing movie.db.");
			}
		} else {
			List<String> allMovies = repo.values().stream().map(MovieRepositoryImpl::toText)
					.collect(Collectors.toList());
			try {
				Files.write(path, allMovies, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				LOG.error("Error writing movie.db.");
			}
		}
	}

	@Override
	public Movie read(Long id) {
		return repo.get(id);
	}

	@Override
	public Collection<Movie> readAll() {
		return repo.values();
	}

	private Movie fromText(String[] strings) {
		return new Movie(Long.parseLong(strings[0]), Year.parse(strings[1]), strings[2]);
	}

	private static String toText(Movie movie) {
		StringBuilder b = new StringBuilder();
		b.append(movie.getId()).append(DELIMITER);
		b.append(movie.getYearOfPublication().toString()).append(DELIMITER);
		b.append(movie.getTitle());
		return b.toString();
	}
}
