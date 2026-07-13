package hh.fernuni.rentamovie.common.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CommonRepositoryImpl<T extends AbstractIdCarrier> implements CommonRepository<T> {
	private static final Logger LOG = LoggerFactory.getLogger(CommonRepositoryImpl.class);
	private final Map<Long, T> repo = new ConcurrentHashMap<>();
	private final Path path;
	protected static final String DELIMITER = ",";

	protected CommonRepositoryImpl(String filename) {
		path = Paths.get(filename);
		init();
	}

	protected void init() {
		try {
			if (Files.exists(path)) {
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				for (String domainClassAsText : lines) {
					if (domainClassAsText.isBlank()) {
						continue;
					}
					try {
						T domainClass = fromText(domainClassAsText.split(DELIMITER));
						repo.put(domainClass.getId(), domainClass);
					} catch (RuntimeException e) {
						LOG.error("Skipping unreadable line in {}: '{}' ({})", path, domainClassAsText, e.toString());
					}
				}
			} else {
				Files.createFile(path);
			}
        } catch (IOException _) {
            LOG.error("Error working with file in {}.", path);
		}
	}

	@Override
	public void save(T domainClass) {
		String domainClassAsText = toText(domainClass);
		T old = repo.put(domainClass.getId(), domainClass);

		if (old == null) {
			try {
				Files.write(path, Collections.singletonList(domainClassAsText), StandardCharsets.UTF_8,
						StandardOpenOption.APPEND);
            } catch (IOException _) {
				LOG.error("Error writing to db.");
			}
		} else {
            List<String> allEntities = repo.values().stream().map(this::toText).toList();
			try {
				Files.write(path, allEntities, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException _) {
				LOG.error("Error writing to db.");
			}
		}
	}

	@Override
	public T read(Long id) {
		return repo.get(id);
	}

	@Override
	public Collection<T> readAll() {
		return repo.values();
	}

	protected static String requireStorableText(String value) {
		if (value.contains(DELIMITER) || value.contains("\n") || value.contains("\r")) {
			throw new IllegalArgumentException("Text must not contain '" + DELIMITER + "' or line breaks: " + value);
		}
		return value;
	}

	protected abstract T fromText(String[] split);

	protected abstract String toText(T domainClass);

}
